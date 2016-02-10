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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class profileSettings extends Fragment implements View.OnClickListener, palarax.e_key_card.UsersManager.profileChangeOptionDialog.dialogDoneListener {

    private profileChangeOptionDialog optionDialog;
    private FragmentManager fm;
    private String changingFactor = "";

    public profileSettings() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BackendlessUser user = Backendless.UserService.CurrentUser();
        View view;
        if (user == null) {
            view = inflater.inflate(R.layout.guest_profile_fragment, container, false);
        } else {
            view = inflater.inflate(R.layout.profile_fragment, container, false);
            view.findViewById(R.id.change_name_btn).setOnClickListener(this);
            view.findViewById(R.id.change_email_btn).setOnClickListener(this);
            view.findViewById(R.id.change_pwd_btn).setOnClickListener(this);

            fm = getFragmentManager();
        }
        view.findViewById(R.id.btn4).setOnClickListener(this);

        return view;
    }

    /**
     * Updates user data
     * @param data  changed value
     */
    private void uploadUserData(String data) {
        BackendlessUser user = Backendless.UserService.CurrentUser();

        if (user != null) {
            switch (changingFactor) {
                case "email":
                    user.setEmail(data);
                    break;
                case "name":
                    user.setProperty("name", data);
                    break;
                case "password":
                    user.setPassword(data);
                    break;
            }
            updateUser(user);
        }
    }

    /**
     * Updates user
     * @param user  user instance to be updated
     */
    private void updateUser(BackendlessUser user) {
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getContext(), "Fail: " + fault, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_name_btn:
                displayOption("Name:");
                changingFactor = "name";
                break;

            case R.id.change_email_btn:
                displayOption("Email:");
                changingFactor = "email";
                break;

            case R.id.change_pwd_btn:
                displayOption("New Password:");
                changingFactor = "password";
                break;

            case R.id.btn4:
                test();
                break;

        }
    }

    /**
     * displays dialog to get user data
     * @param mid_text  value of the middle line
     */
    private void displayOption(String mid_text) {
        optionDialog = new profileChangeOptionDialog();
        optionDialog.setView(mid_text);
        optionDialog.setListener(this);
        optionDialog.show(fm, "option_dialog");

    }

    //What happens once Finish button is pressed
    @Override
    public void onDone(String inputText) {

        if (!inputText.equals("") && changingFactor.equals("email")) {
            uploadUserData(inputText);
        } else if (!inputText.equals("") && changingFactor.equals("name")) {
            uploadUserData(inputText);
        } else if (!inputText.equals("") && changingFactor.equals("password")) {
            uploadUserData(inputText);
        }
    }

    private void test() {

    Toast.makeText(getContext(), "test: ", Toast.LENGTH_SHORT).show();
    }


}
