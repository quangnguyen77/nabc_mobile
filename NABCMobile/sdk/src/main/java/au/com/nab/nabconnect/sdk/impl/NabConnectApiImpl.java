package au.com.nab.nabconnect.sdk.impl;

import android.content.SharedPreferences;

import org.springframework.http.client.ClientHttpRequestFactory;
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

import javax.net.ssl.HttpsURLConnection;

import au.com.nab.nabconnect.sdk.INabConnectApi;
import au.com.nab.nabconnect.sdk.filter.PaymentFilter;
import au.com.nab.nabconnect.sdk.response.AccountBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountEndOfDayBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountTransactionsResponse;
import au.com.nab.nabconnect.sdk.response.PaymentsResponse;
import au.com.nab.nabconnect.sdk.response.SDKResponse;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public class NabConnectApiImpl implements INabConnectApi {

    private static final String DEFAULT_NABCONNECT_GLOBAL_HOST = "https://nabconnect.nab.com.au";
    private static final String NABCONNECT_GLOBAL_HOST_KEY = "NABConnectGlobalHost";
    public static final int REQUEST_TIMEOUT = 20000;
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
        this.nabConnectGlobalHost = preferences.getString(NABCONNECT_GLOBAL_HOST_KEY, DEFAULT_NABCONNECT_GLOBAL_HOST);

        // initialise http request factory
        requestFactory = new NabConnectClientHttpRequestFactory(REQUEST_TIMEOUT);

        nabcClient = new RestTemplate();
        nabcClient.setRequestFactory(requestFactory);

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
            resolveSiteConnection();
        } catch (MalformedURLException e) {
            return new SDKResponse(SDKResponse.ResponseCode.SYSTEM_ERROR, "Invalid configuration");
        } catch (IOException e) {
            return new SDKResponse(SDKResponse.ResponseCode.NETWORK_ERROR, "");
        }

        return null;
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
        return null;
    }


    /**
     * Perform a read against the global url to get actual site. We need to send subsequent requests
     * for session stickiness
     */
    private void resolveSiteConnection() throws IOException, MalformedURLException {
        // resolveSiteConnection default cookie handler if this has not been setup
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
        // do a GET against global url to get site url
        HttpURLConnection urlConnection = null;
        try {
            URL globalUrl = new URL(nabConnectGlobalHost);
            urlConnection = requestFactory.openConnection(globalUrl, "GET");
            // read from the connection so that we can resolve the actual site, we don't
            // worry too much about the response
            readStream(urlConnection.getInputStream());

            URL siteURL = urlConnection.getURL();
            nabConnectSiteHost = siteURL.getProtocol() + "://" + siteURL.getHost() + ":" + siteURL.getPort();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
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
