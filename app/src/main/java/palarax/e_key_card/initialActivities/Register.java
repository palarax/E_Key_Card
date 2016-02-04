package palarax.e_key_card.initialActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import org.androidannotations.annotations.Background;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class Register extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Register.class.getSimpleName(); //used for debugging

    EditText username_edit,password_edit,email_edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.reg_register_btn).setOnClickListener(this);
        username_edit = (EditText) findViewById(R.id.reg_username_text);
        password_edit = (EditText) findViewById(R.id.reg_password_text);
        email_edit = (EditText) findViewById(R.id.reg_email_text);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        String username = username_edit.getText().toString();
        String password = password_edit.getText().toString();
        String email = email_edit.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(getApplicationContext(), "Please enter your username", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    //@Background
    private void async()
    {

    }
}
