package com.chickenhouse.mathfacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
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
 * Created by Jesse on 2/22/2019.
 */

public class sendEmail extends Activity {

    private static final String PERMISSIONPLEASE = "PermissionPlease";
    public String studentName = "GUEST";
    LoginDBHelper loginDB;
    FactDBHelper FactDB;
    summaryDBHelper sumDB;

    String emailAddress;
    String enteredPassword;


    int totalINT, timeINT;
    String correct, incorrect, tooSlow, AvgSpeedString;


    String defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
    String defSPD = "10";


    boolean internetOn = true;

    String messageTxt = "";
    String htmlMessageText = "";
    String tablesMessage = "";

    int numEmailRemaining = -999;

    int initialNumOfEmails = 25;

    Integer MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1234;


    private static final String username = ~;
    private static final String password = ~;

    private static final String fromEmail = "Math.Facts.Results@magclan.tech";

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


//        PermissionExplanation expl_permissions = PermissionExplanation.newInstance();
        showExplanationDialog(false);


        try {
            studentName = input.getStringExtra("Name");
        } catch (Exception e) {
            studentName = "GUEST";
        }

        nameTV.setText(studentName);

        loginDB = new LoginDBHelper(this);
        sumDB = new summaryDBHelper(this);

        String[] baseNames = {"A", "B", "C"};
        String[] basePsswrd = {"0000", "0000", "0000"};
        String[] baseEmail = {"feedback@magclan.tech", "feedback@magclan.tech", "feedback@magclan.tech"};

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
                for (int j = 0; j < baseNames.length; j++) {
                    if (!names.contains(baseNames[j])) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                        String dateToStr = format.format(today);
                        loginDB.createNewUser(baseNames[j], basePsswrd[j], baseEmail[j], dateToStr, defSPD, defSelection);
                        System.out.print(baseNames[j] + " " + basePsswrd[j]);
                    }
                }
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

        loginDB.close();

        if (!password.isEmpty()) {
            if (password.get(0).equals("empty")) {
                System.out.println("password is producing something");
            }
        }

        final ArrayList<String> finalPassword = password;
        final ArrayList<String> finalNames = names;

        try {
            sumDB.getWritableDatabase();
            numEmailRemaining = sumDB.getNumOfEmailsRemain();
            sumDB.close();
        } catch (Exception e) {
            System.out.println("Number of Emails Remaining Not populated!!");
        }

        //if (numEmailRemaining==-999){
        if (numEmailRemaining<0){
            sumDB.getWritableDatabase();
            sumDB.insertSummary("EmailAllotment","none",-1,-1,-1,-1,-1,initialNumOfEmails,"999");
            numEmailRemaining = initialNumOfEmails;
            sumDB.close();
        }


        emailsRemaining.setText(String.valueOf(numEmailRemaining) + " Pre-Purchased Emails Remain  ");


        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        //Creating a continous parameter for time
        timeINT = ((year - 2017) * 366 + day);

        sumDB.getReadableDatabase();
        if(!sumDB.getMessageTxt(new String[] {studentName, String.valueOf(timeINT)}).isEmpty()) {
            messageText.setText(sumDB.getMessageTxt(new String[]{studentName, String.valueOf(timeINT)}));
        }
        sumDB.close();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Communicate to user that the button is not longer available and it working on sending the e-mail.
                //send.setText("Preparing Attachment...");
                //send.setClickable(false);

 /*               if (ContextCompat.checkSelfPermission(sendEmail.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    showExplanationDialog(true);

                } else {

                    showExplanationDialog(false);
*/
                    if(numEmailRemaining>0) {
                        String enteredPassword = finalPassword.get(finalNames.indexOf(studentName));
                        String[] namePin = {studentName, enteredPassword};

                        messageTxt = messageText.getText().toString();

                        sendEmail.createCSVfile csvFile = new sendEmail.createCSVfile();

                        csvFile.execute(namePin);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Name", studentName);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unable to Send. Please Purchase Additional Emails.", Toast.LENGTH_SHORT).show();
                    }

               // }
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

            int daysAgo = 15; // Number of days prior to today included in average

            Calendar calendar = Calendar.getInstance();

            //String[] namePin = {studentName, enteredPassword};
            // Attempts to resolves sending emails to me persitently
            String[] namePin = strings;

            LoginDBHelper loginDB = new LoginDBHelper(sendEmail.this);
            FactDBHelper FactDB = new FactDBHelper(sendEmail.this);
            summaryDBHelper SumDB = new summaryDBHelper(sendEmail.this);

            String prcntCor, prcntInCor, prcntTooSlow;
            long corr, incorr, slow, total;

            Double d = 0.0;

            try {
                emailAddress = loginDB.getEmailAddress(namePin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_YEAR);

            //Creating a continous parameter for time
            timeINT = ((year - 2017) * 366 + day);

            ////////////////////////////////////////////////////////////////////
            // Build Output
            // ... Percent Correct answers in date range
            //
            //
            ///////////////////////////////////////////////////////////////////



            System.out.println("Creating Accuracy Tables");

            String[] numStr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ""};

            // Addition Mathfacts
            String[] mathfactsAdd = {"1_1", "1_2", "1_3", "1_4", "1_5", "1_6", "1_7", "1_8", "1_9"
                    , "2_2", "2_3", "2_4", "2_5", "2_6", "2_7", "2_8", "2_9"
                    , "3_3", "3_4", "3_5", "3_6", "3_7", "3_8", "3_9"
                    , "4_4", "4_5", "4_6", "4_7", "4_8", "4_9"
                    , "5_5", "5_6", "5_7", "5_8", "5_9"
                    , "6_6", "6_7", "6_8", "6_9"
                    , "7_7", "7_8", "7_9"
                    , "8_8", "8_9"
                    , "9_9"};

            String[] numAdd = {"+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9"};

            // Subtraction Mathfacts
            String[] mathfactsSub = {
                    "2-1", "3-1", "4-1", "5-1", "6-1", "7-1", "8-1", "9-1", "10-1"
                    , "3-2", "4-2", "5-2", "6-2", "7-2", "8-2", "9-2", "10-2", "11-2"
                    , "4-3", "5-3", "6-3", "7-3", "8-3", "9-3", "10-3", "11-3", "12-3"
                    , "5-4", "6-4", "7-4", "8-4", "9-4", "10-4", "11-4", "12-4", "13-4"
                    , "6-5", "7-5", "8-5", "9-5", "10-5", "11-5", "12-5", "13-5", "14-5"
                    , "7-6", "8-6", "9-6", "10-6", "11-6", "12-6", "13-6", "14-6", "15-6"
                    , "8-7", "9-7", "10-7", "11-7", "12-7", "13-7", "14-7", "15-7", "16-7"
                    , "9-8", "10-8", "11-8", "12-8", "13-8", "14-8", "15-8", "16-8", "17-8"
                    , "10-9", "11-9", "12-9", "13-9", "14-9", "15-9", "16-9", "17-9", "18-9"};

            String[] numSub = {"-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9"};

            // Multiplication Mathfacts

            String[] mathfactsMult = {"1x1", "1x2", "1x3", "1x4", "1x5", "1x6", "1x7", "1x8", "1x9"
                    , "2x2", "2x3", "2x4", "2x5", "2x6", "2x7", "2x8", "2x9"
                    , "3x3", "3x4", "3x5", "3x6", "3x7", "3x8", "3x9"
                    , "4x4", "4x5", "4x6", "4x7", "4x8", "4x9"
                    , "5x5", "5x6", "5x7", "5x8", "5x9"
                    , "6x6", "6x7", "6x8", "6x9"
                    , "7x7", "7x8", "7x9"
                    , "8x8", "8x9"
                    , "9x9"};

            String[] numMult = {"x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9"};

            // Division MathFacts
            String[] mathfactsDiv = {"1/1", "2/1", "3/1", "4/1", "5/1", "6/1", "7/1", "8/1", "9/1"
                    , "2/2", "4/2", "6/2", "8/2", "10/2", "12/2", "14/2", "16/2", "18/2"
                    , "3/3", "6/3", "9/3", "12/3", "15/3", "18/3", "21/3", "24/3", "27/3"
                    , "4/4", "8/4", "12/4", "16/4", "20/4", "24/4", "28/4", "32/4", "36/4"
                    , "5/5", "10/5", "15/5", "20/5", "25/5", "30/5", "35/5", "40/5", "45/5"
                    , "6/6", "12/6", "18/6", "24/6", "30/6", "36/6", "42/6", "48/6", "54/6"
                    , "7/7", "14/7", "21/7", "28/7", "35/7", "42/7", "49/7", "56/7", "63/7"
                    , "8/8", "16/8", "24/8", "32/8", "40/8", "48/8", "56/8", "64/8", "72/8"
                    , "9/9", "18/9", "27/9", "36/9", "45/9", "54/9", "63/9", "72/9", "81/9"};

            String[] numDiv = {"=1", "=2", "=3", "=4", "=5", "=6", "=7", "=8", "=9"};

            String percent = "";


            //Creating a continous parameter for time
            timeINT = ((year - 2017) * 366 + day);



            /////////////////////////////////////////////////////////////////////////////
            // Number of Correct, Incorrect and Too Slow Answers for the day
            //
            //
            /////////////////////////////////////////////////////////////////////////////

            //Creating a continous parameter for time
            String time = valueOf((year-2017)*366+day);

            long[] add = new long[4]; // create a vector of number of attempts, correct, errors, too slow
            long[] sub = new long[4]; // create a vector of number of attempts, correct, errors, too slow
            long[] mult = new long[4]; // create a vector of number of attempts, correct, errors, too slow
            long[] div = new long[4]; // create a vector of number of attempts, correct, errors, too slow

            String[] todaysStats = new String[]{studentName, time, "1"};
            ArrayList stats = FactDB.DailyStats(todaysStats);
            if (stats.contains("empty")) {
                correct = "0";
                corr = 0;
                System.out.println("Number correct is " + correct);
            } else {
                correct = String.valueOf(stats.size());
                corr = stats.size();
                System.out.println("Number correct is " + correct);

                // Count the number of each operation attempted & correct
                for (int m = 0; m < corr; m++) {
                    if (String.valueOf(stats.get(m)).contains("_")) {
                        add[0]++; // total number of addition attempts
                        add[1]++; // correct addition attempts
                    } else if (String.valueOf(stats.get(m)).contains("-")) {
                        sub[0]++; // total number of subtraction attempts
                        sub[1]++; // correct subtraction attempts
                    } else if (String.valueOf(stats.get(m)).contains("x")) {
                        mult[0]++; // total number of multiplication attempts
                        mult[1]++; // correct multiplication attempts
                    } else if (String.valueOf(stats.get(m)).contains("/")) {
                        div[0]++; // total number of division attempts
                        div[1]++; // correct division attempts
                    } else {
                        System.out.print("Did not fit any available CORRECT categories value was = " + String.valueOf(stats.get(m)));
                    }
                }
            }


            todaysStats[2] = "0";
            stats = FactDB.DailyStats(todaysStats);
            if (stats.contains("empty")) {
                incorrect = (String.valueOf(0));
                incorr = 0;
                System.out.println("Number incorrect is " + incorrect);
            } else {
                incorrect = (String.valueOf(stats.size()));
                incorr = stats.size();
                System.out.println("Number incorrect is " + incorrect);

                // Count the number of each operation attempted & incorrect
                for (int m = 0; m < (incorr); m++) {
                    if (String.valueOf(stats.get(m)).contains("_")) {
                        add[0]++; // total number of addition attempts
                        add[2]++; // correct addition attempts
                    } else if (String.valueOf(stats.get(m)).contains("-")) {
                        sub[0]++; // total number of subtraction attempts
                        sub[2]++; // correct subtraction attempts
                    } else if (String.valueOf(stats.get(m)).contains("x")) {
                        mult[0]++; // total number of multiplication attempts
                        mult[2]++; // correct multiplication attempts
                    } else if (String.valueOf(stats.get(m)).contains("/")) {
                        div[0]++; // total number of division attempts
                        div[2]++; // correct division attempts
                    } else {
                        System.out.print("Did not fit any available INCORRECT categories value was = " + String.valueOf(stats.get(m)));
                    }
                }
            }

            todaysStats[2] = "2";
            stats = FactDB.DailyStats(todaysStats);
            if (stats.contains("empty")) {
                tooSlow = (String.valueOf(0));
                slow = 0;
                System.out.println("Number too Slow is " + tooSlow);
            } else {
                tooSlow = (String.valueOf(stats.size()));
                slow = stats.size();
                System.out.println("Number too Slow is " + tooSlow);

                // Count the number of each operation attempted & too slow
                for (int m = 0; m < slow; m++) {
                    if (String.valueOf(stats.get(m)).contains("_")) {
                        add[0]++; // total number of addition attempts
                        add[3]++; // correct addition attempts
                    } else if (String.valueOf(stats.get(m)).contains("-")) {
                        sub[0]++; // total number of subtraction attempts
                        sub[3]++; // correct subtraction attempts
                    } else if (String.valueOf(stats.get(m)).contains("x")) {
                        mult[0]++; // total number of multiplication attempts
                        mult[3]++; // correct multiplication attempts
                    } else if (String.valueOf(stats.get(m)).contains("/")) {
                        div[0]++; // total number of division attempts
                        div[3]++; // correct division attempts
                    } else {
                        System.out.print("Did not fit any available TOO SLOW categories value was = " + String.valueOf(stats.get(m)));
                    }
                }
            }

            total = corr + incorr + slow;
            System.out.println("Total number of facts = " + String.valueOf(total));

            if (total > 0) {
                long prcntCorVal = (long) ((100.0 * corr) / total);
                long prcntInCorVal = (long) ((100.0 * incorr) / total);
                long prcntTooSlowVal = (long) ((100.0 * slow) / total);

                // Set number of decimal places
                prcntCor = String.valueOf(prcntCorVal);
                prcntInCor = String.valueOf(prcntInCorVal);
                prcntTooSlow = String.valueOf(prcntTooSlowVal);

            } else {
                prcntCor = "??";
                prcntInCor = "??";
                prcntTooSlow = "??";
            }

            // Find Today's Average Speed
