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
 * written by: Samantha Moy
 */

public class DeleteProfileActivity extends AppCompatActivity {
    Button confirmDelete;
    Button cancelDelete;
    int barcode;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_profile_confirm);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode"); //customer barcode id



        //TRANSITION TO LOGIN SCREEN (confirm delete)
        confirmDelete = findViewById(R.id.confirmDelete);
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXECUTE SQL STATEMENT
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Statement stmt = MainActivity.conn.createStatement();
                            //query updates all but points
                            String query = "DELETE FROM `CustomerInfo` WHERE `Barcode`=" + barcode;
                            System.out.println(query);
                            stmt.executeUpdate(query);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent i = new Intent(DeleteProfileActivity.this,
                        MainActivity.class);
                i.putExtras(b);
                //Confirmation
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Account successfully deleted.", Toast.LENGTH_LONG);
                toast.show();
                startActivity(i);
            }
        });




        //TRANSITION TO PROFILE SCREEN (cancel delete)
        cancelDelete = findViewById(R.id.cancelDelete);
        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeleteProfileActivity.this,
                        ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });




    }

}
