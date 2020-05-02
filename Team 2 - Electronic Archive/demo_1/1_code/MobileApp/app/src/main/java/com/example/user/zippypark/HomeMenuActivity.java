package com.example.user.zippypark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcraft.jsch.*;

import java.sql.*;


public class HomeMenuActivity extends AppCompatActivity {
    Button currentReservationsButton;
    Button profileButton;
    Button createResButton;
    Button resHistoryButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu);

        final Bundle b = getIntent().getExtras();


        currentReservationsButton = findViewById(R.id.currentReservations);
        currentReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeMenuActivity.this,
                        CurrentReservationsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        profileButton = findViewById(R.id.viewProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeMenuActivity.this,
                        ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        createResButton = findViewById(R.id.createReservation);
        createResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeMenuActivity.this,
                        CreateReservationActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        resHistoryButton = findViewById(R.id.reservationHistory);
        resHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeMenuActivity.this,
                        ReservationHistoryActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}



