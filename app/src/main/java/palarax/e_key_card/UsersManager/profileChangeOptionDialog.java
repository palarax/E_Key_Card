package palarax.e_key_card.UsersManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import palarax.e_key_card.R;
import palarax.e_key_card.initialActivities.DefaultCallback;

/**
 * @author Ilya Thai
 */
public class profileChangeOptionDialog extends DialogFragment {

    public static final String TAG = "dialog";
    private String mid_text;
    private String data;

    private dialogDoneListener mListener;

    public void setListener(dialogDoneListener listener) {
        mListener = listener;
    }

    public profileChangeOptionDialog() {
    }

    /**
     * listens for the onDone
     */
    public interface dialogDoneListener {
        void onDone(String inputText);
    }


    /**
     * sets mid_text as it determines the view
     * @param mid_text  middle value
     */
    public void setView(String mid_text) {
        this.mid_text = mid_text;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.write_options_dialog, container);

        //hiding bottom line since we aren't using it
        EditText write_bot = (EditText) view.findViewById(R.id.dialog_write_bot);
        TextView bot = (TextView) view.findViewById(R.id.dialog_text_bot);
        bot.setVisibility(View.INVISIBLE);
        write_bot.setVisibility(View.INVISIBLE);

        TextView top = (TextView) view.findViewById(R.id.dialog_text_top);
        TextView mid = (TextView) view.findViewById(R.id.dialog_text_mid);

        final EditText write_top = (EditText) view.findViewById(R.id.dialog_write_top);
        final EditText write_mid = (EditText) view.findViewById(R.id.dialog_write_mid);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams)top.getLayoutParams();
        params.topMargin=150;   //putting the layout in the middle

        top.setLayoutParams(params);
        top.setText(mid_text);
        mid.setText("  Old Password:");
        mid.setTextSize(15);
        top.setTextSize(15);

        TextView btn = (TextView) view.findViewById(R.id.finishOption);
        btn.setText("Update");
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                BackendlessUser user = Backendless.UserService.CurrentUser();
                String email = user.getEmail();
                // Perform action on click
                Log.e(TAG, "button listener");

                if (!write_top.getText().toString().isEmpty()) {
                    data = write_top.getText().toString();
                } else {
                    data = "";
                }

                if (!write_mid.getText().toString().isEmpty()) {

                    //checks if password matches user's password
                    Backendless.UserService.login(email, write_mid.getText().toString(), new DefaultCallback<BackendlessUser>(getContext()) {
                        public void handleResponse(BackendlessUser backendlessUser) {
                            super.handleResponse(backendlessUser);

                            if (!Backendless.UserService.isValidLogin()) {
                                Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                data = "";
                            }

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    data = "";
                }

                if (!data.isEmpty()) {
                    mListener.onDone(data);
                    dismiss();
                }

            }
        });
        return view;
    }
}
