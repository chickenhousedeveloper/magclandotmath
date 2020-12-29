package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.String.valueOf;

//import java.util.Properties;


//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

/**
 * Created by Jesse on 7/20/2018.
 */

public class createCSVFile extends AsyncTask<Void, Integer, Void> {

    final String mathfactstxt = "MathFacts.txt";

    String studentName;
    String enteredPassword;
    String emailAddress = "";

    private Activity activity;

    Context mContext;

    public void createCSVFile(Context context, String[] userInfo) {



        studentName = userInfo[0];
        enteredPassword = userInfo[1];
        mContext = context;

    }

    @Override
    protected Void doInBackground(Void... params) {

        // ArrayList uris = new ArrayList(); // create array list

        int daysAgo = 15; // Number of days prior to today included in average

        File root = Environment.getExternalStorageDirectory();
        File mathFile = new File(root.getAbsolutePath(), mathfactstxt);

        Calendar calendar = Calendar.getInstance();

        String[] namePin = {studentName,enteredPassword};

        LoginDBHelper loginDB = new LoginDBHelper(mContext);
        FactDBHelper FactDB = new FactDBHelper(mContext);

        String correct, incorrect, tooSlow, prcntCor, prcntInCor, prcntTooSlow;
        long corr, incorr, slow, total;

        try{
            emailAddress = loginDB.getEmailAddress(namePin);
        } catch (Exception e){
            emailAddress = "jessemagnuson@gmail.com";
        }

     /*   ////////////////////////////////////////////////////////////////////
        // Build csv File
        //
        //
        ///////////////////////////////////////////////////////////////////

        if (!mathFile.exists()) {
            try {
                mathFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            mathFile.delete();
            try {
                mathFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(mathFile, true));
            writer.write("Time/Date, Fact, Correct");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] nameType = {studentName, "Type"};

        ArrayList<?> list = new ArrayList<>();

        int j = 0;
        while (j<10) {
            try {
                list = FactDB.createcvs(nameType);
            } catch (Exception e){
                System.out.println("attempt " + String.valueOf(j));
            }
            j = j + 1;
            if (list.size()>0) {
                j = 10;
            }
        }

        long Length = list.size() / 3;
        System.out.println("List length = " + String.valueOf(list.size()));

        j = 0;
        while (j < Length-1) {
            try {
                if (!list.get(3 + 3 * j).equals("0")) {
                    BufferedWriter writer = null;
                    writer = new BufferedWriter(new FileWriter(mathFile, true));
                    writer.write("" + list.get(3 * j).toString()
                            + "," + list.get(1 + 3 * j).toString()
                            + "," + list.get(2 + 3 * j).toString() + "");
                    writer.newLine();
                    writer.close();
                }
                j = j + 1;
                System.out.println("Next Line = " + String.valueOf(j));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                j = j + 1;
            }
        }
        */

        ////////////////////////////////////////////////////////////////////
        // Build csv File
        // ... Percent Correct answers in date range
        //
        //
        ///////////////////////////////////////////////////////////////////

        String[] numStr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ""};

        // Addition Mathfacts
        String[] mathfactsAdd ={"1_1","1_2","1_3","1_4","1_5","1_6","1_7","1_8","1_9"
                ,"2_2","2_3","2_4","2_5","2_6","2_7","2_8","2_9"
                ,"3_3","3_4","3_5","3_6","3_7","3_8","3_9"
                ,"4_4","4_5","4_6","4_7","4_8","4_9"
                ,"5_5","5_6","5_7","5_8","5_9"
                ,"6_6","6_7","6_8","6_9"
                ,"7_7","7_8","7_9"
                ,"8_8","8_9"
                ,"9_9"};

        String[] numAdd ={"+1","+2","+3","+4","+5","+6","+7","+8","+9"};

        // Subtraction Mathfacts
        String[] mathfactsSub ={"1-1","2-1","3-1","4-1","5-1","6-1","7-1","8-1","9-1"
                ,"2-2","3-2","4-2","5-2","6-2","7-2","8-2","9-2"
                ,"3-3","4-3","5-3","6-3","7-3","8-3","9-3"
                ,"4-4","5-4","6-4","7-4","8-4","9-4"
                ,"5-5","6-5","7-5","8-5","9-5"
                ,"6-6","7-6","8-6","9-6"
                ,"7-7","8-7","9-7"
                ,"8-8","9-8"
                ,"9-9"};

