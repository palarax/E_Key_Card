package palarax.e_key_card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;
import palarax.e_key_card.initialActivities.Register;

/**
 * @author Ilya Thai
 */

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {


    // https://www.cloudboost.io/quickstart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: implement transition animations
        setContentView(R.layout.content_main);
        //Initialise cloudboost api
        CloudApp.init("ecard", "wsI7LudUN2cmLulZJhbKRK5n+jBTQI24AmJmm1KxRqI=");

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
                Toast.makeText(getApplicationContext(), "Login test", Toast.LENGTH_LONG).show();
                loginOnClick();
                break;

            case R.id.guestBTN:
                guestOnClick();
                break;

        }

    }


    public void registerOnClick() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void loginOnClick(){
        /*
        CloudUser user = new CloudUser();
        user.setUserName("username");
        user.setPassword("password");
        try {
            user.logIn(new CloudUserCallback() {
                @Override
                public void done(CloudUser object, CloudException e) throws CloudException {
                    if (e != null) {
                    }
                    if (object != null) {
                        //
                    }
                }
            });
        }catch (CloudException e){}
        */
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", "Ilya");
        startActivity(intent);
    }

    public void signup() {
        CloudUser obj = new CloudUser();
        obj.setUserName("username");
        obj.setPassword("password");
        obj.setEmail("hello@abc.com");

        //catch errors u twat
        try {
            obj.signUp(new CloudUserCallback() {
                @Override
                public void done(CloudUser object, CloudException e) throws CloudException {
                    if (e != null) {
                    }
                    if (object != null) {
                    }
                }
            });
        }catch(CloudException e){}
    }


    public void guestOnClick() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", "Guest");
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();
        CloudApp.init("ecard", "wsI7LudUN2cmLulZJhbKRK5n+jBTQI24AmJmm1KxRqI=");

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
