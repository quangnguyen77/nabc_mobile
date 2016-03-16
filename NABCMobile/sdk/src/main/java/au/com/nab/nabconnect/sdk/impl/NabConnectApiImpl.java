package au.com.nab.nabconnect.sdk.impl;

import android.content.SharedPreferences;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.nab.nabconnect.sdk.INabConnectApi;
import au.com.nab.nabconnect.sdk.filter.PaymentFilter;
import au.com.nab.nabconnect.sdk.internal.Constants;
import au.com.nab.nabconnect.sdk.internal.NabcEndPoint;
import au.com.nab.nabconnect.sdk.request.LoginRequest;
import au.com.nab.nabconnect.sdk.response.AccountBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountEndOfDayBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountTransactionsResponse;
import au.com.nab.nabconnect.sdk.response.PaymentsResponse;
import au.com.nab.nabconnect.sdk.response.SDKResponse;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public class NabConnectApiImpl implements INabConnectApi {


    /**
     * Connection url to connect to NABC server. In production with dual sites, call to NABC will be
     * resolved to one site and the client is expected to maintain connection to the site
     */
    private String nabConnectGlobalHost;
    /**
     * The url of the site to connect to
     */
    private String nabConnectSiteHost;

    /**
     * Flag to indicate if authentication with NABC is established
     */
    private boolean authenticated;

    private NabConnectClientHttpRequestFactory requestFactory;

    private RestTemplate nabcClient;

    public NabConnectApiImpl(SharedPreferences preferences) {
        this.nabConnectGlobalHost = preferences.getString(Constants.NABCONNECT_GLOBAL_HOST_KEY, "https://vm010040250031.aurdev.national.com.au:445");

        // initialise http request factory
        requestFactory = new NabConnectClientHttpRequestFactory(Constants.REQUEST_TIMEOUT);

        // initialise nabcClient
        nabcClient = new RestTemplate();
        nabcClient.setRequestFactory(requestFactory);
        nabcClient.getMessageConverters().add(new FormHttpMessageConverter());
        nabcClient.getMessageConverters().add(new StringHttpMessageConverter());
        nabcClient.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    }

    @Override
    public boolean isLogin() {
        return authenticated;
    }

    @Override
    public SDKResponse login(String username, String password) {
        // reset all authentication flags
        authenticated = false;
        try {
            String loginToken = extractLoginToken();
            LoginRequest loginRequest = new LoginRequest(username, password, loginToken);
            SDKResponse loginResponse = performLoginToNabc(loginRequest);
            // TODO if login is successful to NABC, perform back channel login to ESG


            return loginResponse;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new SDKResponse(SDKResponse.ResponseCode.SYSTEM_ERROR, "Invalid configuration: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new SDKResponse(SDKResponse.ResponseCode.NETWORK_ERROR, "Unable to read from NABC: " + e.getMessage());
        }
    }

    @Override
    public SDKResponse logout() {
        return null;
    }

    @Override
    public AccountBalancesResponse getAccountBalances() {
        return null;
    }

    @Override
    public AccountEndOfDayBalancesResponse getAccountEndOfDayBalances() {
        return null;
    }

    @Override
    public AccountTransactionsResponse getAccountTransactions(String accountId) {
        return null;
    }

    @Override
    public PaymentsResponse getPayments(PaymentFilter filter) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("page", new Integer(filter.getPage()).toString());
        map.add("rows", new Integer(filter.getRows()).toString());
        if (filter.isAuthorisable()) {
            map.add("selectedShowMeType", "AUTHORISABLE");
        }

        PaymentsResponse paymentsResponse = nabcClient.postForObject(nabConnectSiteHost + NabcEndPoint.GET_PAYMENTS_SERVICE.getValue(), map, PaymentsResponse.class);
        paymentsResponse.setResponseCode(SDKResponse.ResponseCode.SUCCESS);

        return paymentsResponse;
    }


    /**
     * Perform a read against the global url to get actual site. We need to send subsequent requests
     * for session stickiness
     */
    private String extractLoginToken() throws IOException, MalformedURLException {
        // extractLoginToken default cookie handler if this has not been setup
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
        // do a GET against global url to get site url
        HttpURLConnection urlConnection = null;
        String loginToken = null;
        try {
            URL globalUrl = new URL(nabConnectGlobalHost + NabcEndPoint.INITIAL_LOGIN_SERVICE.getValue());
            urlConnection = requestFactory.openConnection(globalUrl, "GET");
            // read from the connection so that we can extract the login token
            String response = readStream(urlConnection.getInputStream());

            loginToken = extractLoginToken(response);

            // parse the redirected url
            URL siteURL = urlConnection.getURL();

            nabConnectSiteHost = siteURL.getProtocol() + "://" + siteURL.getHost();
            if (siteURL.getPort() != - 1) {
                nabConnectSiteHost = nabConnectSiteHost + ":" + siteURL.getPort();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return loginToken;
    }

    /**
     * Perform login into NABC
     * @param loginRequest
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private SDKResponse performLoginToNabc(LoginRequest loginRequest) throws MalformedURLException, IOException {
        // do a GET against global url to get site url

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("org.apache.struts.taglib.html.TOKEN", loginRequest.getToken());
        map.add("value(siteContext)", "desktop");
        map.add("value(userId)", loginRequest.getUsername());
        map.add("value(password)", loginRequest.getPassword());
        map.add("value(loginButton)", "Login");
        String loginResponse = nabcClient.postForObject(nabConnectSiteHost + NabcEndPoint.AUTHENTICATE_SERVICE.getValue(), map, String.class);

        // if the response contains User ID token then we are good
        if (loginResponse.contains(Constants.USER_ID_TOKEN)) {
            return new SDKResponse(SDKResponse.ResponseCode.SUCCESS, "");
        }
        // if it contains Broadcast message, we need to perform further action to skip
        // broadcast message
        if (loginResponse.contains(Constants.BROADCAST_MESSAGE_TOKEN)) {
            // perform further call to skip broadcast message
            return new SDKResponse(SDKResponse.ResponseCode.SUCCESS_WITH_BROADCAST, "");
        }


        return new SDKResponse(SDKResponse.ResponseCode.SYSTEM_ERROR, "");
    }

    /**
     * Extract the org.apache.struts.taglib.html.TOKEN value from the given html.<br>
     * The token will be in form name="org.apache.struts.taglib.html.TOKEN" value="xxxxxxxxxxxx"
     * @param html
     * @return
     */
    private String extractLoginToken(String html) {
        String regEx = "TOKEN\" value=\"(\\w+?)\"";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }


    private static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            line = reader.readLine();
            while( line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
