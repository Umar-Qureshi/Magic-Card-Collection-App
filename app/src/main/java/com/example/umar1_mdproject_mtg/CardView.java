package com.example.umar1_mdproject_mtg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CardView extends AppCompatActivity {

    private final static String TAG ="CardView";
    //beginning of url
    public String CardView_apiURL = "https://api.scryfall.com/cards/";
    String query2 = null;
    Boolean inCollection = false;

    //Variables to save into user database if desired
    public String card_pk;
    public String card_name;
    public String card_set;
    public String card_color;
    public String card_qty;
    public String card_price;
    public String card_imgURL;

    public String uID;

    CardDB_Helper mySQL_CardDB_Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview);

        //simply displaying the cardID retrieved from previous listview, Not needed remove the setTexts in the future
        Intent intent = getIntent();
        String CardID = intent.getStringExtra("CardID");
        uID = intent.getStringExtra("UserID");
        String qty;
        FloatingActionButton addBtn = findViewById(R.id.btnAdd);
        FloatingActionButton deleteBtn = findViewById(R.id.btnDelete);
        if (uID != null) {
            if (!uID.equals("N/A")) {
                inCollection = true;
                qty = Integer.toString(intent.getIntExtra("Qty", 0));
            } else {
                qty = "0";
            }
        }else { qty = "0"; }

        if(inCollection){
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(v -> deleteCard());
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }

        addBtn.setOnClickListener(v -> {
            if (inCollection) {
                editCard();
            } else {
                addCard();
            }
        });

        EditText QTY = (EditText) findViewById(R.id.txtQTY);
        QTY.setText(qty);

        //Url that give us a Json file of all data from that specific card where we will extract information from
        //Example URL:  https://api.scryfall.com/cards/e9d5aee0-5963-41db-a22b-cfea40a967a3  basically this: https://api.scryfall.com/cards/[CARD+ID]
        try {
            query2 = CardView_apiURL + URLEncoder.encode(CardID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //execute the url query
        CardView.JSONTask myTask2 = new CardView.JSONTask();
        myTask2.execute(query2);
    }

    public void addCard() {
        //Connecting to the database
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);

        //obtaining qty from textview
        EditText QTY = (EditText) findViewById(R.id.txtQTY);
        card_qty = QTY.getText().toString();

        //Adding the card to the database from the variables gained from the query
        mySQL_CardDB_Helper.addCard(card_pk, card_name, card_set, card_color, card_qty, card_price, card_imgURL);
    }

    public void editCard(){
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);
        EditText QTY = (EditText) findViewById(R.id.txtQTY);
        String newQty = QTY.getText().toString();

        mySQL_CardDB_Helper.editQty(Integer.parseInt(uID), Integer.parseInt(newQty));
    }

    public void deleteCard(){
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);
        mySQL_CardDB_Helper.removeEntry(Integer.parseInt(uID));
        finish();
    }

    class JSONTask extends AsyncTask<String, String, String> {
        HttpURLConnection conn;

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Setup connection to use the URL over the internet
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream stream = conn.getInputStream();
                    //Reading and storing the JSON
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    //return the Json file read to the onPostExecute Execute function
                    return buffer.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //If empty (which is really unlikely to happen for this page)
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Empty View, go back and try again", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try {
                //Save the JSON that was read and returned into a JSON Object to easily use and extract info
                JSONObject jsonObject = new JSONObject(result);

                //find the view to print in
                TextView currentView = findViewById(R.id.lbl_cardInfo);
                //Get values from the Json Object and store them in the variables
                card_pk = jsonObject.getString("id");
                card_name = jsonObject.getString("name");
                card_set = jsonObject.getString("set_name");
                card_color = jsonObject.getJSONArray("color_identity").optString(0, "Colorless");
                card_price = String.valueOf(jsonObject.getJSONObject("prices").getDouble("usd"));
                card_imgURL = jsonObject.getJSONObject("image_uris").getString("normal");

                //Display the cards info in the textview
                currentView.append("Card Name: " + card_name);
                currentView.append("\nSet Name: " + card_set);
                currentView.append("\nColor: " + card_color);
                currentView.append("\nUSD Price: $" + card_price);

                //Call function to load the cards picture into the image view
                new DisplayURL((ImageView) findViewById(R.id.imageView)).execute(card_imgURL);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DisplayURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DisplayURL(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
