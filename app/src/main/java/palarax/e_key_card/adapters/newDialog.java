package palarax.e_key_card.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class newDialog extends DialogFragment {

    private String message;
    private String btnText;
    private Boolean canceled = false;

    public newDialog() {

    }

    public void getMsg(String buttonT, String msg)
    {
        this.message = msg;
        this.btnText = buttonT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container);
        TextView name = (TextView) view.findViewById(R.id.write_step);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        name.setText(message);
        TextView btn = (TextView) view.findViewById(R.id.writefunctionBTN);
        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                canceled=true;
                getDialog().cancel();
            }
        });
        return view;
    }

    public void setBtnResult(Boolean result)
    {
        canceled = result;
    }

    public boolean getBtnResult()
    {
        return canceled;
    }
}