//            String[] inputData = new String[]{studentName, String.valueOf(Integer.valueOf(time)-timeINT)};

            String[] inputData = new String[]{studentName, time};

            double AvgSpeed = FactDB.todaysSpeed(inputData) - 1;
            String AvgSpeedString = String.format("%.2f", AvgSpeed);

            // Create message to parent
            String Subject = "Math fact resuts by: " + studentName;
            String messageText = "Math Fact data from Today\n\n" +
                    "Today's Stats: \n" +
                    "   Correct = " + correct + "    Percent = " + prcntCor + "%\n" +
                    " InCorrect = " + incorrect + "     Percent = " + prcntInCor + "%\n" +
                    " Too Slow = " + tooSlow + "     Percent = " + prcntTooSlow + "%\n" + "\n" +
                    " Average Speed = " + AvgSpeedString + " seconds/fact" + "\n\n" +
                    " " + String.valueOf(add[0]) + "\t\t Addition Facts" + "\n" +
                    " " + String.valueOf(sub[0]) + "\t\t Subtraction Facts" + "\n" +
                    " " + String.valueOf(mult[0]) + "\t\t Multiplication Facts " + "\n" +
                    " " + String.valueOf(div[0]) + "\t\t Division Facts " + "\n\n" +
                    messageTxt;

            int totalINT = Integer.valueOf(correct) + Integer.valueOf(incorrect) + Integer.valueOf(tooSlow);



