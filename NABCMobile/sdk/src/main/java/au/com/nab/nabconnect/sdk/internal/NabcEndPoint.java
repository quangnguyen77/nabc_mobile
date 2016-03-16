package au.com.nab.nabconnect.sdk.internal;

/**
 * Created by nabcmacbook1 on 16/03/2016.
 */
public enum NabcEndPoint {

    INITIAL_LOGIN_SERVICE("/auth/nabclogin/login.do"),
    AUTHENTICATE_SERVICE("/auth/nabclogin/authenticate.do"),
    GET_PAYMENTS_SERVICE("/appPayments/pmt/getPaymentListNoSession.html");

    private String value;

    NabcEndPoint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
