package au.com.nab.nabconnect.mobile;

import android.content.Context;
import android.content.SharedPreferences;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import au.com.nab.nabconnect.sdk.INabConnectApi;
import au.com.nab.nabconnect.sdk.NABConnectSDK;

/**
 * This class is a wrapper class on NabConnectApi to provide singleton access to NabConnectSDK
 */
@EBean (scope = EBean.Scope.Singleton)
public class NabConnectManager {
    @RootContext
    Context context;

    private INabConnectApi nabConnectApi;

    @AfterInject
    void init() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nabc-preference", Context.MODE_PRIVATE);
        nabConnectApi = NABConnectSDK.getNabConnectApi(sharedPreferences);
    }

    public INabConnectApi getNabConnectApi() {
        return nabConnectApi;
    }
}
