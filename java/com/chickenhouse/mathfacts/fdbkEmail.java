package com.chickenhouse.mathfacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

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
import javax.mail.internet.MimeMultipart;

import static java.lang.String.valueOf;

/**
 * Created by Jesse on 10/29/2019.
 */

public class fdbkEmail extends Activity {


    public String studentName = "GUEST";
    LoginDBHelper loginDB;
    summaryDBHelper sumDB;

    String emailAddress;
    String enteredPassword;
    String adminEmail;

    boolean internetOn = true;

    String messageTxt = "";
    String htmlMessageText = "";

    int numEmailRemaining = -999;

    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1234;

    private static final String username = ~;
    private static final String password = ~;

    private static final String fromEmail = "Math.Facts.Results@magclan.tech";
    String magtechEmailAddress = "feedback@magclan.tech";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

        Date today = new Date();

        final TextView nameTV = (TextView) findViewById(R.id.studentName);
        final Button send = (Button) findViewById(R.id.sendEmail);
        final EditText messageText = (EditText) findViewById(R.id.enterMessage);
        final TextView emailsRemaining = (TextView) findViewById(R.id.emailsRemaining);

        Intent input = getIntent();

        showExplanationDialog(false);


        try {
            studentName = input.getStringExtra("Name");
        } catch (Exception e) {
            studentName = "GUEST";
        }

        nameTV.setText(studentName);

        loginDB = new LoginDBHelper(this);

        ArrayList<String> names = new ArrayList();
        ArrayList<String> password = new ArrayList();

        try {
            names = loginDB.availableUsers(names);
        } catch (Exception e) {
            System.out.println("Try #1");
        }

        if (!names.isEmpty()) {
            if (names.get(0).equals("empty")) {
                System.out.print("names was empty!");
                        try {
                    names = loginDB.availableUsers(names);
                } catch (Exception e) {
                    System.out.println("Try #2");
                }
            }
        }

        try {
            password = loginDB.associatedPins(password);
        } catch (Exception e) {
            System.out.println("Try #1: pins");
        }

        if (!password.isEmpty()) {
            if (password.get(0).equals("empty")) {
                System.out.println("password is producing something");
            }
        }

        final ArrayList<String> finalPassword = password;
        final ArrayList<String> finalNames = names;

        // Set up gift Manager
        freebieManager giftManager = new freebieManager(this);

        try {
            giftManager.update();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        try{
            String[] nameString = {studentName};
            adminEmail = loginDB.getEmailAddressByName(nameString);
        }catch (Exception e){

        }
        giftManager.execute(adminEmail);


        // No input required. Feedback Emails are free.
        emailsRemaining.setText("");


        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                // Get num of Email Remaining prior to sending feedback email
                try {
                    sumDB.getWritableDatabase();
                    numEmailRemaining = sumDB.getNumOfEmailsRemain();
                    sumDB.close();
                } catch (Exception e) {
                    System.out.println("Number of Emails Remaining Not populated!!");
                }


                    String enteredPassword = finalPassword.get(finalNames.indexOf(studentName));
                    String[] namePin = {studentName, enteredPassword};

                    messageTxt = messageText.getText().toString();

                    fdbkEmail.createCSVfile csvFile = new fdbkEmail.createCSVfile();

                    csvFile.execute();

                    Intent main = new Intent(getApplicationContext(),adminHomePage.class);
                    main.putExtra("Name",studentName);
                    startActivity(main);
                    finish();

            }
        });

    }

    private void showExplanationDialog(Boolean flag) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.Fragment permissionsExplan = fm.findFragmentById(R.id.permission_explanation);
        android.app.FragmentTransaction fragTrans = fm.beginTransaction();

        if(flag){
            fragTrans.show(permissionsExplan).commit();
        }else {
            fragTrans.hide(permissionsExplan).commit();
        }
    }





    class createCSVfile extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {

            ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mgr.getActiveNetworkInfo();


            if (netInfo != null) {
                // if (netInfo.isConnected()) {
                if (netInfo.isConnectedOrConnecting()){
                    // Internet Available
                    internetOn = true;
                }else {
                    //No internet
                    internetOn = false;
                }
            } else {
                //No internet
                internetOn = false;
            }
        }

        ;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(String... strings) {

            Calendar calendar = Calendar.getInstance();

//            String[] namePin = {studentName, enteredPassword};
            String[] namePin = {studentName};

            LoginDBHelper loginDB = new LoginDBHelper(fdbkEmail.this);

            try {
                emailAddress = loginDB.getEmailAddressByName(namePin);
            } catch (Exception e) {
                emailAddress = username;
            }


            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_YEAR);


            //Creating a continous parameter for time
            int timeINT = ((year - 2017) * 366 + day);


            // Create message to parent
            String Subject = "Math fact resuts by: " + studentName;
            String messageText = "Email :" + emailAddress +
                    " has something important to say about MagClan.Tech's Timed Facts App "+"\n\n" +
                    messageTxt;

            String htmlMessageText = "";
