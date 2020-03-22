package com.example.user.zippypark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 3/19/2020.
 */

public class ProfileActivity extends AppCompatActivity {
    Button editProfileButton;
    Button homeProfileButton;
    Button profileButton;
    int barcode;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        final Bundle b = getIntent().getExtras(); //barcode??
        //SELECT from db to display profile data





        //TRANSITION TO EDIT PROFILE SCREEN
        editProfileButton = findViewById(R.id.edit);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,
                        EditProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        //TRANSITION TO HOME SCREEN
        homeProfileButton = findViewById(R.id.homeButton);
        homeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }
}