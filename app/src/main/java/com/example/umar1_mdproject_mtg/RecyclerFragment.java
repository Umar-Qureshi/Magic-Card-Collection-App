package com.example.umar1_mdproject_mtg;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Defining our RecyclerView fragment to be put onto the Surface view of the main activity screen
public class RecyclerFragment extends Fragment {

    CardDB_Helper mySQL_CardDB_Helper;

    private static final String TAG = "RecyclerFragment";

    protected RecyclerView recyclerView;
    protected CardAdapter cardAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Card> dataset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataset= new ArrayList<>();
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,Bundle savedInstanceState){
        View rootView = layoutInflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);
        int scrollPosition = 0;
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);

        cardAdapter = new CardAdapter(dataset, getActivity());
        recyclerView.setAdapter(cardAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }
    private void initDataset(){
        mySQL_CardDB_Helper = new CardDB_Helper(getActivity(), null, null, 1);
        //Cursor goes through each row of the table and outputs it to the screen
        Cursor c = mySQL_CardDB_Helper.ViewData();

        if (c.getCount() == 0) {
            Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_LONG).show();
        } else {
            while (c.moveToNext()) {
                int uID = c.getInt(c.getColumnIndex("user_card_ID"));
                String cID = c.getString(c.getColumnIndex("card_PK"));
                String cTitle = c.getString(c.getColumnIndex("card_name"));
                String cSet = c.getString(c.getColumnIndex("card_set"));
                String cColor = c.getString(c.getColumnIndex("card_color"));
                int cQty = c.getInt(c.getColumnIndex("card_qty"));
                Float cPrice = c.getFloat(c.getColumnIndex("card_price"));
                String cImgLink = c.getString(c.getColumnIndex("card_img"));
                if(cTitle!=null) {
                    Card tmpCard = new Card(uID, cID, cTitle, cSet, cColor, cQty, cPrice, cImgLink);
                    Log.d(TAG, "Got Card Title: " + tmpCard.getCardTitle());
                    dataset.add(tmpCard);
                }
            }
        }
    }
}


