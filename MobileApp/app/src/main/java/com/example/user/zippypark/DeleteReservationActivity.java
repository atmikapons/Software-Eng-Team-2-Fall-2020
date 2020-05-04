package com.example.user.zippypark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * written by: Shreya Patel
 */

public class DeleteReservationActivity extends AppCompatActivity {

    Button confirmReservationDelete;
    Button cancelReservationDelete;
    int barcode;
    int resID;
    int points;
    int varpoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_reservation_confirm);

        final Bundle bundle = getIntent().getExtras();
        barcode = bundle.getInt("barcode");
        resID = bundle.getInt("resID");

        confirmReservationDelete = findViewById(R.id.confirmResDelete);
        confirmReservationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String action = "Cancels";

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Statement statement = MainActivity.conn.createStatement(); //pull points from CustomerInfo
                            String query1 = "SELECT Points FROM `CustomerInfo` WHERE `Barcode`=" + barcode;
                            ResultSet rs1 = statement.executeQuery(query1);
                            if(rs1.next()){
                                points = rs1.getInt("Points");
                            }

                            Statement statement1 = MainActivity.conn.createStatement(); //get the subtraction of points
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

                            Statement stmt = MainActivity.conn.createStatement();
                            String query = "DELETE FROM `Reservations` WHERE `rID`=\"" + resID + "\"";
                            //System.out.println(resID);
                            stmt.executeUpdate(query);

                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent i = new Intent(DeleteReservationActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(bundle);

                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Reservation Deleted, Payment refunded", duration);
                toast.show();

                startActivity(i);
            }
        });

        cancelReservationDelete = findViewById(R.id.cancelResDelete);
        cancelReservationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeleteReservationActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(bundle);

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Reservation Deletion Canceled", duration);
                toast.show();
                startActivity(i);
            }
        });
    }
}
