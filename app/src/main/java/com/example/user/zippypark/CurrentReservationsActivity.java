package com.example.user.zippypark;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 3/19/2020.
 */

public class CurrentReservationsActivity extends AppCompatActivity {

    ListView listView;
    int barcode;
    List<Reservation> reservations;
    static CustomAdapter customAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_reservation);

        Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode");
        //Date today = (java.sql.Date) Calendar.getInstance().getTime();

        AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                  try {
                      Statement stmt = MainActivity.conn.createStatement();
                      ResultSet rs = stmt.executeQuery("SELECT * FROM Reservations WHERE Barcode=" + barcode);
                      reservations = new ArrayList<>();
                      while (rs.next()) {
                          Reservation r = new Reservation(rs.getDate("Date"),
                                  rs.getTime("StartTime"),
                                  rs.getTime("EndTime"),
                                  rs.getInt("Barcode"),
                                  rs.getInt("AssignedSpot"),
                                  rs.getDouble("Charge"),
                                  rs.getInt("rID"));
                          reservations.add(r);
                      }
                  } catch (SQLException e) {
                      e.printStackTrace();
                  }
              }
          });



        listView = findViewById(R.id.listview);
        customAdapter = new CustomAdapter(reservations, getApplicationContext());
        listView.setAdapter(customAdapter);

    }

    public static void updateAdapter() {
        customAdapter.notifyDataSetChanged();
    }
}

class CustomAdapter extends ArrayAdapter<Reservation> implements View.OnClickListener {

    private List<Reservation> dataSet;
    private TextView date;
    private TextView startTime;
    private TextView endTime;
    private TextView charge;
    Context mContext;

    public CustomAdapter(List<Reservation> reservations, Context context) {
        super(context, R.layout.res_list_item, reservations);
        dataSet = reservations;
        mContext = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;
        if (v == null) v = LayoutInflater.from(mContext)
                .inflate(R.layout.res_list_item, viewGroup, false);
        Reservation p = dataSet.get(i);

        date = v.findViewById(R.id.reservationDate);
        date.setText(p.getDate().toString());

        startTime = v.findViewById(R.id.startTime);
        startTime.setText(p.getStartTime().toString());

        endTime = v.findViewById(R.id.endTime);
        endTime.setText(p.getEndTime().toString());

        charge = v.findViewById(R.id.charge);
        charge.setText("" + p.getCharge());

        return v;
    }

    @Override
    public void onClick(View view) {
    }

}

