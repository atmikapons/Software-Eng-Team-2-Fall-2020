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

import com.jcraft.jsch.*;

import java.sql.*;

public class EditReservationActivity extends AppCompatActivity {

    EditText resDateEditText;
    EditText startTimeEditText;
    EditText endTimeEditText;
    Button editResButton;
    Button deleteResButton;
    int barcode;
    int resID;

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

                String date1 = resDateEditText.getText().toString();
                String start1 = startTimeEditText.getText().toString();
                String end1 = endTimeEditText.getText().toString();

                final Date date = Date.valueOf(date1);
                final Time start = Time.valueOf(start1);
                final Time end = Time.valueOf(end1);
                final double charge = 160;

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
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
                        HomeMenuActivity.class);
                i.putExtras(bundle);

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Reservation Updated", duration);
                toast.show();

                startActivity(i);
            }

        });

        deleteResButton = findViewById(R.id.delete);
        deleteResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Statement stmt = MainActivity.conn.createStatement();
                            String query = "DELETE FROM `Reservations` WHERE `rID`=\"" + resID + "\"";

                            System.out.println(resID);
                            stmt.executeUpdate(query);

                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent i = new Intent(EditReservationActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}