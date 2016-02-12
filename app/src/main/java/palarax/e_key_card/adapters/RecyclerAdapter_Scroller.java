package palarax.e_key_card.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import palarax.e_key_card.R;
import palarax.e_key_card.adapters.helper.ItemTouchHelperAdapter;
import palarax.e_key_card.adapters.helper.OnStartDragListener;

/**
 * @author Ilya Thai
 */
public class RecyclerAdapter_Scroller extends RecyclerView.Adapter<FeedCardHolder> implements ItemTouchHelperAdapter {

    public static final String TAG = "View Adapter";
    private List<CardObject> feedItemList ;  //Cards are created here
    private View view;
    private final OnStartDragListener mDragStartListener;
    private Context mContext;


    public RecyclerAdapter_Scroller(List<CardObject> feedItemList,OnStartDragListener dragStartListener,Context context) {
            mContext=context;
            mDragStartListener = dragStartListener;
            this.feedItemList = feedItemList;
    }



    @Override
    public FeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new FeedCardHolder(view);
    }


    @Override
    public void onBindViewHolder(final FeedCardHolder holder, int position) {
        holder.ID.setText(feedItemList.get(position).getID());
        holder.tech.setText(feedItemList.get(position).getTech());
        holder.type.setText(feedItemList.get(position).getType());
        holder.message.setText(feedItemList.get(position).getMsg());
        holder.size.setText(feedItemList.get(position).getSize());
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    public void createDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * Checks if the card is already in the system
     * @param ID ID of the card
     * @return true if card exists
     */
    public int exists(String ID)
    {
        for(int position=0;position<getItemCount();position++)
        {
            if(feedItemList.get(position).getID().equals(ID)){
                return position;
            }
        }
        return -2;
    }

    public void updateCard(int position,String ID, String msg, String tech, String type, String size)
    {
        feedItemList.get(position).setID(ID);
        feedItemList.get(position).setMsg(msg);
        feedItemList.get(position).setTech(tech);
        feedItemList.get(position).setType(type);
        feedItemList.get(position).setSize(size);
    }

    public void addItem(CardObject dataObj, int index) {
        feedItemList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        if(!feedItemList.isEmpty()){
            feedItemList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearAll()
    {
        feedItemList.clear();
    }

    @Override
    public int getItemCount() {
        if(feedItemList==null){
            return 0;
        }else{
            return feedItemList.size();
        }
    }

    @Override
    public void onItemDismiss(int position) {
        feedItemList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(feedItemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


}
