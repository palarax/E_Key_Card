package palarax.e_key_card.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class RecyclerAdapter_Scroller extends RecyclerView.Adapter<FeedCardHolder> {

    public static final String TAG = "View Adapter";
    private List<CardObject> feedItemList;  //Cards are created here
    View view;

    public RecyclerAdapter_Scroller(List<CardObject> feedItemList) {
            this.feedItemList = feedItemList;
    }

    @Override
    public FeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new FeedCardHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedCardHolder holder, int position) {
        holder.ID.setText(feedItemList.get(position).getID());
        holder.tech.setText(feedItemList.get(position).getTech());
        holder.type.setText(feedItemList.get(position).getType());
        if(feedItemList.get(position).getID().equals("-1"))
        {
            view.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks if the card is already in the system
     * @param ID ID of the card
     * @return true if card exists
     */
    public boolean exists(String ID)
    {
        for(int position=0;position<getItemCount();position++)
        {
            if(feedItemList.get(position).getID().equals(ID)){
                return true;
            }
        }
        return false;
    }

    public void addItem(CardObject dataObj, int index) {
        if(feedItemList.get(0).getID().equals("1"))
        {
           deleteItem(0);
        }
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
}
