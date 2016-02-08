package palarax.e_key_card.UsersManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class profileSettings extends Fragment implements View.OnClickListener,palarax.e_key_card.UsersManager.profileChangeOptionDialog.dialogDoneListener{

    private profileChangeOptionDialog optionDialog;
    private FragmentManager fm ;
    private String changingFactor = "";

    public profileSettings() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        view.findViewById(R.id.change_name_btn).setOnClickListener(this);
        view.findViewById(R.id.change_email_btn).setOnClickListener(this);
        view.findViewById(R.id.change_pwd_btn).setOnClickListener(this);

        return view;
    }

    private void uploadUserData()
    {
        //TODO: upload user UID, geolocation
        //TODO: upload tag name + UID
        BackendlessUser user = Backendless.UserService.CurrentUser();
        if(user != null)
        {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_name_btn:
                displayOption("Email: ");
                changingFactor = "email";
                break;

            case R.id.change_email_btn:
                displayOption("Name: ");
                changingFactor = "name";
                break;

            case R.id.change_pwd_btn:
                displayOption("New Password: ");
                changingFactor = "password";
                break;

        }
    }

    private void displayOption(String mid_text)
    {
        optionDialog = new profileChangeOptionDialog();
        optionDialog.setView(mid_text);
        optionDialog.setListener(this);
        optionDialog.show(fm, "option_dialog");

    }

    @Override
    public void onDone(String inputText) {
        String email = "";
        String password = "";
        String name = "";

        if(!inputText.equals("") && changingFactor.equals("email"))
        {
            email = inputText;
            Toast.makeText(getContext(), "email: "+email, Toast.LENGTH_SHORT).show();
        }
        else if(!inputText.equals("") && changingFactor.equals("name"))
        {
            name = inputText;
            Toast.makeText(getContext(), "name: "+name, Toast.LENGTH_SHORT).show();
        }
        else if(!inputText.equals("") && changingFactor.equals("password"))
        {
            password = inputText;
            Toast.makeText(getContext(), "password: "+password, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "nothing", Toast.LENGTH_SHORT).show();
        }

    }
}
