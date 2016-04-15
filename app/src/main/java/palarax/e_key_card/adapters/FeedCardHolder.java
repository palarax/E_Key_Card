package palarax.e_key_card.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import palarax.e_key_card.R;
import palarax.e_key_card.adapters.helper.ItemTouchHelperViewHolder;
import palarax.e_key_card.entities.tags;

/**
 * @author Ilya Thai
 */
public class FeedCardHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    protected TextView ID;
    protected TextView tech;
    protected TextView type;
    protected TextView message;
    protected TextView size;
    protected TextView serialNumber;
    protected TextView balance;
    protected Button delete;

    protected TextView balance_label;
    protected TextView serialNumber_label;


    public FeedCardHolder(View view) {
        super(view);
        //view.setOnCreateContextMenuListener(this);
        this.ID = (TextView) view.findViewById(R.id.tagID_text);
        this.tech = (TextView) view.findViewById(R.id.techList_text);
        this.type = (TextView) view.findViewById(R.id.tagType_text);
        this.message = (TextView) view.findViewById(R.id.tagMessage_text);
        this.size = (TextView) view.findViewById(R.id.tagSize_text);
        this.delete = (Button) view.findViewById(R.id.delete_card);
        this.serialNumber = (TextView) view.findViewById(R.id.opalNumber_text);
        this.balance = (TextView) view.findViewById(R.id.opalBalance_text);

        this.balance_label =(TextView) view.findViewById(R.id.opalBalance);
        this.serialNumber_label =(TextView) view.findViewById(R.id.opalNumber);
    }

    private void sync()
    {
        tags tag = new tags();
        tag.setTagName(ID.getText().toString());
        //tag.setLastLocation();
    }

    private void setUPLocation()
    {

    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}