package palarax.e_key_card.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class FeedCardHolder extends RecyclerView.ViewHolder {
    protected TextView ID;
    protected TextView tech;
    protected TextView type;
    protected TextView message;
    protected TextView size;


    public FeedCardHolder(View view) {
        super(view);
        this.ID = (TextView) view.findViewById(R.id.tagID_text);
        this.tech = (TextView) view.findViewById(R.id.techList_text);
        this.type = (TextView) view.findViewById(R.id.tagType_text);
        this.message = (TextView) view.findViewById(R.id.tagMessage_text);
        this.size = (TextView) view.findViewById(R.id.tagSize_text);
    }
}