        String[] numSub ={"-1","-2","-3","-4","-5","-6","-7","-8","-9"};

        // Multiplication Mathfacts

        String[] mathfactsMult ={"1x1","1x2","1x3","1x4","1x5","1x6","1x7","1x8","1x9"
                ,"2x2","2x3","2x4","2x5","2x6","2x7","2x8","2x9"
                ,"3x3","3x4","3x5","3x6","3x7","3x8","3x9"
                ,"4x4","4x5","4x6","4x7","4x8","4x9"
                ,"5x5","5x6","5x7","5x8","5x9"
                ,"6x6","6x7","6x8","6x9"
                ,"7x7","7x8","7x9"
                ,"8x8","8x9"
                ,"9x9"};

        String[] numMult ={"x1","x2","x3","x4","x5","x6","x7","x8","x9"};

        // Division MathFacts
        String[] mathfactsDiv ={"1/1","2/1","3/1","4/1","5/1","6/1","7/1","8/1","9/1"
                ,"2/2","4/2","6/2","8/2","10/2","12/2","14/2","16/2","18/2"
                ,"3/3","6/3","9/3","12/3","15/3","18/3","21/3","24/3","27/3"
                ,"4/4","8/4","12/4","16/4","20/4","24/4","28/4","32/4","36/4"
                ,"5/5","10/5","15/5","20/5","25/5","30/5","35/5","40/5","45/5"
                ,"6/6","12/6","18/6","24/6","30/6","36/6","42/6","48/6","54/6"
                ,"7/7","14/7","21/7","28/7","35/7","42/7","49/7","56/7","63/7"
                ,"8/8","16/8","24/8","32/8","40/8","48/8","56/8","64/8","72/8"
                ,"9/9","18/9","27/9","36/9","45/9","54/9","63/9","72/9","81/9"};

        String[] numDiv ={"=1","=2","=3","=4","=5","=6","=7","=8","=9"};

