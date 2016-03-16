package au.com.nab.nabconnect.sdk.request;

/**
 * Created by nabcmacbook1 on 16/03/2016.
 */
public class LoginRequest {
    private static final String NATIVE_APP_SITE_CONTEXT = "desktop";

    private String username;
    private String password;
    private String token;

    public LoginRequest(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
