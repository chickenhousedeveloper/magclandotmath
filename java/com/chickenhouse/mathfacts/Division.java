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

import static java.lang.String.valueOf;

/**
 * Created by Jesse on 1/4/2019.
 */

public class Division extends Activity {

    FactDBHelper FactDB;

    String[] mathfacts ={"1/1","2/1","3/1","4/1","5/1","6/1","7/1","8/1","9/1"
            ,"2/2","4/2","6/2","8/2","10/2","12/2","14/2","16/2","18/2"
            ,"3/3","6/3","9/3","12/3","15/3","18/3","21/3","24/3","27/3"
            ,"4/4","8/4","12/4","16/4","20/4","24/4","28/4","32/4","36/4"
            ,"5/5","10/5","15/5","20/5","25/5","30/5","35/5","40/5","45/5"
            ,"6/6","12/6","18/6","24/6","30/6","36/6","42/6","48/6","54/6"
            ,"7/7","14/7","21/7","28/7","35/7","42/7","49/7","56/7","63/7"
            ,"8/8","16/8","24/8","32/8","40/8","48/8","56/8","64/8","72/8"
            ,"9/9","18/9","27/9","36/9","45/9","54/9","63/9","72/9","81/9"};

    int[] mathfactsAnswers ={
             1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9
            ,1,2,3,4,5,6,7,8,9};

    int[] mathfactsCorrect ={
            0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0};

    int[] mathfactsInCorrect ={
            0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0};

    double[] mathfactsPercent ={
            0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0};

    int[] mathfactOptions = {
            0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0
            ,0,0,0,0,0,0,0,0,0};

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
    int operator = 4;

    String[] selections;

    public Calendar calendar = Calendar.getInstance();

    final int[] counter = {0};
    int counterAtAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.divisionlayout);

        final TextView left = (TextView) findViewById(R.id.Top);
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
                    if (mathfacts[j].contains("/"+ String.valueOf(i+1))){
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
        String time = valueOf((year-2017)*366+day);


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

                Intent answers = new Intent(getApplicationContext(),divresults.class);

                answers.putExtra("function", "Division");
                answers.putExtra("mathfacts", mathfacts);
                answers.putExtra("mathfactsCorrect", mathfactsCorrect);


                for (int l=0; l<mathfacts.length; l++ ) {

                    String[] factString = {studentName , mathfacts[l]};
                    //mathfactsPercent[l] = mathfactsCorrect[l]/(denom);
                    mathfactsPercent[l] = FactDB.lastFive(factString);
                    //System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercent[l] );
                }

                answers.putExtra("mathfactsPercent", mathfactsPercent);
                answers.putExtra("Name",studentName);
                answers.putExtra("admin","No");

                startActivity(answers);
                finish();
            }
        } );

        left.setText(" ");
        bottom.setText(" ");

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


                    @Override
                    public void stopCountdown() {
                        super.stopCountdown();
                    }

                    public void onTick(long millisUntilFinished) {
                        // input math fact.
                        if(counter[0] == 0){
                            questionInt = selectMathFacts(mathfactOptions);
                            k = questionInt;

                            String[] problem = mathfacts[k].split("/");
                            System.out.println("when counter[0] = 0, k = " + k );
                            System.out.println("setpoint = " + setpoint);

                            left.setText("" + problem[1]);
                            bottom.setText("" + problem[0]);

                            ans.setText("");
                            ans.setTextColor(Color.BLACK);
                            ans.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        }

                        // advance counter
                        counter[0] = counter[0] + 1;
                        System.out.println("counter[0] = " + counter[0]);

                        if(counter[0] == setpoint) {
                            System.out.println("k = " + k);
                            System.out.println("mathfactsAnswers[k] = " + mathfactsAnswers[k]);
                            System.out.println("mathfactsAnswers[2] = " + mathfactsAnswers[2]);

                            int year = calendar.get(Calendar.YEAR);
                            int day = calendar.get(Calendar.DAY_OF_YEAR);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int min = calendar.get(Calendar.MINUTE);
                            int sec = calendar.get(Calendar.SECOND);

                            //Creating a continous parameter for time
                            String time = valueOf((year - 2017) * 366 + day);

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
                                FactDB.insertFact(studentName, mathfacts[k], "2", time, String.valueOf(setpoint+2));
                                System.out.println("Too Slow!");

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
                                        //ans.setText("CORRECT!!!");
                                        //System.out.println("Correct!!");

                                        //record success
                                        FactDB.insertFact(studentName, mathfacts[k], "1", time, String.valueOf(setpoint+2));
                                    } else {
                                        mathfactsInCorrect[k] = mathfactsInCorrect[k] + 1;
                                        ans.setTextColor(Color.RED);
                                        ans.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                        ans.setText("" + String.valueOf(mathfactsAnswers[k]));
                                        System.out.println("Incorrect!");

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

                        // counter[0] = 0 means that user followed standard process
                        // counter[0] < setpoint means that the user indicated that they had solved the problem
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
                                        //System.out.println("Correct!!");

                                        //record success
                                        FactDB.insertFact(studentName,mathfacts[k],"1",time,String.valueOf(counterAtAnswer));
                                    } else {
                                        mathfactsInCorrect[k] = mathfactsInCorrect[k] + 1;
                                        ans.setTextColor(Color.RED);
                                        ans.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                        ans.setText("" + String.valueOf(mathfactsAnswers[k]));
                                        System.out.println("Incorrect!");

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
                            Intent answers = new Intent(getApplicationContext(),divresults.class);
                            answers.putExtra("function", "Division");
                            answers.putExtra("mathfacts", mathfacts);
                            answers.putExtra("mathfactsCorrect", mathfactsCorrect);
                            // for (int l=0; l<mathfacts.length; l++ ) {
                            //     System.out.println(" " + mathfacts[l] + ", correct = " + mathfactsCorrect[l]
                            //             + ", incorrect = " + mathfactsInCorrect[l]);
                            // }

                            for (int l=0; l<mathfacts.length; l++ ) {
                                String[] factString = {studentName , mathfacts[l]};

                                mathfactsPercent[l]=FactDB.lastFive(factString);
                                //System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercent[l] );
                            }
                            answers.putExtra("mathfactsPercent", mathfactsPercent);
                            answers.putExtra("Name",studentName);
                            answers.putExtra("admin","No");

                            startActivity(answers);
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
