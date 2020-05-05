package com.example.user.zippypark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.*;

import java.sql.*;


/**
* authored, tested, and debugged by: Shreya Patel
*/


public class EditReservationActivity extends AppCompatActivity {

    EditText resDateEditText;
    EditText startTimeEditText;
    EditText endTimeEditText;
    Button editResButton;
    Button deleteResButton;
    int barcode;
    int resID;
    double multiplier;
    int base;
    String date1;
    String start2;
    String end1;
    double charge;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_reservation);

        final Bundle bundle = getIntent().getExtras();
        barcode = bundle.getInt("barcode");
        resID = bundle.getInt("resID");

        resDateEditText = findViewById(R.id.reservationDateEditText);
        startTimeEditText = findViewById(R.id.startTime);
        endTimeEditText = findViewById(R.id.EndTime);

        //prepopulate text fields

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stmt = MainActivity.conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT `Date`, `StartTime`, `EndTime` FROM `Reservations` " +
                            "WHERE `rID`=\"" + resID + "\"");

                    if(rs.next()) {
                        resDateEditText.setText(rs.getDate("Date").toString());
                        startTimeEditText.setText(rs.getTime("StartTime").toString());
                        endTimeEditText.setText(rs.getTime("EndTime").toString());
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //Update the reservation fields
        editResButton = findViewById(R.id.update);
        editResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date1 = resDateEditText.getText().toString();
                final String start1 = startTimeEditText.getText().toString();
                start2 = startTimeEditText.getText().toString();
                end1 = endTimeEditText.getText().toString();


                if(resDateEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    resDateEditText.setError("Field cannot be left blank");
                }

                else if(startTimeEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    startTimeEditText.setError("Field cannot be left blank");
                }

                else if(endTimeEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    endTimeEditText.setError("Field cannot be left blank");
                }

                else{
                    final Date date = Date.valueOf(date1);
                    final Time start = Time.valueOf(start2);
                    final Time end = Time.valueOf(end1);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Statement stmt1 = MainActivity.conn.createStatement();
                                ResultSet rs = stmt1.executeQuery("SELECT * FROM `Payment` WHERE `StartTime`=\"" + start1 + "\"");
                                if(rs.next()){
                                    multiplier = rs.getDouble("Multiplier");
                                    base = rs.getInt("BasePrice");
                                }

                                long diff = end.getTime() - start.getTime(); //length of the reservation
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); //conversion to minutes

                                if((minutes) % 15 == 0) {
                                    charge = base * ((minutes/15) * multiplier); //price formula
                                }
                                else{
                                    charge = base * ((minutes/15) + 1) * multiplier;
                                }

                                Statement stmt = MainActivity.conn.createStatement();
                                String query = "UPDATE `Reservations` SET `Date`='" + date +
                                        "', `StartTime`='" + start + "', `EndTime`='" + end +
                                        "', `Barcode`='" + barcode + "', `Charge`='" + charge +"' WHERE `rID`=\"" + resID + "\"";

                                stmt.executeUpdate(query);

                            } catch(SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Intent i = new Intent(EditReservationActivity.this,
                            CurrentReservationsActivity.class);
                    i.putExtras(bundle);

                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Reservation Updated", duration);
                    toast.show();

                    startActivity(i);
                }

            }

        });

        deleteResButton = findViewById(R.id.delete);
        deleteResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditReservationActivity.this,
                        DeleteReservationActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}