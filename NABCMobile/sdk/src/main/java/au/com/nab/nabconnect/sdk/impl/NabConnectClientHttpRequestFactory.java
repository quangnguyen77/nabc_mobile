package au.com.nab.nabconnect.sdk.impl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

/**
 * Created by nabcmacbook1 on 15/03/2016.
 */
public class NabConnectClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    private int timeout;

    public NabConnectClientHttpRequestFactory(int timeout) {
        this.timeout = timeout;
        setReadTimeout(timeout);
        setConnectTimeout(timeout);
    }

    public HttpURLConnection openConnection(URL url, String httpMethod) throws IOException {
        HttpURLConnection connection = super.openConnection(url, null);
        prepareConnection(connection, httpMethod);
        return connection;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(initSSLContext().getSocketFactory());
        }
        super.prepareConnection(connection, httpMethod);
    }

    /**
     * Setup SSL Context to trust everyone for now
     * @Todo need to bief up certificate verification
     * @return
     */
    private SSLContext initSSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            return context;
        } catch (final Exception ex) {
            return null;
        }
    }
}
