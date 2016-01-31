package palarax.e_key_card.CardReader;

import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
public class writeOptionDialog extends DialogFragment{

    public static final String TAG = "dialog";
    private String mid_text;
    private String[] data;

    private dialogDoneListener mListener;

    public void setListener(dialogDoneListener listener) {
        mListener = listener;
    }

    public writeOptionDialog() {

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
        TextView top = (TextView) view.findViewById(R.id.dialog_text_top);
        TextView mid = (TextView) view.findViewById(R.id.dialog_text_mid);
        TextView bot = (TextView) view.findViewById(R.id.dialog_text_bot);

        final EditText write_top = (EditText) view.findViewById(R.id.dialog_write_top);
        final EditText write_mid = (EditText) view.findViewById(R.id.dialog_write_mid);
        final EditText write_bot = (EditText) view.findViewById(R.id.dialog_write_bot);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //name.setText(message);

        if(mid_text.equals("URL:") || mid_text.equals("MOBILE:") || mid_text.equals("TEXT:") )
        {
            top.setVisibility(View.INVISIBLE);
            bot.setVisibility(View.INVISIBLE);
            write_top.setVisibility(View.INVISIBLE);
            write_bot.setVisibility(View.INVISIBLE);
            mid.setText(mid_text);
            if(mid_text.equals("MOBILE:")){write_mid.setInputType(InputType.TYPE_CLASS_PHONE);}
        }else
        {
            top.setText("NAME:");
            mid.setText("MOBILE");
            bot.setText(mid_text);
            write_mid.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        TextView btn = (TextView) view.findViewById(R.id.finishOption);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Log.e(TAG,"button listener");
                data = new String [3];
                if(!write_bot.getText().toString().isEmpty())
                {
                    data[1] = write_bot.getText().toString();
                }else{data[1]="";}

                if(!write_mid.getText().toString().isEmpty())
                {
                    data[0] = write_mid.getText().toString();
                }else{data[0]="";}

                if(!write_top.getText().toString().isEmpty())
                {
                    data[2] = write_top.getText().toString();
                }else{data[2]="";}

                mListener.onDone(data);
                dismiss();
            }
        });
        return view;
    }


}
