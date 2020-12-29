package com.chickenhouse.mathfacts;


/**
 * This is a simple example used in Amazon InAppPurchase Sample App, to show how
 * developer's application holding the customer's InAppPurchase data.
 * 
 * 
 */
public class UserIapData {
    private volatile int remainingEmails = 0;
    private volatile int consumedEmails = 0;
    private final String amazonUserId;
    private final String amazonMarketplace;
    public String getAmazonUserId() {
        return amazonUserId;
    }

    public String getAmazonMarketplace() {
        return amazonMarketplace;
    }

    public void setRemainingEmails(final int remaining) {
        this.remainingEmails = remaining;
    }

    public void setConsumedEmails(final int consumed) {
        this.consumedEmails = consumed;
    }

    public int getRemainingEmails() {
        return this.remainingEmails;
    }

    public int getConsumedEmails() {
        return this.consumedEmails;
    }

    public UserIapData(final String amazonUserId, final String amazonMarketplace) {
        this.amazonUserId = amazonUserId;
        this.amazonMarketplace = amazonMarketplace;
    }
}
