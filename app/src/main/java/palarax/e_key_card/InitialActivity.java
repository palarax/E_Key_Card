package palarax.e_key_card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

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
                break;

            case R.id.guestBTN:
                nextActivity("Guest");
                break;

        }

    }


    public void registerOnClick() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void loginOnClick() {
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



    public void nextActivity(String name) {
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
