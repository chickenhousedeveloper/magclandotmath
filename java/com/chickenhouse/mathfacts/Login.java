package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import static com.chickenhouse.mathfacts.R.id.sendEmail;
import static com.chickenhouse.mathfacts.R.id.title_template;
import static java.lang.String.valueOf;

/**
 * Created by Jesse on 11/6/2017.
 */

public class Login extends Activity {

    public String studentName = "GUEST";
    LoginDBHelper loginDB;

    String admin = "AdminExists";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        final SharedPreferences settings = getSharedPreferences(admin, MODE_PRIVATE);
        final String adminExists = settings.getString(admin,"none");

        if (adminExists.equals("none") | adminExists.equals("null")){
            System.out.println("No Admin has been established");
            Intent intent = new Intent(getApplicationContext(), createAdminActivity.class);
            startActivity(intent);
        }

        Date today = new Date();

        final Button log = (Button) findViewById(R.id.login);
        final EditText name = (EditText) findViewById(R.id.enterStudentName);
        final EditText bday = (EditText) findViewById(R.id.enterBirthday);

        Intent input = getIntent();

        try {
            studentName = input.getStringExtra("Name");
        } catch (Exception e) {
            studentName = "GUEST";
        }

        loginDB = new LoginDBHelper(this);


        ArrayList<String> names = new ArrayList();

        try {
            names = loginDB.logins(names);
        }catch (Exception e){

        }



        if (names.get(0).toString().equals("empty")){
            System.out.println("No Admin has been established");
            Intent intent = new Intent(getApplicationContext(), createAdminActivity.class);
            startActivity(intent);
            finish();
        }

        final ArrayList<String> finalNames = names;


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredName = name.getText().toString().toUpperCase();
                String enteredPassword = bday.getText().toString();

                if (finalNames.contains(enteredName)) {
                    if (String.valueOf(enteredPassword).equals(finalNames.get(finalNames.indexOf(enteredName)+1))) {
                        studentName = enteredName;
                        Toast.makeText(getApplicationContext(), "Welcome " + enteredName, Toast.LENGTH_SHORT).show();

                        if (enteredName.toUpperCase().equals(settings.getString(admin,"none"))) {
                            Intent intent = new Intent(getApplicationContext(), adminHomePage.class);
                            intent.putExtra("Name", studentName);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("Name", studentName);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext()
                                , "Pin or Name does not match a database record.", Toast.LENGTH_SHORT).show();

                        /*
                        System.out.println("Password InCorrect for Name = " + enteredName);
                        System.out.println("Password = " + enteredPassword);
                        System.out.println("Index = " + finalNames.indexOf(enteredName));
                        System.out.println("Index = " + finalNames.get(finalNames.indexOf(enteredName)+1));
                        System.out.println("Length names = " + finalNames.size());

                         */

                    }
                } else {
                    Toast.makeText(getApplicationContext()
                            , "Birth date or Name does not match a database record.", Toast.LENGTH_SHORT).show();

                    //System.out.println("Name InCorrect for Name = " + enteredName);

                }
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}

