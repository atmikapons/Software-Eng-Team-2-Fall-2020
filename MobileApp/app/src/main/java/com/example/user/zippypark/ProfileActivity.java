package com.example.user.zippypark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by user on 3/19/2020.
 */

public class ProfileActivity extends AppCompatActivity {
    Button editProfileButton;
    Button homeProfileButton;
    Button deleteProfileButton;

    //customer's info pulled from DB
    int barcode;
    String firstDB;
    String lastDB;
    String emailDB;
    String phoneDB;
    String passwordDB;
    int handicapDB;
    String licenseDB;
    String registrationDB;
    String creditCardTypeDB;
    String creditCardNumDB;
    String expDateDB;
    String cvvDB;
    int pointsDB;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode"); //customer barcode id

        // SELECT from db to display profile data
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stmt = MainActivity.conn.createStatement();
                    String query = "SELECT `FirstName`, `LastName`, `Email`, " +
                            "`PhoneNum`, `Password`, `Barcode`, `LicenseNum`, `RegistrationNum`, " +
                            "`CreditCardType`, `CreditCardNum`, `ExpDate`, `CVV`, `Points`, " +
                            "`Handicapped` FROM `CustomerInfo` WHERE `Barcode`=" + barcode;
                    //System.out.println("QUERY IS: " + query);
                    ResultSet rs = stmt.executeQuery(query);

                    //Display customer's info
                    if(rs.next()) {
                        firstDB = rs.getString("FirstName");
                        TextView firstText = (TextView) findViewById(R.id.firstNameEditText);
                        firstText.setText(firstDB);

                        lastDB = rs.getString("LastName");
                        TextView lastText = (TextView) findViewById(R.id.lastNameEditText);
                        lastText.setText(lastDB);

                        emailDB = rs.getString("Email");
                        TextView emailText = (TextView) findViewById(R.id.emailEditText);
                        emailText.setText(emailDB);

                        phoneDB = rs.getString("PhoneNum");
                        TextView phoneText = (TextView) findViewById(R.id.phoneEditText);
                        phoneText.setText(phoneDB);

                        passwordDB = rs.getString("Password");
                        TextView passwordText = (TextView) findViewById(R.id.passwordEditText);
                        passwordText.setText(passwordDB);

                        handicapDB = rs.getInt("Handicapped");
                        TextView handicapText = (TextView) findViewById(R.id.handicapEditText);
                        if(handicapDB == 1){
                            String temp = "Yes";
                            handicapText.setText(temp);
                        }else{
                            String temp = "No";
                            handicapText.setText(temp);
                        }

                        licenseDB = rs.getString("LicenseNum");
                        TextView licenseText = (TextView) findViewById(R.id.licenseNumEditText);
                        licenseText.setText(licenseDB);

                        registrationDB = rs.getString("RegistrationNum");
                        TextView registrationText = (TextView) findViewById(R.id.registrationNumEditText);
                        registrationText.setText(registrationDB);

                        creditCardTypeDB = rs.getString("CreditCardType");
                        TextView creditCardTypeText = (TextView) findViewById(R.id.creditCardTypeEditText);
                        creditCardTypeText.setText(creditCardTypeDB);

                        creditCardNumDB = rs.getString("CreditCardNum");
                        TextView creditCardNumText = (TextView) findViewById(R.id.creditCardNumEditText);
                        creditCardNumText.setText(creditCardNumDB);

                        expDateDB = rs.getString("ExpDate");
                        TextView expDateText = (TextView) findViewById(R.id.expDateEditText);
                        expDateText.setText(expDateDB);

                        cvvDB = rs.getString("CVV");
                        TextView cvvText = (TextView) findViewById(R.id.cvvEditText);
                        cvvText.setText(cvvDB);

                        pointsDB = rs.getInt("Points");
                        TextView pointsText = (TextView) findViewById(R.id.points);
                        pointsText.setText(String.valueOf(pointsDB));

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });




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

        //DELETE PROFILE
        deleteProfileButton = findViewById(R.id.deleteProfButton);
        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,
                        DeleteProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }
}