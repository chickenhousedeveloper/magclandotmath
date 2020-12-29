package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.String.valueOf;

/**
 * Created by Jesse on 10/29/2017.
 */

public class Multiplication extends Activity {


    FactDBHelper FactDB;

    String[] mathfacts ={"1x1","1x2","1x3","1x4","1x5","1x6","1x7","1x8","1x9"
            ,"2x2","2x3","2x4","2x5","2x6","2x7","2x8","2x9"
            ,"3x3","3x4","3x5","3x6","3x7","3x8","3x9"
            ,"4x4","4x5","4x6","4x7","4x8","4x9"
            ,"5x5","5x6","5x7","5x8","5x9"
            ,"6x6","6x7","6x8","6x9"
            ,"7x7","7x8","7x9"
            ,"8x8","8x9"
            ,"9x9"};

    int[] mathfactsAnswers ={1
            ,2,3,4,5,6,7,8,9
            ,4,6,8,10,12,14,16,18
            ,9,12,15,18,21,24,27
            ,16,20,24,28,32,36
            ,25,30,35,40,45
            ,36,42,48,54
            ,49,56,63
            ,64,72
            ,81};

    int[] mathfactsCorrect ={0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0
            ,0,0,0,0,0,0
            ,0,0,0,0,0
            ,0,0,0,0
            ,0,0,0
            ,0,0
            ,0};

    int[] mathfactsInCorrect ={0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0
            ,0,0,0,0,0,0
            ,0,0,0,0,0
            ,0,0,0,0
            ,0,0,0
            ,0,0
            ,0};

    double[] mathfactsPercent ={0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0
            ,0,0,0,0,0,0
            ,0,0,0,0,0
            ,0,0,0,0
            ,0,0,0
            ,0,0
            ,0};

    int[] mathfactOptions = {0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0
            ,0,0,0,0,0,0
            ,0,0,0,0,0
            ,0,0,0,0
            ,0,0,0
            ,0,0
            ,0};

    public Spinner spinner;

    CountDownTimer countdown;

    int numtimes = 1;
    int totalSetLength = 100*numtimes;//50*5; 12.28.2018 - changed to 100 mathfacts
    int counterlength = 1;

    public int questionInt = 0;

    Random rand = new Random();

    int k = 0;

    String defaultSpeed = "10";
    String defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
    int operator = 3;

    String[] selections;

    public Calendar calendar = Calendar.getInstance();

