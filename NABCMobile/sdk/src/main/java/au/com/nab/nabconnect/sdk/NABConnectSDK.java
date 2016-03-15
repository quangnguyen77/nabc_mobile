package au.com.nab.nabconnect.sdk;

import android.content.SharedPreferences;

import au.com.nab.nabconnect.sdk.impl.NabConnectApiImpl;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public class NABConnectSDK {


    public static INabConnectApi getNabConnectApi(SharedPreferences preferences) {
        return new NabConnectApiImpl(preferences);
    }
}
