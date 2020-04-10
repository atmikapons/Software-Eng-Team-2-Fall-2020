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


public class CreateReservationActivity extends AppCompatActivity {

    EditText resDateEditText;
    EditText startTimeEditText;
    EditText endTimeEditText;
    Button createButton;
    int barcode;
    int multiplier;
    int base;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reservation);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode");

        resDateEditText = findViewById(R.id.reservationDateEditText);
        startTimeEditText = findViewById(R.id.startTime);
        endTimeEditText = findViewById(R.id.endTime);


        createButton = findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date1 = resDateEditText.getText().toString();
                final String start1 = startTimeEditText.getText().toString();
                String end1 = endTimeEditText.getText().toString();

                final Date date = Date.valueOf(date1);
                final Time start = Time.valueOf(start1);
                final Time end = Time.valueOf(end1);


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Statement stmt1 = MainActivity.conn.createStatement();
                            ResultSet rs = stmt1.executeQuery("SELECT * FROM `Payment` WHERE `StartTime`=\"" + start1 + "\"");
                            if(rs.next()){
                                multiplier = rs.getInt("Multiplier");
                                System.out.println(multiplier);
                                base = rs.getInt("BasePrice");
                                System.out.println(base);
                            }

                            long diff = end.getTime() - start.getTime(); //length of the reservation
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff); //conversion to minutes
                            long charge1 = base * (minutes/15) * multiplier; //price formula
                            final double charge = (double) charge1;

                            Statement stmt = MainActivity.conn.createStatement();
                            String query =("INSERT INTO `Reservations`" +
                                    "(`Date`, `StartTime`, `EndTime`, `Barcode`, `Charge`)" +
                                    "VALUES ('" + date + "', '" + start + "', '" + end + "', '" +
                                    barcode + "', '" + charge + "')");
                            //System.out.println(query);
                            stmt.executeUpdate(query);
                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent i = new Intent(CreateReservationActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(b);

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Reservation Created, Payment made", duration);
                toast.show();

                startActivity(i);
            }
        });

    }
}
