package palarax.e_key_card.UsersManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import palarax.e_key_card.R;



/**
 * @author Ilya Thai
 */
public class profileSettings extends Fragment implements View.OnClickListener, palarax.e_key_card.UsersManager.profileChangeOptionDialog.dialogDoneListener,OnLocationUpdatedListener {

    private static final int RC_HANDLE_LOCATION_PERM = 200;

    private profileChangeOptionDialog optionDialog;
    private FragmentManager fm;
    private String changingFactor = "";
    private LocationGooglePlayServicesProvider provider;


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
        //locationActivity.setUp(getContext(),getActivity());
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
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                }
                else {
                    startLocation();
                }

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


    private void requestPermissions() {
        Log.e("profileSettings", "Location permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(getActivity(), permissions, RC_HANDLE_LOCATION_PERM);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("profileSettings", "onRequest");
        if (requestCode != RC_HANDLE_LOCATION_PERM) {
            Log.e("profileSettings", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e("profileSettings", "Location permission granted - initialize the camera source");
            // we have permission, so get location
            startLocation();
            return;
        }
    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(getContext()).logging(true).build();

        smartLocation.location(provider).start(this);
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }


    private void showLocation(Location location) {
        if (location != null) {
            final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }


    private void stopLocation() {
        SmartLocation.with(getContext()).location().stop();
        Toast.makeText(getContext(), "Location stoped!", Toast.LENGTH_SHORT).show();
    }

    private void showLast() {
        Location lastLocation = SmartLocation.with(getContext()).location().getLastLocation();
        if (lastLocation != null) {
            final String text = String.format("Latitude %.6f, Longitude %.6f",
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude());
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * method that will return whether the permission is accepted. By default it is true if the user is using a device below
     * version 23
     * @param permission
     * @return
     */
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
            return(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

}
