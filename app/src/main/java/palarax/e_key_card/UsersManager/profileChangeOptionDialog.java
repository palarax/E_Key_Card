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

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class profileChangeOptionDialog extends DialogFragment {

    public static final String TAG = "dialog";
    private String mid_text;
    private String[] data;

    private dialogDoneListener mListener;

    public void setListener(dialogDoneListener listener) {
        mListener = listener;
    }

    public profileChangeOptionDialog() {

    }

    public interface dialogDoneListener {
        void onDone(String inputText[]);
    }


    public void setView(String mid_text)
    {
        this.mid_text = mid_text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.write_options_dialog, container);

        EditText write_bot = (EditText) view.findViewById(R.id.dialog_write_bot);
        TextView bot = (TextView) view.findViewById(R.id.dialog_text_bot);
        bot.setVisibility(View.INVISIBLE);
        write_bot.setVisibility(View.INVISIBLE);

        TextView top = (TextView) view.findViewById(R.id.dialog_text_top);
        TextView mid = (TextView) view.findViewById(R.id.dialog_text_mid);

        final EditText write_top = (EditText) view.findViewById(R.id.dialog_write_top);
        final EditText write_mid = (EditText) view.findViewById(R.id.dialog_write_mid);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        top.setText(mid_text);
        mid.setText("Old Password: ");

        TextView btn = (TextView) view.findViewById(R.id.finishOption);
        //set btn text
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.e(TAG, "button listener");
                data = new String [2];

                if(!write_mid.getText().toString().isEmpty())
                {
                    data[0] = write_mid.getText().toString();
                }else{data[0]="";}

                if(!write_top.getText().toString().isEmpty())
                {
                    data[1] = write_top.getText().toString();
                }else{data[1]="";}

                mListener.onDone(data);
                dismiss();
            }
        });
        return view;
    }
}