//////////////////////////////////////////////
//
//  Create HTML File for Accuracy Tables
//
/////////////////////////////////////////////

            // Create Header for HTML file
                htmlMessageText = htmlMessageText +"<head>\n";
                htmlMessageText = htmlMessageText +"<style>\n";
            htmlMessageText = htmlMessageText +"table, th, td {\n";
            htmlMessageText = htmlMessageText +"border: 1px solid black;\n";
            htmlMessageText = htmlMessageText +"}\n";
            htmlMessageText = htmlMessageText +"</style>\n";
            htmlMessageText = htmlMessageText +"<meta charset='UTF-8'>\n";
            htmlMessageText = htmlMessageText +"</head>";



            htmlMessageText = htmlMessageText +"<h1><b>Message from Administrator: " + emailAddress + "</b></h1>\n";
            htmlMessageText = htmlMessageText + "<h1><b>" + String.valueOf(numEmailRemaining) + " Pre-Purchased Emails Remain Device " + "</b></h1>\n\n";
            htmlMessageText = htmlMessageText +"<p>" + messageTxt + "</p>\n\n";


            System.out.println("Message for Email: " + messageText);

            System.out.println("Email Receipient  = " + emailAddress);

            sendMail(magtechEmailAddress, Subject, htmlMessageText);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);

            System.out.println("Email Created");

            new fdbkEmail.SendMailTask().execute(message);

        } catch (AddressException e) {
            e.printStackTrace();

            System.out.println("Address Exception");
        } catch (MessagingException e) {
            e.printStackTrace();

            System.out.println("Message Exception");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            System.out.println("Encoding exception");
        }
    }


    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, "Math Facts Results"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);

        // Create Multipart Message
        Multipart multipartMessage = new MimeMultipart();

        // Create part text part of message
        BodyPart messageBodyPart = new MimeBodyPart();
        //messageBodyPart.setText(messageBody);
        messageBodyPart.setContent(messageBody,"text/html");

        // Add message to multipart email
        multipartMessage.addBodyPart(messageBodyPart);

        message.setContent(multipartMessage);

        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // The next two lines were added to attempt to increase security for sending email
        properties.put("mail.smtp.ssl.checkserveridentity","true");
        properties.put("mail.smtp.starttls.required","true");

        //properties.put("mail.smtp.host", "smtp.gmail.com"); // Used this when we were sending over gmail
        properties.put("mail.smtp.host","email-smtp.us-east-1.amazonaws.com");
        properties.put("mail.smtp.port", "587");
        //properties.put("mail.smtp.ssl.trust","smtp.gmail.com");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {

        boolean sentEmail = true;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progressDialog.dismiss();


            if(internetOn && sentEmail) {
                Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Email Failed to Send.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                sentEmail = false;
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(getApplicationContext(),adminHomePage.class);
        main.putExtra("Name",studentName);
        startActivity(main);
        finish();
    }

}
