package com.example.umar1_mdproject_mtg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//This class is used for binding the data set to be displayed in our RecyclerView elements.
//Also handles the interactions taken with a click event for cards in our collection.
//onClick => bring up card information view

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final static String TAG ="CardAdapter";
    private static ArrayList<Card> dataset;
    private static Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView setName;
        private final TextView price;
        private final TextView qty;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(v -> {
                Log.d(TAG, "Element "+getAdapterPosition()+" clicked.");
                goToCardInfo(getAdapterPosition());
            });
            title = (TextView) view.findViewById(R.id.cardTitle);
            setName = (TextView) view.findViewById(R.id.cardSet);
            price = (TextView) view.findViewById(R.id.cardPrice);
            qty = (TextView) view.findViewById(R.id.cardQty);
        }
        public TextView getTitle(){
            return title;
        }
        public TextView getSetName(){
            return setName;
        }
        public TextView getPrice(){
            return price;
        }

        public TextView getQty() {
            return qty;
        }
    }

    //Standard constructor
    public CardAdapter(ArrayList<Card> data, Context con){
        this.context = con;
        this.dataset = data;
    }

    //Inflates our Card view element
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_inventory_view, viewGroup, false);
        return new ViewHolder(v);
    }

    //Populates our collection card view with it's data
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getTitle().setText(dataset.get(position).getCardTitle());
        viewHolder.getSetName().setText(dataset.get(position).getSetName());
        viewHolder.getPrice().setText(dataset.get(position).getCardPrice());
        viewHolder.getQty().setText(Integer.toString(dataset.get(position).getCardQty()));
    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }

    //Bringing along a context for the goToCardInfo() function to be able to use it's intent
    public static Context getMyContext(){
        return context;
    }

    public static void goToCardInfo(int uid){
        Intent intent = new Intent(getMyContext(), CardView.class);
        String cid = dataset.get(uid).getCardID();
        int qty = dataset.get(uid).getCardQty();
        intent.putExtra("CardID", cid);
        intent.putExtra("UserID",Integer.toString(uid));
        intent.putExtra("Qty", qty);
        getMyContext().startActivity(intent);
    }
}
