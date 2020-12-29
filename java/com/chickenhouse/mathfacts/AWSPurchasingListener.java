package com.chickenhouse.mathfacts;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

/**
 * Implementation of {@link PurchasingListener} that listens to Amazon
 * InAppPurchase SDK's events, and call {@link AwSIapManager} to handle the
 * purchase business logic.
 */
public class AWSPurchasingListener implements PurchasingListener {

    private static final String TAG = "IAPConsumablesApp";

    private final AwSIapManager iapManager;
    private final summaryDBHelper sumDB;
    private final SKUHelper skuDB;
    String adminEmail;
    Context cntxt;

    public AWSPurchasingListener(final AwSIapManager iapManager, summaryDBHelper sumDB, SKUHelper skuDB, String administratorEmail, Context context) {

        this.iapManager = iapManager;
        this.sumDB = sumDB;
        this.skuDB = skuDB;
        adminEmail = administratorEmail;
        this.cntxt = context;
    }

    /**
     * This is the callback for {@link PurchasingService#getUserData}. For
     * successful case, get the current user from {@link UserDataResponse} and
     * call {@link AwSIapManager#setAmazonUserId} method to load the Amazon
     * user and related purchase information
     * 
     * @param response
     */
    @Override
    public void onUserDataResponse(final UserDataResponse response) {
        Log.d(TAG, "onGetUserDataResponse: requestId (" + response.getRequestId()
                   + ") userIdRequestStatus: "
                   + response.getRequestStatus()
                   + ")");

        final UserDataResponse.RequestStatus status = response.getRequestStatus();
        switch (status) {
        case SUCCESSFUL:
            Log.d(TAG, "onUserDataResponse: get user id (" + response.getUserData().getUserId()
                       + ", marketplace ("
                       + response.getUserData().getMarketplace()
                       + ") ");
            iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
            break;

        case FAILED:
        case NOT_SUPPORTED:
            Log.d(TAG, "onUserDataResponse failed, status code is " + status);
            iapManager.setAmazonUserId(null, null);
            break;
        }
    }

    /**
     * This is the callback for {@link PurchasingService#getProductData}. After
     * SDK sends the product details and availability to this method, it will
     * call {@link AwSIapManager#enablePurchaseForSkus}
     * {@link AwSIapManager#disablePurchaseForSkus} or
     * {@link AwSIapManager#disableAllPurchases} method to set the purchase
     * status accordingly.
     */
    @Override
    public void onProductDataResponse(final ProductDataResponse response) {
        final ProductDataResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onProductDataResponse: RequestStatus (" + status + ")");

        switch (status) {
        case SUCCESSFUL:
            Log.d(TAG, "onProductDataResponse: successful.  The item data map in this response includes the valid SKUs");
            final Set<String> unavailableSkus = response.getUnavailableSkus();
            Log.d(TAG, "onProductDataResponse: " + unavailableSkus.size() + " unavailable skus");
            iapManager.enablePurchaseForSkus(response.getProductData());
            iapManager.disablePurchaseForSkus(response.getUnavailableSkus());
            break;
        case FAILED:
        case NOT_SUPPORTED:
            Log.d(TAG, "onProductDataResponse: failed, should retry request");
            iapManager.disableAllPurchases();
            break;
        }
    }

    /**
     * This is the callback for {@link PurchasingService#getPurchaseUpdates}.
     * 
     * We will receive Consumable receipts from this callback if the consumable
     * receipts are not marked as "FULFILLED" in Amazon Appstore. So for every
     * single Consumable receipts in the response, we need to call
     * {@link AwSIapManager#handleReceipt} to fulfill the purchase.
     * 
     */
    @Override
    public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse response) {
        Log.d(TAG, "onPurchaseUpdatesResponse: requestId (" + response.getRequestId()
                   + ") purchaseUpdatesResponseStatus ("
                   + response.getRequestStatus()
                   + ") userId ("
                   + response.getUserData().getUserId()
                   + ")");


        final PurchaseUpdatesResponse.RequestStatus status = response.getRequestStatus();
        switch (status) {
        case SUCCESSFUL:
            iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
            for (final Receipt receipt : response.getReceipts()) {
                iapManager.handleReceipt(receipt, response.getUserData(), sumDB, skuDB, adminEmail);
            }
            if (response.hasMore()) {
                PurchasingService.getPurchaseUpdates(false);
                System.out.println("PurchasingService.getPurchaseUpdates(false) just ran.");
            }


           // PurchasingService.getPurchaseUpdates(false);
            //PurchasingService.notifyFulfillment(String.valueOf(response.getReceipts()), FulfillmentResult.FULFILLED);

            System.out.println("SUCCESSFUL Next Is to Unlock Emails");
            // Need find how to have flexibility in additional Emails.
            iapManager.unlockEmails(sumDB);
            break;
        case FAILED:
        case NOT_SUPPORTED:
            Log.d(TAG, "onProductDataResponse: failed, should retry request");
            iapManager.disableAllPurchases();
            break;
        }

    }

    /**
     * This is the callback for {@link PurchasingService#purchase}. For each
     * time the application sends a purchase request
     * {@link PurchasingService#purchase}, Amazon Appstore will call this
     * callback when the purchase request is completed. If the RequestStatus is
     * Successful or AlreadyPurchased then application needs to call
     * {@link AwSIapManager#handleReceipt} to handle the purchase
     * fulfillment. If the RequestStatus is INVALID_SKU, NOT_SUPPORTED, or
     * FAILED, notify corresponding method of {@link AwSIapManager} .
     */
    @Override
    public void onPurchaseResponse(final PurchaseResponse response) {
        final String requestId = response.getRequestId().toString();
        final String userId = response.getUserData().getUserId();
        final PurchaseResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onPurchaseResponse: requestId (" + requestId
                   + ") userId ("
                   + userId
                   + ") purchaseRequestStatus ("
                   + status
                   + ")");

        switch (status) {
        case SUCCESSFUL:
            final Receipt receipt = response.getReceipt();
            iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
            Log.d(TAG, "onPurchaseResponse: receipt json:" + receipt.toJSON());



            iapManager.handleReceipt(receipt, response.getUserData(),sumDB, skuDB, adminEmail);

            // for now it is consistently 5000 but ultimately should change to a variable.
            iapManager.unlockEmails(sumDB);

            // Added to recorded purchases on my database.
            try{
                recPurchases recordPurchases = new recPurchases(cntxt);

                // Try to update the interface logic
                try {
                    recordPurchases.update();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

                recordPurchases.execute(adminEmail);

            }catch (Exception e) {

                System.out.println("Submission to MagClan Database failed: " + adminEmail);

            }



            break;
        case ALREADY_PURCHASED:
            Log.d(TAG, "onPurchaseResponse: already purchased, should never get here for a consumable.");
            // This is not applicable for consumable item. It is only
            // application for entitlement and subscription.
            // check related samples for more details.
            break;
        case INVALID_SKU:
            Log.d(TAG,
                  "onPurchaseResponse: invalid SKU!  onProductDataResponse should have disabled buy button already.");
            final Set<String> unavailableSkus = new HashSet<String>();
            unavailableSkus.add(response.getReceipt().getSku());
            iapManager.disablePurchaseForSkus(unavailableSkus);
            break;
        case FAILED:
        case NOT_SUPPORTED:
            Log.d(TAG, "onPurchaseResponse: failed so remove purchase request from local storage");
            iapManager.purchaseFailed(response.getReceipt().getSku());
            break;
        }

    }

}