//////////////////////////////////////////////
//
//  Create HTML File for Accuracy Tables
//
/////////////////////////////////////////////

            // Create Header for HTML file


                tablesMessage=tablesMessage + "<head>\n";
                tablesMessage=tablesMessage + "<style>\n";
                tablesMessage=tablesMessage + "table, th, td {\n";
                tablesMessage=tablesMessage + "border: 1px solid black;\n";
                tablesMessage=tablesMessage + "}\n";
                tablesMessage=tablesMessage + "</style>\n";
                tablesMessage=tablesMessage + "<meta charset='UTF-8'>\n";
                tablesMessage=tablesMessage + "</head>";

                tablesMessage=tablesMessage + "<h1><b>Message from " + studentName + "</b></h1>\n";
                tablesMessage=tablesMessage + "<p>" + messageTxt + "</p>\n\n";
                tablesMessage=tablesMessage + "<h1><b>" + "Math Fact data from Today" + "</b></h1>\n\n";
                tablesMessage=tablesMessage + "<p>Today's Stats: </p>\n";
                tablesMessage=tablesMessage + "<style>\n";
                tablesMessage=tablesMessage + "table, th, td {\n";
                tablesMessage=tablesMessage + "border: 1px solid black;\n";
                tablesMessage=tablesMessage + "}\n";
                tablesMessage=tablesMessage + "</style>\n";
                tablesMessage=tablesMessage + "<table><tr><th></th><th><b>Number</b></th><th><b></b></th><th><b>Percent</b></th></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>Correct</td><td>" + correct + "</td><td>Percent</td><td>" + prcntCor + "%</td></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>InCorrect</td><td>" + incorrect + "</td><td>Percent</td><td>" + prcntInCor + "%</td></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>Too Slow</td><td>" + tooSlow + "</td><td>Percent</td><td>" + prcntTooSlow + "%</td></tr></table>\n";
                tablesMessage=tablesMessage + "<p> Average Speed = " + AvgSpeedString + " seconds/fact</p>\n";
                tablesMessage=tablesMessage + "<table><tr><th><b>Number of Facts</b></th><th><b>Operation</b></th></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>" + String.valueOf(add[0]) + "</td><td>Addition Facts</td></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>" + String.valueOf(sub[0]) + "</td><td>Subtraction Facts</td></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>" + String.valueOf(mult[0]) + "</td><td>Multiplication Facts</td></tr>\n";
                tablesMessage=tablesMessage + "<tr><td>" + String.valueOf(div[0]) + "</td><td>Division Facts</td></tr></table>\n";
                tablesMessage=tablesMessage + "<p></p>\n";

                tablesMessage=tablesMessage + "<table>\n";
                tablesMessage=tablesMessage + "<tr>\n";
                tablesMessage=tablesMessage + "<td>";

            // Top Line of Accuracy Triangle: ADDITION
            createAccuracyTable(true, "ADDITION", numAdd, mathfactsAdd, numStr, timeINT, 15);

            // Start next entry in Accuracy Tables
                tablesMessage=tablesMessage + "\n<td>";

            // Top Line of Accuracy Triangle: SUBTRACTION
            createAccuracyTable(false, "SUBTRACTION", numSub, mathfactsSub, numStr, timeINT, 15);

            // End Row & Start next entry in Accuracy Tables
                tablesMessage=tablesMessage + "\n</tr><tr><td>";

            // Top Line of Accuracy Triangle: MULTIPLICATION
            createAccuracyTable(true, "MULTIPLICATION", numMult, mathfactsMult, numStr, timeINT, 15);

            // Start next entry in Accuracy Tables
                tablesMessage=tablesMessage + "\n<td>";

            // Top Line of Accuracy Triangle: DIVISION
            createAccuracyTable(false, "DIVISON", numDiv, mathfactsDiv, numStr, timeINT, 15);

            // Close the file
            tablesMessage=tablesMessage + "</tr></table>\n";

            System.out.println("Accuracy Tables Created");


            System.out.println("Message for Email: " + tablesMessage);

            messageText = tablesMessage;

            System.out.println("Email Receipient  = " + emailAddress);

            sendMail(emailAddress, Subject, messageText);

            return null;
        }

    }

    // Method for getting the maximum value
    public static double getMax(double[] inputArray) {
        double maxValue = 0;
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    // Method for getting the maximum value
    public static double getMin(double[] inputArray) {
        double minValue = 100;
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] < minValue) {
                minValue = inputArray[i];
            }
        }
        return minValue;
    }