    final int[] counter = {0};
    int counterAtAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView top = (TextView) findViewById(R.id.Top);
        final TextView bottom = (TextView) findViewById(R.id.Bottom);
        final TextView correct = (TextView) findViewById(R.id.CorrectNum);
        final TextView incorrect = (TextView) findViewById(R.id.IncorrectNum);
        final TextView tooSlow = (TextView) findViewById(R.id.tooSlowNum);
        final EditText ans = (EditText) findViewById(R.id.answer);
        final Button button = (Button) findViewById(R.id.button);
        final Button inputMethodButton = (Button) findViewById(R.id.inputMethod);
        spinner = (Spinner) findViewById(R.id.spinner);

        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        ArrayList timerList = new ArrayList();
        timerList.add("3 seconds/fact");
        timerList.add("4 seconds/fact");
        timerList.add("5 seconds/fact");
        timerList.add("6 seconds/fact");
        timerList.add("7 seconds/fact");
        timerList.add("8 seconds/fact");
        timerList.add("9 seconds/fact");
        timerList.add("10 seconds/fact");
        timerList.add("11 seconds/fact");
        timerList.add("12 seconds/fact");
        timerList.add("13 seconds/fact");
        timerList.add("14 seconds/fact");
        timerList.add("15 seconds/fact");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timerList);
        spinner.setAdapter(spinnerAdapter);

        LoginDBHelper loginDB = new LoginDBHelper(this);

        try {
            defaultSpeed = loginDB.getDefaultSpeedByName(new String[]{studentName});
        } catch (Exception e) {
            defaultSpeed = "10";
            System.out.println("Failed to find name");
        }
        spinner.setSelection(Integer.valueOf(defaultSpeed)-1);

        // Math Fact Selection Vector
        try {
            defSelection = loginDB.getDefaultFactSelectionByName(new String[]{studentName});
        } catch (Exception e) {
            defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
            System.out.println("Failed to find name");
        }
        selections = defSelection.split("#");

        // Define what facts are included based upon facts selected from Admin account
        // Iterate through sets (ones, twos, threes etc.)
        for(int i = 0; i<9; i++){
            // Iterate through mathfacts and choose the ones that contain the sets of interest
            for(int j = 0; j<mathfacts.length; j++) {
                if (selections[8-i].charAt(operator) == '0') {
                    // selections is ordered awkwardly so that 9s are the first column
                } else {
                    if (mathfacts[j].contains(String.valueOf(i+1))){
                        mathfactOptions[j] = 1;
                        System.out.println(mathfacts[j]);
                    }
                }
            }
        }



        FactDB = new FactDBHelper(this);

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        //Creating a continous parameter for time
        String time = valueOf((year-2017)*366+day+hour/24+min/(24*60)+sec/(24*60*60));


        ans.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE & !ans.getText().toString().equals("")) {

                    String editTextAns;
                    try {
                        editTextAns = ans.getText().toString();
                    } catch (Exception e) {
                        editTextAns = "0";
                    }

                    try {
                        counterAtAnswer = counter[0];
                        countdown.stopCountdown();

                        if(evalAccuracy(editTextAns,mathfactsAnswers[k])){
                            counter[0] = 0;
                        }else {
                            counter[0] = -1;
                        };

                        System.out.println("stopped the countdown!");
                        return true;
                    } catch(Exception e){

                    }
                }
                return true;
            }
        });

        String[] todaysStats = new String[] {studentName, time , "1"};
        ArrayList stats = FactDB.DailyStats(todaysStats);
        if(stats.contains("empty")){
            correct.setText(String.valueOf(0));
        }else {
            correct.setText(String.valueOf(stats.size()));
        }

        todaysStats[2] = "0";
        stats = FactDB.DailyStats(todaysStats);
        if(stats.contains("empty")){
            incorrect.setText(String.valueOf(0));
        }else {
            incorrect.setText(String.valueOf(stats.size()));
        }

        todaysStats[2] = "2";
        stats = FactDB.DailyStats(todaysStats);
        if(stats.contains("empty")){
            tooSlow.setText(String.valueOf(0));
        }else {
            tooSlow.setText(String.valueOf(stats.size()));
        }

        inputMethodButton.setText("See Current Progress");
        inputMethodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    countdown.cancel();
                }catch (Exception e){

                }

                Intent answers = new Intent(getApplicationContext(),results.class);

                answers.putExtra("function", "Multiplication");
                answers.putExtra("mathfacts", mathfacts);
                answers.putExtra("mathfactsCorrect", mathfactsCorrect);


                for (int l=0; l<mathfacts.length; l++ ) {

                    String[] factString = {studentName , mathfacts[l]};
                    //mathfactsPercent[l] = mathfactsCorrect[l]/(denom);
                    mathfactsPercent[l] = FactDB.lastFive(factString);
                    System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercent[l] );
                }

                answers.putExtra("mathfactsPercent", mathfactsPercent);
                answers.putExtra("Name",studentName);
                answers.putExtra("admin","No");

                startActivity(answers);
                finish();
            }
        } );

        top.setText("");
        bottom.setText("x  ");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int setpoint = spinner.getSelectedItemPosition() + 2;
                System.out.println("setpoint " + setpoint);

                button.setEnabled(false);


                ans.requestFocus();
                //InputMethodManager inputMthdMngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputMthdMngr.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                //        for (int i = 0; i < totalSetLength; i++) {

                countdown = new CountDownTimer((setpoint+5)*1000, 1000) {

                    // Counter is reset at if/then statement

                    @Override
                    public void stopCountdown() {
                        super.stopCountdown();
                    }

                    public void onTick(long millisUntilFinished) {
                        // input math fact.
                        if(counter[0] == 0){
                            questionInt = selectMathFacts(mathfactOptions);
                            k = questionInt;

                            String[] problem = mathfacts[k].split("x");
                            System.out.println("when counter = 0, k = " + k );
                            System.out.println("setpoint = " + setpoint);

                            if(rand.nextBoolean()){
                                top.setText("   " + problem[0]);
                                bottom.setText("x " + problem[1]);
                            }else{
                                top.setText("   " + problem[1]);
                                bottom.setText("x " + problem[0]);
                            }

                            ans.setText("");
                            ans.setTextColor(Color.BLACK);
                            ans.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        }

                        // advance counter
                        counter[0] = counter[0] + 1;
                        System.out.println("counter = " + counter[0]);

                        if(counter[0] == setpoint) {
                            System.out.println("k = " + k);
                            System.out.println("mathfactsAnswers[k] = " + mathfactsAnswers[k]);

                            int year = calendar.get(Calendar.YEAR);
                            int day = calendar.get(Calendar.DAY_OF_YEAR);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int min = calendar.get(Calendar.MINUTE);
                            int sec = calendar.get(Calendar.SECOND);

                            //Creating a continous parameter for time
                            String time = valueOf((year - 2017) * 366 + day );

                            String editTextAns;
                            try {
                                editTextAns = ans.getText().toString();
                            } catch (Exception e) {
                                editTextAns = "0";
                            }
                            System.out.println("EditText = " + editTextAns);

                            if (editTextAns.isEmpty()) {

                                ans.setText("" + String.valueOf(mathfactsAnswers[k]));
                                ans.setTextColor(Color.rgb(255, 165, 0));
                                System.out.println("Time to Complete: " + setpoint+2);
                                FactDB.insertFact(studentName, mathfacts[k], "2", time, String.valueOf(setpoint+2));

                            } else {

                                if(editTextAns.contains(".")){
                                    String[] ansList;

                                    try{
                                        //String ansString = editTextAns.toString();
                                        ansList = editTextAns.split("\\.");
                                    }catch (Exception e){
                                        ansList = new String[]{"-999"};
                                    }

                                    System.out.println(editTextAns.split("\\."));
                                    System.out.println("ansList[0] = " + ansList[0]);
                                    editTextAns = String.valueOf(ansList[0]);
                                }


                                try {

                                    if (mathfactsAnswers[k] == Integer.parseInt(editTextAns)) {
                                        mathfactsCorrect[k] = mathfactsCorrect[k] + 1;
                                        ans.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                                        ans.setTextColor(Color.GREEN);
                                        ans.setText("CORRECT!!!");
                                        counter[0] = 0;
                                        System.out.println("Time to Complete: " + setpoint+2);

                                        //record success
                                        FactDB.insertFact(studentName, mathfacts[k], "1", time, String.valueOf(setpoint+2));
                                    } else {
                                        mathfactsInCorrect[k] = mathfactsInCorrect[k] + 1;
                                        ans.setTextColor(Color.RED);
                                        ans.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                        ans.setText("" + String.valueOf(mathfactsAnswers[k]));
                                        System.out.println("Incorrect!");
                                        System.out.println("Time to Complete: " + setpoint+2);
                                        counter[0] = 0;

                                        //record error
                                        FactDB.insertFact(studentName, mathfacts[k], "0", time, String.valueOf(setpoint+2));
                                    }

                                } catch (Exception e) {
                                    System.out.println("Something wrong with input - possibly no a number");
                                    counter[0] = 0;

                                }
                            }
                            String[] todaysStats = new String[] {studentName, time , "1"};
                            ArrayList stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                correct.setText("0");
                            }else {
                                correct.setText(String.valueOf(stats.size()));
                            }


                            todaysStats[2] = "0";
                            stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                incorrect.setText("0");
                            }else {
                                incorrect.setText(String.valueOf(stats.size()));
                            }

                            todaysStats[2] = "2";
                            stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                tooSlow.setText("0");
                            }else {
                                tooSlow.setText(String.valueOf(stats.size()));
                            }

                        }


                        if(counter[0] == setpoint+2){
                            counterAtAnswer = counter[0];
                            counter[0] = 0;
                            onFinish();
                        }
                    }

                    @Override
                    public void onFinish() {

                        System.out.println("onFinish()");

                        button.setEnabled(true);

                        // counter = 0 means that user followed standard process
                        // counter < setpoint means that the user indicated that they had solved the problem
                        // So we dont want the process if they followed the standard process but we do want this process
                        if(counter[0] != 0 & counter[0] < setpoint){

                            System.out.println("k = " + k );
                            System.out.println("mathfactsAnswers[k] = " + mathfactsAnswers[k] );
                            System.out.println("mathfactsAnswers[2] = " +mathfactsAnswers[2]);

                            int year = calendar.get(Calendar.YEAR);
                            int day = calendar.get(Calendar.DAY_OF_YEAR);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int min = calendar.get(Calendar.MINUTE);
                            int sec = calendar.get(Calendar.SECOND);

                            //Creating a continous parameter for time
                            String time = valueOf((year-2017)*366+day);

                            String editTextAns;
                            try {
                                editTextAns = ans.getText().toString();
                            }catch (Exception e){
                                editTextAns = "0";
                            }
                            System.out.println("EditText = " + editTextAns);

                            if(editTextAns.isEmpty()){

                                ans.setText(""  + String.valueOf(mathfactsAnswers[k]));
                                ans.setTextColor(Color.rgb(255,165,0));
                                System.out.println("Time to Complete: " + (setpoint+2));
                                //FactDB.insertFact(studentName,mathfacts[k],"2",time,String.valueOf(setpoint+2- counter[0]));
                                FactDB.insertFact(studentName,mathfacts[k],"2",time,String.valueOf(setpoint+2));

                            }else{

                                if(editTextAns.contains(".")){
                                    String[] ansList;

                                    try{
                                        //String ansString = editTextAns.toString();
                                        ansList = editTextAns.split("\\.");
                                    }catch (Exception e){
                                        ansList = new String[]{"-999"};
                                    }

                                    System.out.println(editTextAns.split("\\."));
                                    System.out.println("ansList[0] = " + ansList[0]);
                                    editTextAns = String.valueOf(ansList[0]);
                                }


                                try{
                                    if (mathfactsAnswers[k] == Integer.parseInt(editTextAns)) {
                                        mathfactsCorrect[k] = mathfactsCorrect[k] + 1;
                                        ans.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                                        ans.setTextColor(Color.GREEN);
                                        ans.setText("CORRECT!!!");
                                        counter[0] = 0;
                                        //System.out.println("Correct!!");
                                        System.out.println("Time to Complete: " + (counterAtAnswer));

                                        //record success
                                        FactDB.insertFact(studentName,mathfacts[k],"1",time,String.valueOf(counterAtAnswer));
                                    } else {
                                        mathfactsInCorrect[k] = mathfactsInCorrect[k] + 1;
                                        ans.setTextColor(Color.RED);
                                        ans.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                        ans.setText("" + String.valueOf(mathfactsAnswers[k]));
                                        System.out.println("Incorrect!");
                                        System.out.println("Time to Complete: " + (counterAtAnswer));

                                        //record error
                                        FactDB.insertFact(studentName,mathfacts[k],"0",time,String.valueOf(counterAtAnswer));
                                    }

                                } catch (Exception e) {
                                    System.out.println("Something wrong with input - possibly no a number");
                                    counter[0] = 0;

                                }
                            }

                            String[] todaysStats = new String[] {studentName, time , "1"};
                            ArrayList stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                correct.setText("0");
                            }else {
                                correct.setText(String.valueOf(stats.size()));
                            }


                            todaysStats[2] = "0";
                            stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                incorrect.setText("0");
                            }else {
                                incorrect.setText(String.valueOf(stats.size()));
                            }

                            todaysStats[2] = "2";
                            stats = FactDB.DailyStats(todaysStats);
                            if(stats.contains("empty")){
                                tooSlow.setText("0");
                            }else {
                                tooSlow.setText(String.valueOf(stats.size()));
                            }

                            counter[0] = 0;

                        }

                        if(counterlength<totalSetLength) {
                            countdown.cancel();
                            countdown.start();
                            counterlength ++;
                        }else{

                            countdown.cancel();
                            Intent answers = new Intent(getApplicationContext(),results.class);
                            answers.putExtra("function", "Multiplication");
                            answers.putExtra("mathfacts", mathfacts);
                            answers.putExtra("mathfactsCorrect", mathfactsCorrect);
                            // for (int l=0; l<mathfacts.length; l++ ) {
                            //     System.out.println(" " + mathfacts[l] + ", correct = " + mathfactsCorrect[l]
                            //             + ", incorrect = " + mathfactsInCorrect[l]);
                            // }

                            for (int l=0; l<mathfacts.length; l++ ) {
                                String[] factString = {studentName , mathfacts[l]};

                                mathfactsPercent[l]=FactDB.lastFive(factString);
                                System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercent[l] );
                            }
                            answers.putExtra("mathfactsPercent", mathfactsPercent);
                            answers.putExtra("Name",studentName);

                            startActivity(answers);
                            answers.putExtra("admin","No");
                            finish();

                        }

                    }

                };
                countdown.start();
                // }

            }}
        );


    }

    protected void onPause() {
        super.onPause();

        try{
            countdown.cancel();
        }catch (Exception e){
            System.out.println("countdown was not canceled" );
        }

    }

    protected void onStop() {
        super.onStop();

        try{
            countdown.cancel();
        }catch (Exception e){
            System.out.println("On Stop... countdown was not cancelled.");
        }
    }

    protected void onDestroy() {
        super.onDestroy();

        try{
            countdown.cancel();
        }catch (Exception e){
            System.out.println("On Destroy... countdown was not cancelled");
        }
    }

    private int selectMathFacts(int[] mathfactOptions) {

        int sum = 0;

        for (int j=0;j<mathfacts.length;j++) {
            if(mathfactOptions[j]==1){
                sum = sum + 1;
            };
        }

        if(sum==0){
            sum = mathfacts.length;
        }

        int[] list = new int[sum];
        int count = 0; // variable for populating the list with viable mathfact indexes

        for (int j=0;j<mathfactOptions.length;j++) {
            if(mathfactOptions[j]==1){
                list[count]=j;
                count++;
            };
        }


        int questionInt = rand.nextInt(sum);

        System.out.println("questionInt = " + questionInt);
        System.out.println("math fact location = " + list[questionInt]);

        return list[questionInt];
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        Intent main = new Intent(getApplicationContext(),MainActivity.class);
        main.putExtra("Name",studentName);
        startActivity(main);
        finish();
    }

    private boolean evalAccuracy(String editTextAns, int answer){


        if(editTextAns.isEmpty()){
            return false;

        }else{

            if(editTextAns.contains(".")){
                String[] ansList;

                try{
                    //String ansString = editTextAns.toString();
                    ansList = editTextAns.split("\\.");
                }catch (Exception e){
                    ansList = new String[]{"-999"};
                }

                System.out.println(editTextAns.split("\\."));
                System.out.println("ansList[0] = " + ansList[0]);
                editTextAns = String.valueOf(ansList[0]);
            }


            try{
                if (answer== Integer.parseInt(editTextAns)) {
                    return true;

                } else {
                    System.out.println("Incorrect!");
                    return false;
                }

            } catch (Exception e) {
                System.out.println("Something wrong with input - possibly no a number");
                return false;
            }
        }
    }

}
