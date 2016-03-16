package au.com.nab.nabconnect.mobile.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import au.com.nab.nabconnect.mobile.MainActivity_;
import au.com.nab.nabconnect.mobile.R;

@EFragment(value = R.layout.login_fragment)
public class LoginFragment extends Fragment {

    @ViewById(R.id.username)
    EditText username;

    @ViewById(R.id.password)
    EditText password;

    @Click(R.id.login_button)
    public void login() {
        System.out.println("Login button click");
        MainActivity_ mainActivity = (MainActivity_) getActivity();
        mainActivity.doLogin(username.getText().toString(), password.getText().toString());


    }



}