        String percent = "";

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        //Creating a continous parameter for time
        int timeINT = ((year-2017)*366+day+hour/24+min/(24*60)+sec/(24*60*60));

// Create csv file
        if (!mathFile.exists()) {
            try {
                mathFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            mathFile.delete();
            try {
                mathFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Top Line of Accuracy Triangle: ADDITION
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(mathFile, true));
            writer.write("\t\t" + numAdd[0] + "\t\t" +
                    numAdd[1] + "\t\t" +
                    numAdd[2] + "\t\t" +
                    numAdd[3] + "\t\t" +
                    numAdd[4] + "\t\t" +
                    numAdd[5] + "\t\t" +
                    numAdd[6] + "\t\t" +
                    numAdd[7] + "\t\t" +
                    numAdd[8]
            );
            writer.newLine();

            // Create Accuracy Triangle: ADDITION
            int j = 9;
            int counter = 0;
            for (int i = 0; i < mathfactsAdd.length; i++) {
                if(i==0){
                    writer.write("" + numStr[0] + "\t\t");
                }

                String[] mathFactData = {studentName, mathfactsAdd[i]};

                percent = String.valueOf(FactDB.mathfactResult(mathFactData, timeINT-daysAgo));

                writer.write("" + percent + "\t\t");

                counter = counter + 1;

                if (counter == j) {
                    writer.newLine();
                    writer.write("" + numStr[10 - j] + " ");
                    for (int k = 0; k<(11-j); k++){
                        writer.write("\t\t");
                    }

                    j = j - 1;
                    counter = 0;
                }

            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
            }

        // Top Line of Accuracy Triangle: SUBTRACTION
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(mathFile, true));
            writer.write("\t\t" + numSub[0] + "\t\t" +
                    numSub[1] + "\t\t" +
                    numSub[2] + "\t\t" +
                    numSub[3] + "\t\t" +
                    numSub[4] + "\t\t" +
                    numSub[5] + "\t\t" +
                    numSub[6] + "\t\t" +
                    numSub[7] + "\t\t" +
                    numSub[8]
            );
            writer.newLine();

            // Create Accuracy Triangle: SUBTRACTION
            int j = 9;
            int counter = 0;
            for (int i = 0; i < mathfactsSub.length; i++) {
                if(i==0){
                    writer.write("" + numStr[0] + "\t\t");
                }

                String[] mathFactData = {studentName, mathfactsSub[i]};

                percent = String.valueOf(FactDB.mathfactResult(mathFactData, timeINT-daysAgo));

                writer.write("" + percent + "\t\t");

                counter = counter + 1;

                if (counter == j) {
                    writer.newLine();
                    writer.write("" + numStr[10 - j] + " ");
                    for (int k = 0; k<(11-j); k++){
                        writer.write("\t\t");
                    }

                    j = j - 1;
                    counter = 0;
                }

            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


        // Top Line of Accuracy Triangle: MULTIPLICATION
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(mathFile, true));
            writer.write("\t\t" + numMult[0] + "\t\t" +
                    numMult[1] + "\t\t" +
                    numMult[2] + "\t\t" +
                    numMult[3] + "\t\t" +
                    numMult[4] + "\t\t" +
                    numMult[5] + "\t\t" +
                    numMult[6] + "\t\t" +
                    numMult[7] + "\t\t" +
                    numMult[8]
            );
            writer.newLine();

            // Create Accuracy Triangle: MULTIPLICATION
            int j = 9;
            int counter = 0;
            for (int i = 0; i < mathfactsMult.length; i++) {
                if(i==0){
                    writer.write("" + numStr[0] + "\t\t");
                }

                String[] mathFactData = {studentName, mathfactsMult[i]};

                percent = String.valueOf(FactDB.mathfactResult(mathFactData, timeINT-daysAgo));

                writer.write("" + percent + "\t\t");

                counter = counter + 1;

                if (counter == j) {
                    writer.newLine();
                    writer.write("" + numStr[10 - j] + " ");
                    for (int k = 0; k<(11-j); k++){
                        writer.write("\t\t");
                    }
                //    System.out.println("Next Line = " + String.valueOf(j));
                    j = j - 1;
                    counter = 0;
                }

            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }



        // Top Line of Accuracy Triangle: DIVISION
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(mathFile, true));
            writer.write("\t\t" + numDiv[0] + "\t\t" +
                    numDiv[1] + "\t\t" +
                    numDiv[2] + "\t\t" +
                    numDiv[3] + "\t\t" +
                    numDiv[4] + "\t\t" +
                    numDiv[5] + "\t\t" +
                    numDiv[6] + "\t\t" +
                    numDiv[7] + "\t\t" +
                    numDiv[8]
            );
            writer.newLine();

            // Create Accuracy Triangle: DIVISION
            int j = 9;
            int counter = 0;
            for (int i = 0; i < mathfactsDiv.length; i++) {
                if(i==0){
                    writer.write("" + numStr[0] + "\t\t");
                }

                String[] mathFactData = {studentName, mathfactsDiv[i]};

                percent = String.valueOf(FactDB.mathfactResult(mathFactData, timeINT-daysAgo));

                writer.write("" + percent + "\t\t");

                counter = counter + 1;

                if (counter == 9) {
                    writer.newLine();
                    writer.write("" + numStr[10 - j] + " ");
                    j = j - 1;
                    counter = 0;
                }

            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }




        /////////////////////////////////////////////////////////////////////////////
        // Number of Correct, Incorrect and Too Slow Answers for the day
        //
        //
        /////////////////////////////////////////////////////////////////////////////

        //Creating a continous parameter for time
        String time = valueOf((year-2017)*366+day+hour/24+min/(24*60)+sec/(24*60*60));

        long[] add = new long[4]; // create a vector of number of attempts, correct, errors, too slow
        long[] sub = new long[4]; // create a vector of number of attempts, correct, errors, too slow
        long[] mult = new long[4]; // create a vector of number of attempts, correct, errors, too slow
        long[] div = new long[4]; // create a vector of number of attempts, correct, errors, too slow

