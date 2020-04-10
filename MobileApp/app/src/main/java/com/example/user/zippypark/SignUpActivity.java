package com.example.user.zippypark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jcraft.jsch.*;

import java.sql.*;


/**
 * Created by user on 3/19/2020.
 */

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button nxt;
    Button cancel;
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
    EditText expDateEditText;
    EditText cvvEditText;
    //Spinner handicapEditText;
    static Connection conn;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //HANDICAP SPINNER SETUP
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


        //SIGN UP INFO
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

        //NEXT BUTTON
        nxt = findViewById(R.id.nextButton);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(firstNameEditText.getText().toString())){
                    firstNameEditText.setError("Enter first name");
                } else if(TextUtils.isEmpty(lastNameEditText.getText().toString())){
                    lastNameEditText.setError("Enter last name");
                } else if(TextUtils.isEmpty(emailEditText.getText().toString())){
                    emailEditText.setError("Enter email");
                } else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
                    phoneEditText.setError("Enter phone number");
                } else if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                    passwordEditText.setError("Enter password");
                } else if(TextUtils.isEmpty(licenseNumEditText.getText().toString())){
                    licenseNumEditText.setError("Enter license plate number");
                } else if(TextUtils.isEmpty(registrationNumEditText.getText().toString())){
                    registrationNumEditText.setError("Enter vehicle registration number");
                } else if(TextUtils.isEmpty(creditCardNumEditText.getText().toString())){
                    creditCardNumEditText.setError("Enter credit card number");
                } else if(TextUtils.isEmpty(creditCardTypeEditText.getText().toString())){
                    creditCardTypeEditText.setError("Enter credit card type");
                } else if(TextUtils.isEmpty(cvvEditText.getText().toString())){
                    cvvEditText.setError("Enter CVV");
                } else {

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

                    //formatting handicap response: convert to binary
                    int num = 0;
                    if (handicapAns.equals("Yes")) {
                        num = 1;
                    }
                    final int handicapAnsNum = num;

                    //formatting expDate response: convert to YYYY-MM-DD
                    final String year = Integer.toString(picker.getYear()); //year

                    final String tempMonth; //month
                    int num2 = picker.getMonth();
                    if (num2 < 10) {
                        tempMonth = "0" + picker.getMonth();
                    } else {
                        tempMonth = Integer.toString(picker.getMonth());
                    }
                    final String month = tempMonth;

                    final String tempDay; //day
                    int num3 = picker.getDayOfMonth();
                    if (num3 < 10) {
                        tempDay = "0" + picker.getDayOfMonth();
                    } else {
                        tempDay = Integer.toString(picker.getMonth());
                    }
                    final String day = tempDay;

                    final String expDate = year + "-" + month + "-" + day; //combining format

                    //EXECUTE SQL STATEMENT
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Statement stmt = MainActivity.conn.createStatement();
                                //query has no barcode (autoincrement) and user starts with 10 points
                                String query = "INSERT INTO `CustomerInfo`" +
                                        "(`FirstName`, `LastName`, `Email`, `PhoneNum`, `Password`, " +
                                        "`LicenseNum`, `RegistrationNum`, `CreditCardType`, " +
                                        "`CreditCardNum`, `ExpDate`, `CVV`,`Points`, `Handicapped`) " +
                                        "VALUES ('" + firstName + "', '" + lastName + "', '" +
                                        email + "', '" + phone + "', '" + password + "', '" + licenseNum + "', '" +
                                        registrationNum + "', '" + creditCardType + "', '" + creditCardNum +
                                        "', '" + expDate + "', '" + cvv + "', '10', '" + handicapAnsNum + "')";
                                //System.out.println(query);
                                stmt.executeUpdate(query);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //MOVE TO HOMESCREEN
                    Intent i = new Intent(SignUpActivity.this, HomeMenuActivity.class);
                    startActivity(i);
                }
            }

        });


        //CANCEL BUTTON
        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(i);
            }

        });

    }

    //SPINNER METHODS (need to be here but do nothing)
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