// Good references for adding attachments and having a text field
//https://stackoverflow.com/questions/4345032/how-to-send-a-simple-email-programmatically-exists-a-simple-way-to-do-it
// https://stackoverflow.com/questions/25626301/javamail-does-not-send-message-content
    // Big idea if you want both an attachment and a tex message use a "multipart message"

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);

            System.out.println("Email Created");

            new sendEmail.SendMailTask().execute(message);

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
        InternetAddress[] emailAddresses = InternetAddress.parse(email);
        // message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setRecipients(Message.RecipientType.TO, emailAddress);
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
        properties.put("mail.smtp.connectiontimeout","60000");
        properties.put("mail.smtp.timeout","60000");
        properties.put("mail.smtp.writetimeout","60000");

        // Only cache DNS lookups for 10 seconds

        Security.setProperty("networkaddress.cache.ttl","10");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
            System.setProperty("mail.mime.charset", StandardCharsets.UTF_8.name());
        }

        return Session.getInstance(properties, new Authenticator() {
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
            // get writeable DB
            summaryDBHelper SumDB = new summaryDBHelper(getApplicationContext());
            SumDB.getWritableDatabase();

            if(internetOn && sentEmail) {
                Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                Integer numOfEmail = SumDB.getNumOfEmailsRemain();

                SumDB.insertSummary(studentName,messageTxt,Integer.valueOf(correct),Integer.valueOf(incorrect),
                        Integer.valueOf(tooSlow),timeINT, totalINT, numOfEmail-1,AvgSpeedString);

                SumDB.close();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Email Failed to Send. Check your internet connection.", Toast.LENGTH_SHORT).show();

                Integer numOfEmail = SumDB.getNumOfEmailsRemain();
                //System.out.println("num Of Emails = " + numOfEmail);

                //System.out.println("student Name = "+ studentName);
                //System.out.println("Correct = " + correct + " Incorrect = " + incorrect + " tooSlow = " + tooSlow + " timeINT = " +timeINT +
                //        " totalINT = " + totalINT + " Average Speed = " + AvgSpeedString);

                SumDB.insertSummary(studentName,messageTxt,Integer.valueOf(correct),Integer.valueOf(incorrect),
                        Integer.valueOf(tooSlow),timeINT, totalINT, numOfEmail,AvgSpeedString);

                SumDB.close();
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

    private void createAccuracyTable(Boolean triangle, String title, String[] headerRow, String[] mathfactsList, String[] headerClmn, int timeINT, int daysAgo) {
        // select triangle for Addition/Multiplication and rectangle for Subraction/Multiplication

        Double d;
        FactDBHelper FactDB = new FactDBHelper(sendEmail.this);
        int reqNumOfMathFacts = 50;

        if (triangle == true) {

            int effort = 0;

            for (int i = 0; i < mathfactsList.length; i++) {
                String[] mathFactData = {studentName, mathfactsList[i],String.valueOf(timeINT)};
                effort = effort + FactDB.activeFact(mathFactData,daysAgo);
            }

            System.out.println("Addition / Multiplication --> Calculated Effort = " + effort);

            //Arbitrarily shouldn't bother creating an accuracy plot for less than some min value
            if(effort>reqNumOfMathFacts) {
                // Create Accuracy Triangle (Addition/Multiplication)
                    tablesMessage = tablesMessage + "\n<h1>\n";
                    tablesMessage = tablesMessage + title;
                    tablesMessage = tablesMessage + "\n</h1>\n";
                    tablesMessage = tablesMessage + "<h3>(Number Of Facts = " + String.valueOf(effort) + ")</h3>\n";
                    tablesMessage = tablesMessage + "<table>\n";
                    tablesMessage = tablesMessage + "<tr>\n";
                    tablesMessage = tablesMessage + "<th></th><th>" + headerRow[0] + "</th><th>" +
                            headerRow[1] + "</th><th>" +
                            headerRow[2] + "</th><th>" +
                            headerRow[3] + "</th><th>" +
                            headerRow[4] + "</th><th>" +
                            headerRow[5] + "</th><th>" +
                            headerRow[6] + "</th><th>" +
                            headerRow[7] + "</th><th>" +
                            headerRow[8] + "</th>";
                    tablesMessage = tablesMessage + "</tr>\n";

                    // Create Accuracy Triangle
                    int j = 9;
                    int counter = 0;
                    for (int i = 0; i < mathfactsList.length; i++) {
                        if (i == 0) {
                            tablesMessage = tablesMessage + "<tr><td><b>" + headerClmn[0] + "</b></td>";
                        }

                        String[] mathFactData = {studentName, mathfactsList[i], String.valueOf(timeINT)};

                        d = FactDB.mathfactResult(mathFactData, daysAgo);
                        String percent = String.format("%.2f", d);

                        if (d <= 0.2) {
                            // Set background color to red
                            tablesMessage = tablesMessage + "<td style=\"background-color:#F44242\">" + percent + "</td>";
                        } else if (d <= 0.4) {
                            // Set background color to orange
                            tablesMessage = tablesMessage + "<td style=\"background-color:#FF5900\">" + percent + "</td>";
                        } else if (d <= 0.6) {
                            // Set background color to yellow
                            tablesMessage = tablesMessage + "<td style=\"background-color:#FFFA00\">" + percent + "</td>";
                        } else if (d <= 0.8) {
                            // Set background color to blue
                            tablesMessage = tablesMessage + "<td style=\"background-color:#87CEEB\">" + percent + "</td>";
                        } else if (d <= 1.0) {
                            // Set background color to green
                            tablesMessage = tablesMessage + "<td style=\"background-color:#03BF06\">" + percent + "</td>";
                        } else {
                            // if none of the above set to black
                            tablesMessage = tablesMessage + "<td style=\"background-color:#000000\">" + percent + "</td>";
                        }


                        counter = counter + 1;

                        if (counter == j) {

                            j = j - 1;

                            if (j == 0) {
                                tablesMessage = tablesMessage + "\n</table></td>\n";
                                tablesMessage = tablesMessage + "<td></td>";
                            } else {
                                tablesMessage = tablesMessage + "</tr>\n";
                                tablesMessage = tablesMessage + "<tr><td><b>" + headerClmn[9 - j] + "</b></td> ";
                                for (int k = 1; k < (10 - j); k++) {
                                    tablesMessage = tablesMessage + "<td style=\"background-color:#000000\">   </td>";
                                }
                            }

                            counter = 0;
                        }

                    }

            }else{

                    tablesMessage = tablesMessage + "\n<h1>\n";
                    tablesMessage = tablesMessage + title;
                    tablesMessage = tablesMessage + "\n</h1>\n";
                    tablesMessage = tablesMessage + "<table><tr>\n";
                    tablesMessage = tablesMessage + "<td style=\"background-color:#FFFFFF\">Not enough recent data was present to create chart</td></tr></table></td>\n";
                    tablesMessage = tablesMessage + "<td></td>";

            }
        } else {


            // Evaluate if data exists to create chart.
            int effort = 0;

            for (int i = 0; i < mathfactsList.length; i++) {
                String[] mathFactData = {studentName, mathfactsList[i], String.valueOf(timeINT)};
                effort = effort + FactDB.activeFact(mathFactData,daysAgo);
            }

            System.out.println("Subtraction / Division --> Calculated Effort = " + effort);

            //Arbitrarily shouldn't bother creating an accuracy plot for less than some min value
            if(effort>reqNumOfMathFacts) {
                // Create accuracy rectangle (Subraction/Division)
                    tablesMessage = tablesMessage + "\n<h1>\n";
                    tablesMessage = tablesMessage + title;
                    tablesMessage = tablesMessage + "\n</h1>\n";
                    tablesMessage = tablesMessage + "<h3>(Number Of Facts = " + String.valueOf(effort) + ")</h3>\n";
                    tablesMessage = tablesMessage + "<table>\n";
                    tablesMessage = tablesMessage + "<tr>\n";
                    tablesMessage = tablesMessage + "<th></th><th>" + headerRow[0] + "</th><th>" +
                            headerRow[1] + "</th><th>" +
                            headerRow[2] + "</th><th>" +
                            headerRow[3] + "</th><th>" +
                            headerRow[4] + "</th><th>" +
                            headerRow[5] + "</th><th>" +
                            headerRow[6] + "</th><th>" +
                            headerRow[7] + "</th><th>" +
                            headerRow[8] + "</th>";
                    tablesMessage = tablesMessage + "</tr>\n";

                    // Create Accuracy Triangle
                    int j = 9;
                    int counter = 0;
                    for (int i = 0; i < mathfactsList.length; i++) {
                        if (i == 0) {
                            tablesMessage = tablesMessage + "<tr><td><b>" + headerClmn[0] + "</b></td>";
                        }

                        String[] mathFactData = {studentName, mathfactsList[i], String.valueOf(timeINT)};

                        d = FactDB.mathfactResult(mathFactData, daysAgo);
                        String percent = String.format("%.2f", d);

                        if (d <= 0.2) {
                            // Set background color to red
                            tablesMessage = tablesMessage + "<td style=\"background-color:#F44242\">" + percent + "</td>";
                        } else if (d <= 0.4) {
                            // Set background color to orange
                            tablesMessage = tablesMessage + "<td style=\"background-color:#FF5900\">" + percent + "</td>";
                        } else if (d <= 0.6) {
                            // Set background color to yellow
                            tablesMessage = tablesMessage + "<td style=\"background-color:#FFFA00\">" + percent + "</td>";
                        } else if (d <= 0.8) {
                            // Set background color to blue
                            tablesMessage = tablesMessage + "<td style=\"background-color:#87CEEB\">" + percent + "</td>";
                        } else if (d <= 1.0) {
                            // Set background color to green
                            tablesMessage = tablesMessage + "<td style=\"background-color:#03BF06\">" + percent + "</td>";
                        } else {
                            // if none of the above set to black
                            tablesMessage = tablesMessage + "<td style=\"background-color:#000000\">" + percent + "</td>";
                        }


                        counter = counter + 1;

                        if (counter == 9) {


                            j = j - 1;
                            counter = 0;

                            if (j == 0) {
                                tablesMessage = tablesMessage + "\n</tr></table></td>\n";
                                tablesMessage = tablesMessage + "<td></td>";
                            }else{
                                tablesMessage = tablesMessage + "</tr>";
                                tablesMessage = tablesMessage + "<tr><td><b>" + headerClmn[9 - j] + "</b></td>";
                            }
                        }

                    }

            }else{

                    tablesMessage = tablesMessage + "\n<h1>";
                    tablesMessage = tablesMessage + title;
                    tablesMessage = tablesMessage + "\n</h1>";
                    tablesMessage = tablesMessage + "<table><tr>";
                    tablesMessage = tablesMessage + "<td style=\"background-color:#FFFFFF\">Not enough recent data was present to create chart</td></tr></table></td>";
                    tablesMessage = tablesMessage + "<td></td>";

            }
        }

        FactDB.close();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(getApplicationContext(),MainActivity.class);
        main.putExtra("Name",studentName);
        startActivity(main);
        finish();
    }
}
