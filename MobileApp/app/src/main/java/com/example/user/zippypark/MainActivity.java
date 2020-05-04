package com.example.user.zippypark;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jcraft.jsch.*;

import java.sql.*;

/**
 * written by: Atmika Ponnusamy
 */

public class MainActivity extends AppCompatActivity {
    EditText userNameEditText;
    EditText passwordEditText;
    Button login;
    Button signUp;
    static Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    connect();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        userNameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
                    userNameEditText.setError("Enter username");
                } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    passwordEditText.setError("Enter password");
                } else {
                    final String username = userNameEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();


                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery("SELECT Barcode FROM CustomerInfo " +
                                        "WHERE Email=\"" + username + "\" AND Password=\"" +
                                        password + "\"");
                                if (rs.next()) {
                                    int barcode = rs.getInt("Barcode");
                                    Intent i = new Intent(MainActivity.this,
                                            HomeMenuActivity.class);
                                    Bundle b = new Bundle();
                                    b.putInt("barcode", barcode);
                                    i.putExtras(b);
                                    startActivity(i);
                                } else {
                                    //figure out how to throw an error to the user
                                    //if the username and/or password is incorrect
                                    //Incorrect info notification
                                    runOnUiThread(new Runnable(){ //can't toast from non UI threads (this is the workaround)
                                        public void run(){
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "Login information incorrect.", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    });

                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


        signUp = findViewById(R.id.signupButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    private static void connect() throws ClassNotFoundException, SQLException {
        int rport = 3306;
        String rhost = "coewww.rutgers.edu";
        String user = "asp227";
        String pass = "Peacock427";
        //String driverName = "com.mysql.cj.jdbc.Driver";
        String dbUsername = "zippypark";
        String dbPassword = "goyhVynIiLcJgHpN";
        Session session = null;
        conn = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            Log.d("MainActivity", "Establishing Connection...");
            session.connect();
            Log.d("MainActivity", "Connection established.");

            int assigned_port = session.setPortForwardingL(0, "localhost", rport);
            Log.d("MainActivity", "localhost:" + assigned_port + " -> " + rhost + ":" + rport);
            Log.d("MainActivity", "Port Forwarded");
            //Class.forName(driverName);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:" + assigned_port + "/zippypark?user=" + dbUsername + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8");
        } catch (JSchException e) {
            e.printStackTrace();
        }//        finally {
//            if(conn != null && !conn.isClosed()){
//                Log.d("MainActivity","Closing Database Connection");
//                conn.close();
//            }
//            if(session !=null && session.isConnected()){
//                Log.d("MainActivity","Closing SSH Connection");
//                session.disconnect();
//            }
//        }
    }
}