        String[] todaysStats = new String[] {studentName, time , "1"};
        ArrayList stats = FactDB.DailyStats(todaysStats);
        if(stats.contains("empty")){
            correct = "0";
            corr = 0;
            System.out.println("Number correct is " + correct);
        }else {
            correct = String.valueOf(stats.size());
            corr = stats.size();
            System.out.println("Number correct is " + correct);

            // Count the number of each operation attempted & correct
            for(int m = 0; m<corr; m++){
                if(String.valueOf(stats.get(m)).contains("_")){
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
        if(stats.contains("empty")){
            incorrect = (String.valueOf(0));
            incorr = 0;
            System.out.println("Number incorrect is " + incorrect);
        }else {
            incorrect = (String.valueOf(stats.size()));
            incorr = stats.size();
            System.out.println("Number incorrect is " + incorrect);

            // Count the number of each operation attempted & incorrect
            for(int m = 0; m<(incorr); m++){
                if(String.valueOf(stats.get(m)).contains("_")){
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
        if(stats.contains("empty")){
            tooSlow = (String.valueOf(0));
            slow = 0;
            System.out.println("Number too Slow is " + tooSlow);
        }else {
            tooSlow = (String.valueOf(stats.size()));
            slow = stats.size();
            System.out.println("Number too Slow is " + tooSlow);

            // Count the number of each operation attempted & too slow
            for(int m = 0; m<slow; m++){
                if(String.valueOf(stats.get(m)).contains("_")){
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

        if(total>0) {
            long prcntCorVal = (long) ((100.0 * corr) / total);
            long prcntInCorVal = (long) ((100.0 * incorr) / total);
            long prcntTooSlowVal = (long) ((100.0 * slow) / total);

            // Set number of decimal places
            prcntCor = String.valueOf(prcntCorVal);
            prcntInCor = String.valueOf(prcntInCorVal);
            prcntTooSlow = String.valueOf(prcntTooSlowVal);

        }else{
            prcntCor = "??";
            prcntInCor = "??";
            prcntTooSlow = "??";
        }

// Set up and send intent
        //Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE); // to allow to send multiple attachments
        intent.setType("vnd.android.cursor.dir/email");
        intent.setType("application/x-vcard");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Math fact resuts by: " + studentName);// + dateFormat.format(start.getTime()).toString() + "-" + dateFormat.format(end.getTime()).toString());
        intent.putExtra(Intent.EXTRA_TEXT, "Math Fact data from Today\n\n" +
        "Today's Stats: \n" +
        "   Correct = " + correct + "    Percent = " + prcntCor + "%\n" +
        " InCorrect = " + incorrect  + "     Percent = " + prcntInCor + "%\n" +
        " Too Slow = " + tooSlow  + "     Percent = " + prcntTooSlow +"%\n" + "\n" +
        " " + String.valueOf(add[0])  + "\t\t Addition Facts" + "\n" +
        " " + String.valueOf(sub[0]) + "\t\t Subraction Facts" + "\n" +
        " " + String.valueOf(mult[0]) + "\t\t Multiplication Facts " + "\n" +
        " " + String.valueOf(div[0]) + "\t\t Division Facts " +  "\n"
        );

// Attach txt file

        Uri uriMath = Uri.fromFile(mathFile);
        System.out.println(uriMath.toString());
        //Uri uriImage = Uri.parse("android.resource://com.chickenhouse.mathfacts/" + R.mipmap.mathfacts);
        //System.out.println(uriImage.toString());
        //uris.add(uriMath);
        //uris.add(uriImage);
        intent.putExtra(Intent.EXTRA_STREAM, uriMath);
        //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // MathFacts App is displayed, instead of the email app.
        mContext.startActivity(Intent.createChooser(intent, "Select Email Provider"));

        return null;

    }

    // Method for getting the maximum value
    public static double getMax(double[] inputArray){
        double maxValue = 0;
        for(int i=0;i < inputArray.length;i++){
            if(inputArray[i] > maxValue){
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    // Method for getting the maximum value
    public static double getMin(double[] inputArray){
        double minValue = 100;
        for(int i=0;i < inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return minValue;
    }


}
