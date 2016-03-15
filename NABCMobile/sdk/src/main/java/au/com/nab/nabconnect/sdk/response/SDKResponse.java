package au.com.nab.nabconnect.sdk.response;

/**
 * Created by quangnguyen on 4/02/2016.
 */
public class SDKResponse {
    public enum ResponseCode {
        SUCCESS(0), SYSTEM_ERROR(1), NETWORK_ERROR(2);

        private int value;
        private ResponseCode(int value) {
            this.value = value;
        }
    }

    private ResponseCode responseCode;
    private String message;

    public SDKResponse() {
    }

    public SDKResponse(ResponseCode responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
