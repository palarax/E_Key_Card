package palarax.e_key_card.adapters;

import android.content.Context;
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


    private List<CardObject> feedItemList;  //Cards are created here
    //private Context mContext;               //Context

    public RecyclerAdapter_Scroller(List<CardObject> feedItemList) {
        this.feedItemList = feedItemList;
    }

    @Override
    public FeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nfc_write_fragment, parent, false);
        FeedCardHolder dataObjectHolder = new FeedCardHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(FeedCardHolder holder, int position) {
        holder.ID.setText(feedItemList.get(position).getID());
        holder.tech.setText(feedItemList.get(position).getTech());
        holder.type.setText(feedItemList.get(position).getType());
    }

    public void addItem(CardObject dataObj, int index) {
        feedItemList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        feedItemList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }
}
