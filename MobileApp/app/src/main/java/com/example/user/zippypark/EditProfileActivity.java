package com.example.user.zippypark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;

/**
 * written by: Samantha Moy
 */

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button confirmEditProfileButton;
    Button cancelEditProfileButton;
    DatePicker picker;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText passwordEditText;
    EditText licenseNumEditText;
    EditText registrationNumEditText;
    EditText creditCardTypeEditText;
    EditText creditCardNumEditText;
    EditText cvvEditText;
    TextView points;
    static Connection conn;

    int barcode;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode"); //customer barcode id

        //Handicap Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.handicapSpinner);
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


        //FIELDS TO UPDATE INFO
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        licenseNumEditText = findViewById(R.id.licenseNumEditText);
        registrationNumEditText = findViewById(R.id.registrationNumEditText);
        creditCardTypeEditText = findViewById(R.id.creditCardTypeEditText);
        creditCardNumEditText = findViewById(R.id.creditCardNumEditText);
        //expDateEditText = findViewById(R.id.expDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        points = findViewById(R.id.points);

        //PREPOPULATE CUSTOMER INFO FIELDS
        //EXECUTE SQL STATEMENT
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stmt = MainActivity.conn.createStatement();
                    //query displays all
                    String query = "SELECT * FROM `CustomerInfo` WHERE `Barcode` = " + barcode;
                    //System.out.println(query);
                    ResultSet rs = stmt.executeQuery(query);

                    if(rs.next()) {
                        firstNameEditText.setText(rs.getString("FirstName"));
                        lastNameEditText.setText(rs.getString("LastName"));
                        emailEditText.setText(rs.getString("Email"));
                        phoneEditText.setText(rs.getString("PhoneNum"));
                        passwordEditText.setText(rs.getString("Password"));
                        licenseNumEditText.setText(rs.getString("LicenseNum"));
                        registrationNumEditText.setText(rs.getString("RegistrationNum"));
                        creditCardTypeEditText.setText(rs.getString("CreditCardType"));
                        creditCardNumEditText.setText(rs.getString("CreditCardNum"));
                        cvvEditText.setText(rs.getString("CVV"));
                        points.setText(rs.getString("Points"));


                        //handicap pre pop
                        if(rs.getInt("Handicapped")==1){
                            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
                            int spinnerPosition = myAdap.getPosition("Yes");
                            spinner.setSelection(spinnerPosition);
                        }else{
                            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
                            int spinnerPosition = myAdap.getPosition("No");
                            spinner.setSelection(spinnerPosition);
                        }

                        //ExpDate pre pop
                        String expDateDB = rs.getString("ExpDate");
                        String[] arrDate = expDateDB.split("-");
                        int yearInt = Integer.parseInt(arrDate[0]);
                        //System.out.println("YEARINT: " + yearInt);
                        int monthInt = Integer.parseInt(arrDate[1])-1;
                        //System.out.println("MONTHINT: " + monthInt);
                        int dayInt = Integer.parseInt(arrDate[2]);
                        //System.out.println("DAYINT: " + dayInt);
                        picker.updateDate(yearInt,monthInt,dayInt);


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });




//CONFIRM BUTTON
        confirmEditProfileButton = findViewById(R.id.confirm);
        confirmEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                //final int phone = Integer.parseInt(phoneEditText.getText().toString());
                final String phone = phoneEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String licenseNum = licenseNumEditText.getText().toString();
                final String registrationNum = registrationNumEditText.getText().toString();
                final String creditCardType = creditCardTypeEditText.getText().toString();
                final String creditCardNum = creditCardNumEditText.getText().toString();
                //final String expDate = expDateEditText.getText().toString();
                final String cvv = cvvEditText.getText().toString();
                final String handicapAns = spinner.getSelectedItem().toString();
                final String pointVal = points.getText().toString();

                //formatting handicap response: convert to binary
                int num = 0;
                if (handicapAns.equals("Yes")) {
                    num = 1;
                }
                final int handicapAnsNum = num;

                //formatting expDate response: convert to YYYY-MM-DD
                final String year = Integer.toString(picker.getYear()); //year
                //System.out.println("UPDATED YEAR: " + year);

                final String tempMonth; //month
                int num2 = picker.getMonth();
                if(num2<10){
                    tempMonth = "0" + (picker.getMonth()+1);
                }else {
                    tempMonth = Integer.toString(picker.getMonth()+1);
                }
                final String month = tempMonth;
                //System.out.println("UPDATED MONTH: " + month);


                final String tempDay; //day
                int num3 = picker.getDayOfMonth();
                //System.out.println("ORIGINAL MONTH: " + num3);
                if(num3<10){
                    tempDay = "0" + picker.getDayOfMonth();
                }else {
                    tempDay = Integer.toString(picker.getDayOfMonth());
                }
                final String day = tempDay;
                //System.out.println("UPDATED DAY: " + day);


                final String expDate = year + "-" + month + "-" + day; //combining format
                //System.out.println("UPDATED FULL DATE: " + expDate);



                //EXECUTE SQL STATEMENT
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Statement stmt = MainActivity.conn.createStatement();
                            //query updates all but points
                            String query = "UPDATE `CustomerInfo` SET `FirstName`='" + firstName +
                                    "', `LastName`='" + lastName + "', `Email`='" + email +
                                    "', `PhoneNum`='" + phone + "', `Password`='" + password +
                                    "', `LicenseNum`='" + licenseNum + "', `RegistrationNum`='" +
                                    registrationNum + "', `CreditCardType`='" + creditCardType +
                                    "', `CreditCardNum`='" + creditCardNum + "', `ExpDate`='" +
                                    expDate + "', `CVV`='" + cvv + "', `Handicapped`=" +
                                    handicapAnsNum + " WHERE `Barcode` = " + barcode;
                            //System.out.println(query);
                            stmt.executeUpdate(query);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //MOVE TO PROFILE
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                i.putExtras(b);
                //Confirmation
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Account successfully updated.", Toast.LENGTH_LONG);
                toast.show();
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
