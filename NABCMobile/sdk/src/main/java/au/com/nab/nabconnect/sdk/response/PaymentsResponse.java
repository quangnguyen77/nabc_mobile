package au.com.nab.nabconnect.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.nab.nabconnect.sdk.model.PaymentList;

/**
 * Created by quangnguyen on 4/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentsResponse extends SDKResponse {
    private static final String SUCCESS = "SUCCESS";

    private String status;

    @JsonProperty("data")
    private PaymentList paymentList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentList getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(PaymentList paymentList) {
        this.paymentList = paymentList;
    }

    public boolean isSuccess() {
        return SUCCESS.equalsIgnoreCase(status);
    }
}
