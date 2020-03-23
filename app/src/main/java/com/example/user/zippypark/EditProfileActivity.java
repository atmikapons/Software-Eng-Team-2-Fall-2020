package com.example.user.zippypark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

/**
 * Created by user on 3/19/2020.
 */

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button confirmEditProfileButton;
    Button cancelEditProfileButton;
    DatePicker picker;
    Button profileButton;
    int barcode;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        final Bundle b = getIntent().getExtras(); //barcode??

        //Handicap Spinner
        Spinner spinner = (Spinner) findViewById(R.id.handicapSpinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.handicap_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //EXPDATE SPINNER SETUP
        picker=(DatePicker)findViewById(R.id.expDateEditText);


        //pull data from DB like in Profile
        //to prepopulate .setText() for EditText fields kinda like SignUp





        //TRANSITION TO PROFILE SCREEN (confirm)
        confirmEditProfileButton = findViewById(R.id.confirm);
        confirmEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfileActivity.this,
                        ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }

        });

        //TRANSITION TO PROFILE SCREEN (cancel)
        cancelEditProfileButton = findViewById(R.id.cancel);
        cancelEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfileActivity.this,
                        ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }

        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}