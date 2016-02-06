package palarax.e_key_card.UsersManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import palarax.e_key_card.R;

/**
 * @author
 */
public class profileSettings extends Fragment {

    TextView test;

    public profileSettings() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        test = (TextView) view.findViewById(R.id.test2);

        view.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadUserData();
            }
        });

        return view;
    }

    private void uploadUserData()
    {
        //TODO: upload user UID, geolocation
        //TODO: upload tag name + UID
        BackendlessUser user = Backendless.UserService.CurrentUser();
        if(user != null)
        {
            Toast.makeText(getContext(), "User ID: "+user.getUserId(), Toast.LENGTH_LONG).show();
            String s = new String(user.getProperty("Tags").toString());
            test.setText(user.getProperty("Tags").toString());

        }
    }
}
