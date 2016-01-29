package palarax.e_key_card.CardReader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class writeOptionDialog extends DialogFragment {


    private String mid_text;

    public writeOptionDialog() {

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

        EditText write_top = (EditText) view.findViewById(R.id.dialog_write_top);
        EditText write_mid = (EditText) view.findViewById(R.id.dialog_write_mid);
        EditText write_bot = (EditText) view.findViewById(R.id.dialog_write_bot);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //name.setText(message);
        if(mid_text.equals("URL:") || mid_text.equals("MOBILE:") || mid_text.equals("TEXT:") )
        {
            top.setVisibility(View.INVISIBLE);
            bot.setVisibility(View.INVISIBLE);
            write_top.setVisibility(View.INVISIBLE);
            write_bot.setVisibility(View.INVISIBLE);
            mid.setText(mid_text);
            if(mid_text.equals("NUMBER:")){write_mid.setInputType(InputType.TYPE_CLASS_PHONE);}
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
                getDialog().cancel();
            }
        });
        return view;
    }


}
