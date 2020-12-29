package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class InAppPurchaseActivity extends Activity {

    private AwSIapManager AwSIapManager;
    summaryDBHelper sumDB;
    LoginDBHelper loginDB;
    SKUHelper skuDB;
    String studentName;
    String adminEmail;

    /**
     * Setup IAP SDK and other UI related objects specific to this sample
     * application.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        summaryDBHelper sumDB = new summaryDBHelper(this);

        SKUHelper skuDB = new SKUHelper(this);

        LoginDBHelper loginDB = new LoginDBHelper(this);
        String[] namePin = new String[] {studentName};
        adminEmail = loginDB.getEmailAddressByName(namePin);

        setupApplicationSpecificOnCreate();
        setupIAPOnCreate(sumDB, skuDB);

        try {
            updateEmailsInView(AwSIapManager.getUserIapData().getRemainingEmails());
        }catch (Exception e){
           // updateEmailsInView(25);
           // System.out.println("Presumably, this user is new.");
        }
    }

    /**
     * Setup for IAP SDK called from onCreate. Sets up {@link AwSIapManager}
     * to handle InAppPurchasing logic and {@link AWSPurchasingListener} for
     * listening to IAP API callbacks
     */
    private void setupIAPOnCreate(summaryDBHelper sumDB, SKUHelper skuDB) {
        AwSIapManager = new AwSIapManager(InAppPurchaseActivity.this);
        AwSIapManager.activate();
        final AWSPurchasingListener purchasingListener = new AWSPurchasingListener(AwSIapManager, sumDB, skuDB, adminEmail,this);
        Log.d(TAG, "onCreate: registering PurchasingListener");
        PurchasingService.registerListener(this.getApplicationContext(), purchasingListener);
        Log.d(TAG, "IS_SANDBOX_MODE:" + PurchasingService.IS_SANDBOX_MODE);
    }

    /**
     * Call {@link PurchasingService#getProductData(Set)} to get the product
     * availability
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: call getProductData for skus: " + MySku.values());
        final Set<String> productSkus = new HashSet<String>();
        for (final MySku mySku : MySku.values()) {
            productSkus.add(mySku.getSku());
        }
        PurchasingService.getProductData(productSkus);
    }

    /**
     * Calls {@link PurchasingService#getUserData()} to get current Amazon
     * user's data and {@link PurchasingService#getPurchaseUpdates(boolean)} to
     * get recent purchase updates
     */
    @Override
    protected void onResume() {
        super.onResume();
        AwSIapManager.activate();
        Log.d(TAG, "onResume: call getUserData");
        PurchasingService.getUserData();
        Log.d(TAG, "onResume: getPurchaseUpdates");
        PurchasingService.getPurchaseUpdates(false);

        try {
            updateEmailsInView(AwSIapManager.getUserIapData().getRemainingEmails());
        }catch (Exception e){

        }
    }

    /**
     * Deactivate Sample IAP manager on main activity's Pause event
     */
    @Override
    protected void onPause() {
        super.onPause();
        AwSIapManager.deactivate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        Intent main = new Intent(getApplicationContext(),adminHomePage.class);
        main.putExtra("Name",studentName);
        startActivity(main);
        finish();
    }


    /**
     * Click handler invoked when user clicks button to buy an orange
     * consumable. This method calls {@link PurchasingService#purchase(String)}
     * with the SKU to initialize the purchase from Amazon Appstore
     */
    public void onUnlockEmail5000Click(final View view) {

        final RequestId requestId = PurchasingService.purchase(MySku.ADDEMAILS.getSku());
        Log.d(TAG, "onUnlockEmail5000Click: requestId (" + requestId + ")");

     }

    /**
     * Click handler called when user clicks button to eat an orange consumable.
     */

    public void onEatOrangeClick(final View view) {
        try {
            AwSIapManager.consumeEmailPurchase(sumDB);
            Log.d(TAG, "onEatOrangeClick: consuming 1 orange");

            updateEmailsInView(AwSIapManager.getUserIapData().getRemainingEmails());
        } catch (final RuntimeException e) {
            showMessage("Unknow error when eat Orange");
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////// Application specific code below
    // ////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////

    private static final String TAG = "IAPConsumablesApp";
    protected Handler guiThreadHandler;

    protected Button buyButton;

    protected TextView numEmails;
    protected TextView termsAndConditions;

    /**
     * Setup application specific things, called from onCreate()
     */
    protected void setupApplicationSpecificOnCreate() {
        setContentView(R.layout.inapppurchase);

        buyButton = (Button) findViewById(R.id.buy_button);

        numEmails = (TextView) findViewById(R.id.num_emails);

        termsAndConditions = (TextView) findViewById(R.id.termsAndCond);

        // Set Text in terms and conditions textview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termsAndConditions.setText(Html.fromHtml(getString(R.string.termsandcond), Html.FROM_HTML_MODE_COMPACT));
        } else {
            termsAndConditions.setText(Html.fromHtml(getString(R.string.termsandcond)));
        }

        guiThreadHandler = new Handler();
    }

    /**
     * Disable buy button for any unavailable SKUs. In this sample app, this
     * would just disable "Buy Orange" button
     *
     * @param unavailableSkus
     */

    protected void disableButtonsForUnavailableSkus(final Set<String> unavailableSkus) {
        for (final String unavailableSku : unavailableSkus) {
            if (MySku.ADDEMAILS.getSku().equals(unavailableSku)) {
                Log.d(TAG, "disableButtonsForUnavailableSkus: disabling buyEmailsButton");
                disableBuyButton();
            }
        }
    }

    /**
     * Disable "Buy Orange" button
     */
    void disableBuyButton() {
        buyButton.setEnabled(false);
    }


    /**
     * Enable "Buy Orange" button
    */
    void enableBuyButton() {
        buyButton.setEnabled(true);
    }


    /**
     * Update view with how many oranges I have and how many I've consumed.
     *
     * @param haveQuantity
     */
    protected void updateEmailsInView(final int haveQuantity) {

        Log.d(TAG, "updateEmailsInView with haveQuantity (" + haveQuantity
                + ")");
        guiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                numEmails.setText(String.valueOf(haveQuantity));
            }
        });
    }

    /**
     * Show message on UI
     *
     * @param message
     */
    public void showMessage(final String message) {
        Toast.makeText(InAppPurchaseActivity.this, message, Toast.LENGTH_LONG).show();
    }


}
