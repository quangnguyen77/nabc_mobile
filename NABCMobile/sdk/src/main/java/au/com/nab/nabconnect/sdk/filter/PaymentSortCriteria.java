package au.com.nab.nabconnect.sdk.filter;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public enum PaymentSortCriteria {
    PAYMENT_ID("paymentId");

    private String value;

    private PaymentSortCriteria(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
};
