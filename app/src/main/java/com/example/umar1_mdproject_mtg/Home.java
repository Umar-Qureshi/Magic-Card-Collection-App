package com.example.umar1_mdproject_mtg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    CardDB_Helper mySQL_CardDB_Helper;
    static final int REQUEST_MAGIC_CARD = 42;
    private static final int CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connecting to the database
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);

        //below line to clears the previous rows each time app runs (it just removes every row from the table)
        //mySQL_CardDB_Helper.DeleteCardRows();

        //Manually Add Rows to the database
        //mySQL_CardDB_Helper.addCard("x11111", "vampire","set1", "red", "1", "$10", "img1");
        //mySQL_CardDB_Helper.addCard("y22222", "beast", "set2", "teal", "2", "$22", "img2");


        //call the function to display to screen
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerFragment fragment = new RecyclerFragment();
            transaction.replace(R.id.rView, fragment);
            transaction.commit();
        }

        ImageButton btnCam = (ImageButton) findViewById(R.id.btnCam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"launching Cam", Toast.LENGTH_LONG);
                launchCam(v);
            }
        });
    }

    public void launchCam(View view){

        if(ContextCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            try {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(getBaseContext(), "Opening Camera", Toast.LENGTH_SHORT);
        Intent captureIntent = new Intent(this, CardRecognition.class);
        startActivityForResult(captureIntent,REQUEST_MAGIC_CARD);
    }

    public void MTG_Search_View(View view) {
        Intent openSearch = new Intent(getApplicationContext(), MTG_Search.class);
        startActivity(openSearch);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}