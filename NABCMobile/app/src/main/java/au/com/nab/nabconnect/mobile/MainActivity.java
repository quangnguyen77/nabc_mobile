package au.com.nab.nabconnect.mobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import au.com.nab.nabconnect.mobile.login.LoginFragment_;
import au.com.nab.nabconnect.sdk.filter.PaymentFilter;
import au.com.nab.nabconnect.sdk.response.PaymentsResponse;
import au.com.nab.nabconnect.sdk.response.SDKResponse;

@EActivity
public class MainActivity extends AppCompatActivity {

    @Bean
    NabConnectManager nabConnectManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (!nabConnectManager.getNabConnectApi().isLogin()) {
                showLogin();
            }
            else {
                System.out.println("Not implemented yet");
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, LoginFragment_.builder().build()).commit();
    }

    @Background
    public void doLogin(String username, String password) {
        SDKResponse response = nabConnectManager.getNabConnectApi().login(username, password);
        System.out.println("Login response " + response);
        // show payment fragment
        PaymentsResponse paymentsResponse = nabConnectManager.getNabConnectApi().getPayments(new PaymentFilter());
        System.out.println("Payment size " + paymentsResponse.getPaymentList().getTotalRecords());
    }
}
