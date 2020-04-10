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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        reservations = new ArrayList<>(10);

        new GetReservationsTask().execute();

        listView = findViewById(R.id.listview);
        customAdapter = new CustomAdapter(reservations, getApplicationContext());
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                int resID = reservations.get(i).getResID();
                Intent intent = new Intent(CurrentReservationsActivity.this,
                        EditReservationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("resID", resID);
                bundle.putInt("barcode", barcode);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    public static void updateAdapter() {
        customAdapter.notifyDataSetChanged();
    }

    class GetReservationsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(Calendar.getInstance().getTime());

                Statement stmt = MainActivity.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Reservations WHERE " +
                        "Barcode=" + barcode + " AND Date >= " + today + " ORDER BY Date ASC");

                if(!rs.next()) Log.d("CurrentReservations", "no reservations for this user");

                while (rs.next()) {
                    Reservation r = new Reservation(rs.getDate("Date"),
                            rs.getTime("StartTime"),
                            rs.getTime("EndTime"),
                            rs.getInt("Barcode"),
                            rs.getInt("AssignedSpot"),
                            rs.getDouble("Charge"),
                            rs.getInt("rID"),
                            rs.getInt("VIP"));
                    reservations.add(r);
                    Log.d("CurrentRes", ""+reservations.contains(r));

                    String msg = "Date" + r.getDate() +
                            "StartTime" + r.getStartTime() +
                            "EndTime" + r.getEndTime() +
                            "Barcode" + r.getBarcode();

                    Log.d("CurrentReservationsInside", msg);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(reservations != null && !reservations.isEmpty()) {
                listView = findViewById(R.id.listview);
                customAdapter = new CustomAdapter(reservations, getApplicationContext());
                listView.setAdapter(customAdapter);
            }
        }
    }

}

class CustomAdapter extends ArrayAdapter<Reservation> implements View.OnClickListener {

    private List<Reservation> dataSet;

    private Context mContext;

    CustomAdapter(List<Reservation> reservations, Context context) {
        super(context, R.layout.res_list_item, reservations);
        dataSet = reservations;
        mContext = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView date;
        TextView startTime;
        TextView endTime;
        TextView charge;
        TextView vip;

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

        vip = v.findViewById(R.id.vip);
        vip.setText(p.getVip());

        charge = v.findViewById(R.id.charge);
        charge.setText("$" + p.getCharge() + "0");

        return v;
    }

    @Override
    public void onClick(View view) {
    }
}


