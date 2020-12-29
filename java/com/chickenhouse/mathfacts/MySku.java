package com.chickenhouse.mathfacts;

/**
 * 
 * MySku enum contains all In App Purchase products definition that the sample
 * app will use. The product definition includes two properties: "SKU" and
 * "Available Marketplace".
 * 
 */
public enum MySku {

    // This is the product to purchase and eat in the sample code.
    ADDEMAILS("5k_Additional_Emails", "US");

    // This is a sample product to show how IAP SDK handle not supported
    // products
    //APPLE("com.amazon.mathfacts.iap.consumable.apple", "US");

    private final String sku;
    private final String availableMarkpetplace;

    /**
     * Returns the MySku object from the specified Sku and marketplace value.
     * 
     * @param sku
     * @param marketplace
     * @return
     */
    public static MySku fromSku(final String sku, final String marketplace) {
        if (ADDEMAILS.getSku().equals(sku) && (ADDEMAILS.getAvailableMarketplace() == marketplace || ADDEMAILS.getAvailableMarketplace().equals(marketplace))) {
            return ADDEMAILS;
        }
        return null;
    }

    /**
     * Returns the Sku string of the MySku object
     * 
     * @return
     */
    public String getSku() {
        return this.sku;
    }

    /**
     * Returns the Available Marketplace of the MySku object
     * 
     * @return
     */
    public String getAvailableMarketplace() {
        return this.availableMarkpetplace;
    }

    private MySku(final String sku, final String availableMarkpetplace) {
        this.sku = sku;
        this.availableMarkpetplace = availableMarkpetplace;
    }

}
