package palarax.e_key_card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import palarax.e_key_card.initialActivities.Register;

/**
 * @author Ilya Thai
 */

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = InitialActivity.class.getSimpleName(); //used for debugging


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: implement transition animations
        setContentView(R.layout.content_main);
        //Initialise cloud api

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
        /*mKinveyClient.ping(new KinveyPingCallback() {
            public void onFailure(Throwable t) {
                Log.e(TAG, "Kinvey Ping Failed", t);
            }
            public void onSuccess(Boolean b) {
                Log.e(TAG, "Kinvey Ping Success");
            }
        });*/

    }

    public void signup() {
    }


    public void guestOnClick() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", "Guest");
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
