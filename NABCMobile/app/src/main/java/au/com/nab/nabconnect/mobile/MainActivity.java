package au.com.nab.nabconnect.mobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import au.com.nab.nabconnect.mobile.login.LoginFragment;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new LoginFragment()).commit();
    }

    @Background
    public void doLogin(View view) {
        System.out.println("Login pressed");
        nabConnectManager.getNabConnectApi().login("abc","def");
    }
}
