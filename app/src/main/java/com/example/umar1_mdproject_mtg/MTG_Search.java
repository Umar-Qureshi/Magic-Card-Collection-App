package com.example.umar1_mdproject_mtg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.List;

public class MTG_Search extends AppCompatActivity {

    //Example url api call for a search of all cards with the word "vampire"
    //https://api.scryfall.com/cards/search?q=vampire&unique=cards&as=full&order=name
    //Same call but now the word has been replaced with [CARD+NAME] indicating that is where the word(s) the user has searched should be placed
    //https://api.scryfall.com/cards/search?q=[CARD+NAME]&unique=cards&as=full&order=name

    //beginning of url
    public String BroadCardSearch_apiURL = "https://api.scryfall.com/cards/search?q=";
    //end of url
    public String BCS_endof_apiURL = "&unique=cards&as=full&order=name";
    //Variable that stores what the user searches
    public String Search;
    String query = null;
    //List variable that stores a list of Card Ids after the search has been executed
    List<String> CardIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mtg_search);

        ListView ListViewMTGG = (ListView) findViewById(R.id.ListViewMTG);
        //When an entry in the listview is tapped the following function is executed
        ListViewMTGG.setOnItemClickListener((adapterView, view, i, l) -> {

            //Open Cardview page and send the card Id which will be queried to get all the information on it
            Intent intent = new Intent(getBaseContext(), CardView.class);
            intent.putExtra("CardID", CardIds.get(i));
            intent.putExtra("UserID", "N/A");
            startActivity(intent);
        });
    }

    public void MTG_Search(View view) {

        //User input for the card search
        EditText txtSearch = (findViewById(R.id.txtSearch));
        Search = txtSearch.getText().toString();

        try {
            //encode the users search into the middle of the url and then the query variable will be used to fetch the card(s)
            query = BroadCardSearch_apiURL + URLEncoder.encode(Search, "UTF-8") + BCS_endof_apiURL;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //execute the url query and save the JSON that it outputs into "myTask"
        JSONTask myTask = new JSONTask();
        myTask.execute(query);
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
            //If Search was incorrect (so no Json was read or returned)
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please check the search entry and try again", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try {
                //Save the JSON that was read and returned into a JSON Object to easily use and extract info
                JSONObject jsonObject = new JSONObject(result);
                JSONArray CardArray;

                if (jsonObject.isNull("data")) {
                    return;
                }

                //An array for every "row" or card entry
                CardArray = jsonObject.getJSONArray("data");

                ListView listView = (ListView) findViewById(R.id.ListViewMTG);
                //Clearing Listview and CardIds each time before new values are stored inside of it
                listView.setAdapter(null);
                CardIds.clear();
                //CardIds list that stores a list of Card Ids after the search has been executed

                //An Arraylist which will be used to populate each row of the ListView
                ArrayList<String> lstData = new ArrayList<String>();

                //Go through each "row" or card entry found in the JSON
                for (int i = 0; i < CardArray.length(); i++) {

                    JSONObject obj = CardArray.getJSONObject(i);

                    //Storing the card name and Id
                    lstData.add("Card Name: " + obj.getString("name"));
                    CardIds.add(obj.getString("id"));
                }
                //Draw/place each card name in a separate entry for each row in the listview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, lstData);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
