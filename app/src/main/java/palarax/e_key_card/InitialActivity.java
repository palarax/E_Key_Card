package palarax.e_key_card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import palarax.e_key_card.initialActivities.DefaultCallback;
import palarax.e_key_card.initialActivities.Register;

/**
 * @author Ilya Thai
 */

public class InitialActivity extends Activity implements View.OnClickListener {

    private static final String TAG = InitialActivity.class.getSimpleName(); //used for debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: implement transition animations
        setContentView(R.layout.content_main);
        //Initialise cloud api
        Backendless.setUrl(BackEndlessDefaults.SERVER_URL);
        Backendless.initApp(this, BackEndlessDefaults.APPLICATION_ID, BackEndlessDefaults.SECRET_KEY, BackEndlessDefaults.VERSION);
        //Initialise buttons listeners
        findViewById(R.id.loginBTN).setOnClickListener(this);
        findViewById(R.id.registerBTN).setOnClickListener(this);
        findViewById(R.id.guestBTN).setOnClickListener(this);
        findViewById(R.id.passwordreset).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //TODO: chooks activity
            case R.id.registerBTN:
                registerOnClick();
                break;

            case R.id.loginBTN:
                loginOnClick();
                hideKeyboard();
                break;

            case R.id.guestBTN:
                nextActivity("Guest");
                hideKeyboard();
                break;

            case R.id.passwordreset:
                passwordReset();
                hideKeyboard();
                break;
        }

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void passwordReset()
    {
        final EditText emailField = (EditText) findViewById(R.id.email);
        if(TextUtils.isEmpty(emailField.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
        }
        else {
            Backendless.UserService.restorePassword(emailField.getText().toString(), new AsyncCallback<Void>() {
                public void handleResponse(Void response) {
                    Toast.makeText(getApplicationContext(), "Email with temporary password has been sent", Toast.LENGTH_LONG).show();
                }

                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(InitialActivity.this, fault.getCode(), Toast.LENGTH_SHORT).show();
                    // password revovery failed, to get the error code call fault.getCode()
                }
            });
        }
    }


    private void registerOnClick() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void loginOnClick() {
        final EditText emailField = (EditText) findViewById(R.id.email);
        final EditText passwordField = (EditText) findViewById(R.id.password);
        Backendless.UserService.login(emailField.getText().toString(), passwordField.getText().toString(), new DefaultCallback<BackendlessUser>(InitialActivity.this) {
            public void handleResponse(BackendlessUser backendlessUser) {
                super.handleResponse(backendlessUser);
                BackendlessUser user = Backendless.UserService.CurrentUser();
                nextActivity((String) user.getProperty("name"));
            }
        });
    }



    private void nextActivity(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();
        Backendless.setUrl(BackEndlessDefaults.SERVER_URL);
        Backendless.initApp(this, BackEndlessDefaults.APPLICATION_ID, BackEndlessDefaults.SECRET_KEY, BackEndlessDefaults.VERSION);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
