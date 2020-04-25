package com.example.user.zippypark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.*;

import java.sql.*;


public class CreateReservationActivity extends AppCompatActivity {

    EditText resDateEditText;
    EditText startTimeEditText;
    EditText endTimeEditText;
    Button createButton;
    Button rewardButton;
    Button cancelButton;
    TextView pointsRequired;
    int barcode;
    double multiplier;
    int base;
    int vip;
    int numpoints;
    int points;
    int varpoints;
    String date1;
    String start2;
    String end1;
    int count;
    double charge;
    String hour;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reservation);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode");


        resDateEditText = findViewById(R.id.reservationDateEditText);
        startTimeEditText = findViewById(R.id.startTime);
        endTimeEditText = findViewById(R.id.endTime);

        new GetPointsTask().execute();

        rewardButton = findViewById(R.id.reward);
        rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vip = 1;

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Statement stmt = MainActivity.conn.createStatement();
                            String query = "UPDATE `CustomerInfo` SET `Points`='" + (numpoints - 100) + "\'";

                            stmt.executeUpdate(query);

                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


                Toast toast = Toast.makeText(getApplicationContext(),
                        "VIP Spot reward applied", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        createButton = findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date1 = resDateEditText.getText().toString();
                final String start1 = startTimeEditText.getText().toString();
                start2 = startTimeEditText.getText().toString();
                end1 = endTimeEditText.getText().toString();
                final String action = "CreateRes";

                int j = Integer.parseInt(start1.substring(0,2));
                if(j >= 10){
                    hour = Integer.toString(j) + ":00:00";
                }
                else{
                    hour = "0" + Integer.toString(j) + ":00:00";
                }


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
                                Statement statement = MainActivity.conn.createStatement(); //get points from CustomerInfo
                                String query1 = "SELECT `Points` FROM `CustomerInfo` WHERE `Barcode`=" + barcode;
                                ResultSet rs1 = statement.executeQuery(query1);
                                if(rs1.next()){
                                    points = rs1.getInt("Points");
                                }

                                Statement statement1 = MainActivity.conn.createStatement(); //get the addition of points
                                String query2 = "SELECT `Points` FROM `Points` WHERE `Action`=\"" + action + "\"";
                                ResultSet rs2 = statement1.executeQuery(query2);
                                if(rs2.next()){
                                    varpoints = rs2.getInt("Points");
                                }

                                int updatePoints = points + varpoints;

                                Statement statement2 = MainActivity.conn.createStatement(); //update points in CustomerInfo
                                String query3 = "UPDATE `CustomerInfo` SET `Points`='" + updatePoints + "' " +
                                        "WHERE `Barcode`=" + barcode;
                                statement2.executeUpdate(query3);

                                Statement stmt1 = MainActivity.conn.createStatement(); //calculate charge
                                    ResultSet rs = stmt1.executeQuery(
                                            "SELECT * FROM `Payment` WHERE `StartTime`=\"" + hour + "\"");
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
                                String query =("INSERT INTO `Reservations`" +
                                        "(`Date`, `StartTime`, `EndTime`, `Barcode`, `VIP`, `Charge`)" +
                                        "VALUES ('" + date + "', '" + start + "', '" + end + "', '" +
                                        barcode + "', '" + vip + "', '" + charge + "')");
                                stmt.executeUpdate(query);

                                Statement stmt2 = MainActivity.conn.createStatement();
                                ResultSet rs3 = stmt2.executeQuery("SELECT COUNT(`StartTime`)'" + count +
                                        "' FROM `Reservations` WHERE `StartTime`=\"" + start1 + "\" AND " +
                                        "`EndTime`=\"" + end1 + "\" AND `Date`=\"" + date1 + "\"");
                                if(rs3.next()){

                                    int count1 = count;

                                    if(count1 > 20){
                                        createButton.setEnabled(false);

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Reservations full", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }

                            } catch(SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    Intent i = new Intent(CreateReservationActivity.this,
                            CurrentReservationsActivity.class);
                    i.putExtras(b);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Reservation Created", Toast.LENGTH_LONG);

                    toast.show();

                    startActivity(i);
                }

            }
        });

        cancelButton = findViewById(R.id.res_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateReservationActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(b);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Reservation canceled", Toast.LENGTH_SHORT);
                toast.show();

                startActivity(i);
            }
        });

    }

    class GetPointsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Statement stmt1 = MainActivity.conn.createStatement();
                ResultSet rs = stmt1.executeQuery(
                        "SELECT `Points` FROM `CustomerInfo` WHERE `Barcode`=\"" + barcode + "\"");
                if (rs.next()) {
                    numpoints = rs.getInt("Points");
                    Log.d("numpoints", "numpoints inside: " + numpoints);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (numpoints < 100) {
                pointsRequired = findViewById(R.id.pointsrequired);
                int numPointsRequired = 100 - numpoints;
                Log.d("numpoints", "numpoints outside: " + numpoints);
                pointsRequired.setText(numPointsRequired + " more points until next reward");
                rewardButton.setEnabled(false);
                rewardButton.setBackgroundColor(getColor(R.color.grey));
            }
        }
    }
}
