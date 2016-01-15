package palarax.e_key_card.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        Log.e(TAG, "CARD str: " + feedItemList.get(0).getType());
        //view.setVisibility(View.INVISIBLE);
        if(feedItemList.get(0).getID().equals("-1"))
        {
            view.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.VISIBLE);
        }

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
