package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jesse on 7/28/2019.
 */

public class Progress extends Activity {

    String currentOperation = "Current Operation";
    String startEval = "SOE";
    String endEval = "EOE";
    String student = "Student";
    String defaultSpeed = "10";

    String pupilID = "";

    ArrayList<String> names = new ArrayList<>();

    FactDBHelper FactDB;
    LoginDBHelper loginDB;

    final Calendar calendar = Calendar.getInstance();

    String defSelection;
    String[] selections;
    int operator = 0;

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

    // Symbols are for show in the App
    String[] mathfactsSymbols ={"1+1","1+2","1+3","1+4","1+5","1+6","1+7","1+8","1+9"
            ,"2+2","2+3","2+4","2+5","2+6","2+7","2+8","2+9"
            ,"3+3","3+4","3+5","3+6","3+7","3+8","3+9"
            ,"4+4","4+5","4+6","4+7","4+8","4+9"
            ,"5+5","5+6","5+7","5+8","5+9"
            ,"6+6","6+7","6+8","6+9"
            ,"7+7","7+8","7+9"
            ,"8+8","8+9"
            ,"9+9"};


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress);

        loginDB = new LoginDBHelper(this);

        FactDB = new FactDBHelper(this);

        String myFormat = "MM/dd/yy"; // Standard Format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        //Creating a continous parameter for time
        final String time = String.valueOf((year - 2017) * 366 + day );

        final SharedPreferences settings = getSharedPreferences(currentOperation, MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        final String oper = settings.getString(currentOperation,"Addition");

        String strt = settings.getString(startEval,sdf.format(calendar.getTime()));
        String stopEval = settings.getString(endEval,sdf.format(calendar.getTime()));

        //final EditText studentID = (EditText) findViewById(R.id.newName);
        final EditText SOE = (EditText) findViewById(R.id.editTextStartDate);
        final EditText EOE = (EditText) findViewById(R.id.editTextEndDate);
        Button eval = (Button) findViewById(R.id.evaluate);
        Spinner operations = (Spinner) findViewById(R.id.operations);
        Spinner studentSpinner = (Spinner) findViewById(R.id.studentSpinner);
        Spinner factSpeedSpinner = (Spinner) findViewById(R.id.speedSpinner);

        final CheckBox selectAll = (CheckBox) findViewById(R.id.selectAll);
        final CheckBox ones = (CheckBox) findViewById(R.id.selectOnes);
        final CheckBox twos = (CheckBox) findViewById(R.id.selectTwos);
        final CheckBox threes = (CheckBox) findViewById(R.id.selectthrees);
        final CheckBox fours = (CheckBox) findViewById(R.id.selectFours);
        final CheckBox fives = (CheckBox) findViewById(R.id.selectFives);
        final CheckBox sixs = (CheckBox) findViewById(R.id.selectSixs);
        final CheckBox sevens = (CheckBox) findViewById(R.id.selectSevens);
        final CheckBox eights = (CheckBox) findViewById(R.id.selectEights);
        final CheckBox nines = (CheckBox) findViewById(R.id.selectNines);


        // Set up Spinner for Operation to Evaluate
        operations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(parent.getItemAtPosition(position).toString(),currentOperation);
                editor.commit();
                //oper = settings.getString(currentOperation,"Addition");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> AddSubMultDiv = new ArrayList<String>();
        AddSubMultDiv.add("Addition");
        AddSubMultDiv.add("Subtraction");
        AddSubMultDiv.add("Multiplication");
        AddSubMultDiv.add("Division");

//        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,AddSubMultDiv);
//        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout,AddSubMultDiv);
        spinAdapter.setDropDownViewResource(R.layout.spinnerlayoutdropdown);

        operations.setAdapter(spinAdapter);
        operations.setSelection(AddSubMultDiv.indexOf(oper));

        // Set up Spinner for Student Names in System
        try {
            names = loginDB.availableUsers(names);
        }catch(Exception e){
            names.add("none");
            Toast.makeText(getApplicationContext(), "Go to User Settings and create Student Psuedonym and Pin for each student.", Toast.LENGTH_SHORT).show();
        };

        pupilID = settings.getString(student, names.get(0));

        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(parent.getItemAtPosition(position).toString(),student);
                editor.commit();
                pupilID = settings.getString(student, names.get(0));
                System.out.println("User ID is " + pupilID);

                // Set Math Fact Spinner Position
                try {
                    defaultSpeed = loginDB.getDefaultSpeedByName(new String[]{pupilID});
                    System.out.println("Look Up Name " + pupilID);
                } catch (Exception e) {
                    defaultSpeed = "10";
                    System.out.println("Failed to find name");
                }

                updateSpinner(defaultSpeed);

                // if the fact selection doesn't match then selection will be updated.
                try{
                    defSelection = loginDB.getDefaultFactSelectionByName(new String[]{pupilID});
                    System.out.println("defSelection = " + defSelection);
                    String tempSelect = rebuildSelectionString();
                    System.out.println( " temp Select = " +tempSelect);
                    if(!defSelection.equals(tempSelect)) {
                        loginDB.updateFactSelected(pupilID, tempSelect, time);
                        defSelection = loginDB.getDefaultFactSelectionByName(new String[]{pupilID});
                    }
                }catch (Exception e){
                    System.out.println("failed to update loginDB Selected");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> studentAdapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout,names);

        studentAdapter.setDropDownViewResource(R.layout.spinnerlayoutdropdown);

        studentSpinner.setAdapter(studentAdapter);
        studentSpinner.setSelection(names.indexOf(settings.getString(student,names.get(0))));


        // Set Response to Operation Selection
        operations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                operator = i+1;
                // Set Selected Math Facts
                try {
                    defSelection = loginDB.getDefaultFactSelectionByName(new String[]{pupilID});
                    System.out.println("defSelection = " + defSelection);

                    // Check if any changes were made - update DB accordingly
                    try {
                        String tempSelect = rebuildSelectionString();
                        System.out.println("tempSelect = " + tempSelect);
                        if (!defSelection.equals(tempSelect)) {
                            loginDB.updateFactSelected(pupilID, tempSelect, time);
                            defSelection = loginDB.getDefaultFactSelectionByName(new String[]{pupilID});
                            System.out.println("NEW defSelection = " + defSelection);
                        }
                    }catch (Exception e) {

                        System.out.println("Apparently first attemp at creating selection vector");
                    }


                    selections = defSelection.split("#");

                    if (selections[0].charAt(i+1) == '1') {
                        nines.setChecked(true);
                    } else {
                        nines.setChecked(false);
                    }
                    ;

                    if (selections[1].charAt(i+1) == '1') {
                        eights.setChecked(true);
                    } else {
                        eights.setChecked(false);
                    }
                    ;

                    if (selections[2].charAt(i+1) == '1') {
                        sevens.setChecked(true);
                    } else {
                        sevens.setChecked(false);
                    }
                    ;

                    if (selections[3].charAt(i+1) == '1') {
                        sixs.setChecked(true);
                    } else {
                        sixs.setChecked(false);
                    }
                    ;

                    if (selections[4].charAt(i+1) == '1') {
                        fives.setChecked(true);
                    } else {
                        fives.setChecked(false);
                    }
                    ;

                    if (selections[5].charAt(i+1) == '1') {
                        fours.setChecked(true);
                    } else {
                        fours.setChecked(false);
                    }
                    ;

                    if (selections[6].charAt(i+1) == '1') {
                        threes.setChecked(true);
                    } else {
                        threes.setChecked(false);
                    }
                    ;

                    if (selections[7].charAt(i+1) == '1') {
                        twos.setChecked(true);
                    } else {
                        twos.setChecked(false);
                    }
                    ;

                    if (selections[8].charAt(i+1) == '1') {
                        ones.setChecked(true);
                    } else {
                        ones.setChecked(false);
                    }
                    ;

                    if (ones.isChecked() & twos.isChecked() & threes.isChecked() & fours.isChecked() & fives.isChecked() & sixs.isChecked() & sevens.isChecked() & eights.isChecked() & nines.isChecked()) {
                        selectAll.setChecked(true);
                    } else {
                        selectAll.setChecked(false);
                    }
                }catch(Exception e){

                    Toast.makeText(getApplicationContext(), "A Student User ID must be selected.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set Response to select all
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                     if(b){
                                                         ones.setChecked(true);
                                                         updateSelection(0, operator,"1");
                                                         twos.setChecked(true);
                                                         updateSelection(1, operator,"1");
                                                         threes.setChecked(true);
                                                         updateSelection(2, operator,"1");
                                                         fours.setChecked(true);
                                                         updateSelection(3, operator,"1");
                                                         fives.setChecked(true);
                                                         updateSelection(4, operator,"1");
                                                         sixs.setChecked(true);
                                                         updateSelection(5, operator,"1");
                                                         sevens.setChecked(true);
                                                         updateSelection(6, operator,"1");
                                                         eights.setChecked(true);
                                                         updateSelection(7, operator,"1");
                                                         nines.setChecked(true);
                                                         updateSelection(8, operator,"1");

                                                     }

                                                 }
                                             });

        ones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[8] = updateSelection(8,operator,"1");
                }else{
                    selections[8] = updateSelection(8,operator,"0");
                }
            }
        });

        twos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[7] = updateSelection(7,operator,"1");
                }else{
                    selections[7] =  updateSelection(7,operator,"0");
                }
            }
        });

        threes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[6] = updateSelection(6,operator,"1");
                }else{
                    selections[6] = updateSelection(6,operator,"0");
                }
            }
        });

        fours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[5] = updateSelection(5,operator,"1");
                }else{
                    selections[5] = updateSelection(5,operator,"0");
                }
            }
        });

        fives.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[4] =  updateSelection(4,operator,"1");
                }else{
                    selections[4] = updateSelection(4,operator,"0");
                }
            }
        });

        sixs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[3] = updateSelection(3,operator,"1");
                }else{
                    selections[3] = updateSelection(3,operator,"0");
                }
            }
        });
        sevens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[2] = updateSelection(2,operator,"1");
                }else{
                    selections[2] = updateSelection(2,operator,"0");
                }
            }
        });

        eights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[1] = updateSelection(1,operator,"1");
                }else{
                    selections[1] = updateSelection(1,operator,"0");
                }
            }
        });

        nines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selections[0] = updateSelection(0,operator,"1");
                }else{
                    selections[0] = updateSelection(0,operator,"0");
                }
            }
        });
                // Set up Math Fact Speed Setting

                ArrayList timerList = new ArrayList();
        timerList.add("3 seconds/fact");
        timerList.add("4 seconds/fact");
        timerList.add("5 seconds/fact");
        timerList.add("7 seconds/fact");
        timerList.add("8 seconds/fact");
        timerList.add("9 seconds/fact");
        timerList.add("10 seconds/fact");
        timerList.add("11 seconds/fact");
        timerList.add("12 seconds/fact");
        timerList.add("13 seconds/fact");
        timerList.add("14 seconds/fact");
        timerList.add("15 seconds/fact");

        ArrayAdapter<String> factSpeedspinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timerList);
        factSpeedSpinner.setAdapter(factSpeedspinnerAdapter);
        factSpeedSpinner.setSelection(8);

        factSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Get position of fact speed spinner
                String setDefaultSpeed = String.valueOf(i+2);

                if(loginDB.getDefaultSpeedByName(new String[]{pupilID}).equals(setDefaultSpeed)) {
                    System.out.println("Default Speed Equals what is selected presently " + pupilID + " setDefaultSpeed = " + setDefaultSpeed);
                } else {
                    try {
                        loginDB.updateUserSpeed(pupilID, setDefaultSpeed, time);
                    }catch(Exception e){
                        System.out.println("Well.. at least it didn't break");
                    }
                }

                updateSelection(time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Set up datepicker for Start of Examination
        final DatePickerDialog.OnDateSetListener soeDate = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                String myFormat = "MM/dd/yy"; // Standard Format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editor.putString(startEval,sdf.format(calendar.getTime()));
                editor.commit();

                SOE.setText(sdf.format(calendar.getTime()));
            }
        };

        SOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelection(time);
                new DatePickerDialog(Progress.this, soeDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Set up datepicker for End of Examination
        final DatePickerDialog.OnDateSetListener eoeDate = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                String myFormat = "MM/dd/yy"; // Standard Format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editor.putString(endEval,sdf.format(calendar.getTime()));
                editor.commit();

                EOE.setText(sdf.format(calendar.getTime()));
            }
        };

        EOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelection(time);
                new DatePickerDialog(Progress.this, eoeDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Select Data Range
        SOE.setText(settings.getString(startEval,sdf.format(calendar.DATE)));
        EOE.setText(settings.getString(endEval,sdf.format(calendar.DATE)));

        // Evaluate inputs
        eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelection(time);

                new loadChildData().execute();
            }
        });
    }

    private class loadChildData extends AsyncTask<Object, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent info = getIntent();
            final String studentName = info.getStringExtra("Name");

            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_YEAR);

            /*
            //Creating a continous parameter for time
            String time = String.valueOf((year - 2017) * 366 + day );

            String tempSelect = rebuildSelectionString();
            loginDB.updateFactSelected(studentName,tempSelect,time);
            System.out.println("tempSelect = " + tempSelect);
*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           // Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Object... params) {

            // Get Intent Passed to Progress
            Intent input = getIntent();
            final String adminName = input.getStringExtra("Name");

            final SharedPreferences settings = getSharedPreferences(currentOperation, MODE_PRIVATE);
            final SharedPreferences.Editor editor = settings.edit();

            final EditText SOE = (EditText) findViewById(R.id.editTextStartDate);
            final EditText EOE = (EditText) findViewById(R.id.editTextEndDate);
            final Spinner operations = (Spinner) findViewById(R.id.operations);
            final Spinner studentSpinner = (Spinner) findViewById(R.id.studentSpinner);

            String oper = operations.getItemAtPosition(operations.getSelectedItemPosition()).toString();
            String studentID = studentSpinner.getItemAtPosition(studentSpinner.getSelectedItemPosition()).toString();

            editor.putString(currentOperation,oper);
            editor.commit();

            editor.putString(student,studentID);
            editor.commit();

            String soeString = SOE.getText().toString();
            String eoeString = EOE.getText().toString();

            char[] soeChar = soeString.toCharArray();
            char[] eoeChar = eoeString.toCharArray();

            System.out.println("Start of Evaluation = " + soeString);
            System.out.println("End of Evaluation = " +eoeString);

            Calendar soeCal = Calendar.getInstance();
            soeCal.set((Integer.valueOf(String.valueOf(soeChar[6]))*10+Integer.valueOf(String.valueOf(soeChar[7])))+2000,
                    (Integer.valueOf(String.valueOf(soeChar[0]))*10+Integer.valueOf(String.valueOf(soeChar[1])))-1,
                    Integer.valueOf(String.valueOf(soeChar[3]))*10+Integer.valueOf(String.valueOf(soeChar[4])));
            String soe = String.valueOf(soeCal.get(Calendar.DAY_OF_YEAR) + (soeCal.get(Calendar.YEAR)-2017)*366);

//            String soe = String.valueOf((Integer.valueOf(String.valueOf(soeChar[0]))*10+Integer.valueOf(String.valueOf(soeChar[1])))*31+
//                    Integer.valueOf(String.valueOf(soeChar[3]))*10+Integer.valueOf(String.valueOf(soeChar[4]))+
//                    ((Integer.valueOf(String.valueOf(soeChar[6]))*10+Integer.valueOf(String.valueOf(soeChar[7])))-17)*(366));

            Calendar eoeCal = Calendar.getInstance();
            eoeCal.set((Integer.valueOf(String.valueOf(eoeChar[6]))*10+Integer.valueOf(String.valueOf(eoeChar[7])))+2000,
                    (Integer.valueOf(String.valueOf(eoeChar[0]))*10+Integer.valueOf(String.valueOf(eoeChar[1])))-1,
                    Integer.valueOf(String.valueOf(eoeChar[3]))*10+Integer.valueOf(String.valueOf(eoeChar[4])));

            String eoe = String.valueOf(eoeCal.get(Calendar.DAY_OF_YEAR) + (eoeCal.get(Calendar.YEAR)-2017)*366);

//            String eoe = String.valueOf((Integer.valueOf(String.valueOf(eoeChar[0]))*10+Integer.valueOf(String.valueOf(eoeChar[1])))*31+
//                    (Integer.valueOf(String.valueOf(eoeChar[3]))*10+Integer.valueOf(String.valueOf(eoeChar[4])))+
//                    ((Integer.valueOf(String.valueOf(eoeChar[6]))*10+Integer.valueOf(String.valueOf(eoeChar[7])))-17)*(366));

            //String id = pupilID;

            String id = studentID;
            System.out.println("student = " + id + " in pupilID = " + pupilID);
            System.out.println("soe = " + soe);
            System.out.println("eoe = " + eoe);
            //
            // String mathOperation = settings.getString(currentOperation,"Addition");

            String mathOperation = oper;
            System.out.println("math operation = " + oper + " In Settings = " + settings.getString(currentOperation,"Addition"));

            if(soeString.length()>2 & eoeString.length()>2 & !id.equals("none")) {

                switch (mathOperation) {
                    case "Division":

                        Intent answersDiv = new Intent(getApplicationContext(), divresults.class);

                        answersDiv.putExtra("function", "Division");
                        answersDiv.putExtra("mathfacts", mathfactsDiv);

                        double[] mathfactsPercentDiv = {
                                0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0};

                        int[] mathfactsCorrectDiv = new int[mathfactsPercentDiv.length];

                        for (int l = 0; l < mathfactsDiv.length; l++) {

                            String[] factString = {id, soe, eoe, mathfactsDiv[l]};
                            //String[] factString = {id,mathfactsDiv[l]};
                            mathfactsPercentDiv[l] = FactDB.rangePercent(factString);
                            mathfactsCorrectDiv[l] = R.color.green;
                            //System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercent[l] );
                        }

                        answersDiv.putExtra("mathfactsPercent", mathfactsPercentDiv);
                        answersDiv.putExtra("mathfactsCorrect",mathfactsCorrectDiv);
                        answersDiv.putExtra("Name", adminName);
                        answersDiv.putExtra("admin","Yes");
//                        answersDiv.putExtra("studentName",settings.);

                        startActivity(answersDiv);
                        finish();
                        break;


                    case "Subtraction":
                        Intent answersSub = new Intent(getApplicationContext(), divresults.class);

                        answersSub.putExtra("function", "Subtraction");
                        answersSub.putExtra("mathfacts", mathfactsSub);

                        double[] mathfactsPercentSub = {
                                0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0, 0};

                        int[] mathfactsCorrectSub = new int[mathfactsPercentSub.length];

                        for (int l = 0; l < mathfactsSub.length; l++) {

                            String[] factString = {id, soe, eoe, mathfactsSub[l]};
                            mathfactsPercentSub[l] = FactDB.rangePercent(factString);
                            mathfactsCorrectSub[l] = R.color.green;
                            System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercentSub[l] );
                        }

                        answersSub.putExtra("mathfactsPercent", mathfactsPercentSub);
                        answersSub.putExtra("mathfactsCorrect",mathfactsCorrectSub);
                        answersSub.putExtra("Name", adminName);
                        answersSub.putExtra("admin","Yes");

                        startActivity(answersSub);
                        finish();
                        break;


                    case "Multiplication":
                        Intent answersMult = new Intent(getApplicationContext(), results.class);

                        answersMult.putExtra("function", "Multiplication");
                        answersMult.putExtra("mathfacts", mathfactsMult);

                        double[] mathfactsPercentMult = {0
                                , 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0
                                , 0, 0, 0, 0
                                , 0, 0, 0
                                , 0, 0
                                , 0};

                        int[] mathfactsCorrectMult = new int[mathfactsPercentMult.length];

                        for (int l = 0; l < mathfactsMult.length; l++) {

                            String[] factString = {id, soe, eoe, mathfactsMult[l]};
                            mathfactsPercentMult[l] = FactDB.rangePercent(factString);
                            mathfactsCorrectMult[l] = R.color.green;
                        }

                        answersMult.putExtra("mathfactsPercent", mathfactsPercentMult);
                        answersMult.putExtra("mathfactsCorrect",mathfactsCorrectMult);
                        answersMult.putExtra("Name", adminName);
                        answersMult.putExtra("admin","Yes");

                        startActivity(answersMult);
                        finish();
                        break;

                    case "Addition":
                        Intent answersAdd = new Intent(getApplicationContext(), results.class);

                        answersAdd.putExtra("function", "Addition");
                        answersAdd.putExtra("mathfacts", mathfactsSymbols);

                        double[] mathfactsPercentAdd = {0
                                , 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0, 0
                                , 0, 0, 0, 0, 0
                                , 0, 0, 0, 0
                                , 0, 0, 0
                                , 0, 0
                                , 0};

                        int[] mathfactsCorrectAdd = new int[mathfactsPercentAdd.length];

                        for (int l = 0; l < mathfactsAdd.length; l++) {

                            String[] factString = {id, eoe, soe, mathfactsAdd[l]};
                            mathfactsPercentAdd[l] = FactDB.rangePercent(factString);
                            mathfactsCorrectAdd[l] = R.color.green;
                            System.out.println(" l = " + String.valueOf(l) + " mathfactsPercent = " + mathfactsPercentAdd[l] );
                        }

                        answersAdd.putExtra("mathfactsPercent", mathfactsPercentAdd);
                        answersAdd.putExtra("mathfactsCorrect",mathfactsCorrectAdd);
                        answersAdd.putExtra("Name", adminName);
                        answersAdd.putExtra("admin","Yes");

                        startActivity(answersAdd);
                        finish();
                        break;

                    default:

                        Toast.makeText(getApplicationContext(), "No Operation was selected. Oper = " + mathOperation, Toast.LENGTH_SHORT).show();
                        break;

                }
            }else{

                Toast.makeText(getApplicationContext(), "All Cells Must Be Populated", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        Intent main = new Intent(getApplicationContext(),adminHomePage.class);
        main.putExtra("Name",studentName);
        String tempSelect = rebuildSelectionString();
        loginDB.updateFactSelected(studentName,tempSelect,"02202020");

        startActivity(main);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();

        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

        String tempSelect = rebuildSelectionString();
        loginDB.updateFactSelected(studentName,tempSelect,"02202020");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");

      try {
          String tempSelect = rebuildSelectionString();
          loginDB.updateFactSelected(studentName, tempSelect, "02202020");
      }catch (Exception e){
      }

    }


    public String updateSelection(int num, int operator, String bit){

        System.out.println("num = " + String.valueOf(num));
        System.out.println("operator = " + String.valueOf(operator));
        System.out.println(" bit value = " + bit);
        System.out.println(selections[num]);
        String temp=String.valueOf(selections[num].charAt(0));

        for(int j=1; j<5; j++){
            if(j==operator){
                temp = temp + bit;
            }else {
                temp = temp + String.valueOf(selections[num].charAt(j));
            }
        }
        System.out.println(temp);
        return temp;
    }

    public String rebuildSelectionString(){
        String temp = "";
        for(int j=0; j<9; j++){
            temp = temp + selections[j] + "#";

        }
        System.out.println("function:" + temp);
        return temp;
    }

    public void updateSelection(String time){
        // Check if any changes were made - update DB accordingly
        try {
            String tempSelect = rebuildSelectionString();
            System.out.println("tempSelect = " + tempSelect);
            if (!defSelection.equals(tempSelect)) {
                loginDB.updateFactSelected(pupilID, tempSelect, time);
                defSelection = loginDB.getDefaultFactSelectionByName(new String[]{pupilID});
                System.out.println("NEW defSelection = " + defSelection);
            }
        }catch (Exception e) {

            System.out.println("Apparently first attemp at creating selection vector");
        }
    }

    public void updateSpinner(String defaultSpeed){

        Spinner factSpeedSpinner = (Spinner) findViewById(R.id.speedSpinner);

        factSpeedSpinner.setSelection(Integer.valueOf(defaultSpeed)-2);
    }
}
