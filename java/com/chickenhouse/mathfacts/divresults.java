package com.chickenhouse.mathfacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

/**
 * Created by Jesse on 12/25/2016.
 */

public class divresults extends Activity {

    String studentName = "Guest";

    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results2);

        final TextView topx1 = (TextView) findViewById(R.id.x1);
        final TextView sidex1 = (TextView) findViewById(R.id.mult1x);
        final TextView topx2 = (TextView) findViewById(R.id.x2);
        final TextView sidex2 = (TextView) findViewById(R.id.mult2x);
        final TextView topx3 = (TextView) findViewById(R.id.x3);
        final TextView sidex3 = (TextView) findViewById(R.id.mult3x);
        final TextView topx4 = (TextView) findViewById(R.id.x4);
        final TextView sidex4 = (TextView) findViewById(R.id.mult4x);
        final TextView topx5 = (TextView) findViewById(R.id.x5);
        final TextView sidex5 = (TextView) findViewById(R.id.mult5x);
        final TextView topx6 = (TextView) findViewById(R.id.x6);
        final TextView sidex6 = (TextView) findViewById(R.id.mult6x);
        final TextView topx7 = (TextView) findViewById(R.id.x7);
        final TextView sidex7 = (TextView) findViewById(R.id.mult7x);
        final TextView topx8 = (TextView) findViewById(R.id.x8);
        final TextView sidex8 = (TextView) findViewById(R.id.mult8x);
        final TextView topx9 = (TextView) findViewById(R.id.x9);
        final TextView sidex9 = (TextView) findViewById(R.id.mult9x);

        final TextView prob1x1 = (TextView) findViewById(R.id.prob1x1);
        final TextView prob1x2 = (TextView) findViewById(R.id.prob1x2);
        final TextView prob1x3 = (TextView) findViewById(R.id.prob1x3);
        final TextView prob1x4 = (TextView) findViewById(R.id.prob1x4);
        final TextView prob1x5 = (TextView) findViewById(R.id.prob1x5);
        final TextView prob1x6 = (TextView) findViewById(R.id.prob1x6);
        final TextView prob1x7 = (TextView) findViewById(R.id.prob1x7);
        final TextView prob1x8 = (TextView) findViewById(R.id.prob1x8);
        final TextView prob1x9 = (TextView) findViewById(R.id.prob1x9);

        final TextView prob2x1 = (TextView) findViewById(R.id.prob2x1);
        final TextView prob2x2 = (TextView) findViewById(R.id.prob2x2);
        final TextView prob2x3 = (TextView) findViewById(R.id.prob2x3);
        final TextView prob2x4 = (TextView) findViewById(R.id.prob2x4);
        final TextView prob2x5 = (TextView) findViewById(R.id.prob2x5);
        final TextView prob2x6 = (TextView) findViewById(R.id.prob2x6);
        final TextView prob2x7 = (TextView) findViewById(R.id.prob2x7);
        final TextView prob2x8 = (TextView) findViewById(R.id.prob2x8);
        final TextView prob2x9 = (TextView) findViewById(R.id.prob2x9);

        final TextView prob3x1 = (TextView) findViewById(R.id.prob3x1);
        final TextView prob3x2 = (TextView) findViewById(R.id.prob3x2);
        final TextView prob3x3 = (TextView) findViewById(R.id.prob3x3);
        final TextView prob3x4 = (TextView) findViewById(R.id.prob3x4);
        final TextView prob3x5 = (TextView) findViewById(R.id.prob3x5);
        final TextView prob3x6 = (TextView) findViewById(R.id.prob3x6);
        final TextView prob3x7 = (TextView) findViewById(R.id.prob3x7);
        final TextView prob3x8 = (TextView) findViewById(R.id.prob3x8);
        final TextView prob3x9 = (TextView) findViewById(R.id.prob3x9);

        final TextView prob4x1 = (TextView) findViewById(R.id.prob4x1);
        final TextView prob4x2 = (TextView) findViewById(R.id.prob4x2);
        final TextView prob4x3 = (TextView) findViewById(R.id.prob4x3);
        final TextView prob4x4 = (TextView) findViewById(R.id.prob4x4);
        final TextView prob4x5 = (TextView) findViewById(R.id.prob4x5);
        final TextView prob4x6 = (TextView) findViewById(R.id.prob4x6);
        final TextView prob4x7 = (TextView) findViewById(R.id.prob4x7);
        final TextView prob4x8 = (TextView) findViewById(R.id.prob4x8);
        final TextView prob4x9 = (TextView) findViewById(R.id.prob4x9);

        final TextView prob5x1 = (TextView) findViewById(R.id.prob5x1);
        final TextView prob5x2 = (TextView) findViewById(R.id.prob5x2);
        final TextView prob5x3 = (TextView) findViewById(R.id.prob5x3);
        final TextView prob5x4 = (TextView) findViewById(R.id.prob5x4);
        final TextView prob5x5 = (TextView) findViewById(R.id.prob5x5);
        final TextView prob5x6 = (TextView) findViewById(R.id.prob5x6);
        final TextView prob5x7 = (TextView) findViewById(R.id.prob5x7);
        final TextView prob5x8 = (TextView) findViewById(R.id.prob5x8);
        final TextView prob5x9 = (TextView) findViewById(R.id.prob5x9);

        final TextView prob6x1 = (TextView) findViewById(R.id.prob6x1);
        final TextView prob6x2 = (TextView) findViewById(R.id.prob6x2);
        final TextView prob6x3 = (TextView) findViewById(R.id.prob6x3);
        final TextView prob6x4 = (TextView) findViewById(R.id.prob6x4);
        final TextView prob6x5 = (TextView) findViewById(R.id.prob6x5);
        final TextView prob6x6 = (TextView) findViewById(R.id.prob6x6);
        final TextView prob6x7 = (TextView) findViewById(R.id.prob6x7);
        final TextView prob6x8 = (TextView) findViewById(R.id.prob6x8);
        final TextView prob6x9 = (TextView) findViewById(R.id.prob6x9);

        final TextView prob7x1 = (TextView) findViewById(R.id.prob7x1);
        final TextView prob7x2 = (TextView) findViewById(R.id.prob7x2);
        final TextView prob7x3 = (TextView) findViewById(R.id.prob7x3);
        final TextView prob7x4 = (TextView) findViewById(R.id.prob7x4);
        final TextView prob7x5 = (TextView) findViewById(R.id.prob7x5);
        final TextView prob7x6 = (TextView) findViewById(R.id.prob7x6);
        final TextView prob7x7 = (TextView) findViewById(R.id.prob7x7);
        final TextView prob7x8 = (TextView) findViewById(R.id.prob7x8);
        final TextView prob7x9 = (TextView) findViewById(R.id.prob7x9);

        final TextView prob8x1 = (TextView) findViewById(R.id.prob8x1);
        final TextView prob8x2 = (TextView) findViewById(R.id.prob8x2);
        final TextView prob8x3 = (TextView) findViewById(R.id.prob8x3);
        final TextView prob8x4 = (TextView) findViewById(R.id.prob8x4);
        final TextView prob8x5 = (TextView) findViewById(R.id.prob8x5);
        final TextView prob8x6 = (TextView) findViewById(R.id.prob8x6);
        final TextView prob8x7 = (TextView) findViewById(R.id.prob8x7);
        final TextView prob8x8 = (TextView) findViewById(R.id.prob8x8);
        final TextView prob8x9 = (TextView) findViewById(R.id.prob8x9);

        final TextView prob9x1 = (TextView) findViewById(R.id.prob9x1);
        final TextView prob9x2 = (TextView) findViewById(R.id.prob9x2);
        final TextView prob9x3 = (TextView) findViewById(R.id.prob9x3);
        final TextView prob9x4 = (TextView) findViewById(R.id.prob9x4);
        final TextView prob9x5 = (TextView) findViewById(R.id.prob9x5);
        final TextView prob9x6 = (TextView) findViewById(R.id.prob9x6);
        final TextView prob9x7 = (TextView) findViewById(R.id.prob9x7);
        final TextView prob9x8 = (TextView) findViewById(R.id.prob9x8);
        final TextView prob9x9 = (TextView) findViewById(R.id.prob9x9);

        Intent answers = getIntent();

        int[] mathfactsCorrect = answers.getIntArrayExtra("mathfactsCorrect");
        double[] mathfactsPercent = answers.getDoubleArrayExtra("mathfactsPercent");
        String[] mathfacts = answers.getStringArrayExtra("mathfacts");
        admin = answers.getStringExtra("admin");
        System.out.println( "mathfacts = " + mathfacts[0] );
        String function = answers.getStringExtra("function");
        studentName = answers.getStringExtra("Name");

        for(int i = 0; i<mathfactsPercent.length; i++) {

            if(mathfactsPercent[i]>=0.81){mathfactsCorrect[i] = R.color.green;}
            else {
                if (mathfactsPercent[i] >= 0.61) {
                    mathfactsCorrect[i] = R.color.lightblue;
                }else{
                    if (mathfactsPercent[i] >= 0.41) {
                        mathfactsCorrect[i] = R.color.yellow;
                    }else{
                        if (mathfactsPercent[i] >= 0.21) {
                            mathfactsCorrect[i] = R.color.orange;
                        }else{
                            if (mathfactsPercent[i] >= 0.0) {
                                mathfactsCorrect[i] = R.color.red;
                            }else{
                                    mathfactsCorrect[i] = R.color.black;
                            }
                        }
                    }
                }

            }

        }

        prob1x1.setText(mathfacts[0]);
        prob1x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[0],null));
        prob1x2.setText(mathfacts[1]);
        prob1x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[1],null));
        prob1x3.setText(mathfacts[2]);
        prob1x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[2],null));
        prob1x4.setText(mathfacts[3]);
        prob1x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[3],null));
        prob1x5.setText(mathfacts[4]);
        prob1x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[4],null));
        prob1x6.setText(mathfacts[5]);
        prob1x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[5],null));
        prob1x7.setText(mathfacts[6]);
        prob1x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[6],null));
        prob1x8.setText(mathfacts[7]);
        prob1x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[7],null));
        prob1x9.setText(mathfacts[8]);
        prob1x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[8],null));

        prob2x1.setText(mathfacts[9]);
        prob2x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[9],null));
        prob2x2.setText(mathfacts[10]);
        prob2x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[10],null));
        prob2x3.setText(mathfacts[11]);
        prob2x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[11],null));
        prob2x4.setText(mathfacts[12]);
        prob2x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[12],null));
        prob2x5.setText(mathfacts[13]);
        prob2x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[13],null));
        prob2x6.setText(mathfacts[14]);
        prob2x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[14],null));
        prob2x7.setText(mathfacts[15]);
        prob2x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[15],null));
        prob2x8.setText(mathfacts[16]);
        prob2x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[16],null));
        prob2x9.setText(mathfacts[17]);
        prob2x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[17],null));

        prob3x1.setText(mathfacts[18]);
        prob3x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[18],null));
        prob3x2.setText(mathfacts[19]);
        prob3x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[19],null));
        prob3x3.setText(mathfacts[20]);
        prob3x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[20],null));
        prob3x4.setText(mathfacts[21]);
        prob3x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[21],null));
        prob3x5.setText(mathfacts[22]);
        prob3x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[22],null));
        prob3x6.setText(mathfacts[23]);
        prob3x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[23],null));
        prob3x7.setText(mathfacts[24]);
        prob3x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[24],null));
        prob3x8.setText(mathfacts[25]);
        prob3x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[25],null));
        prob3x9.setText(mathfacts[26]);
        prob3x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[26],null));

        prob4x1.setText(mathfacts[27]);
        prob4x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[27],null));
        prob4x2.setText(mathfacts[28]);
        prob4x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[28],null));
        prob4x3.setText(mathfacts[29]);
        prob4x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[29],null));
        prob4x4.setText(mathfacts[30]);
        prob4x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[30],null));
        prob4x5.setText(mathfacts[31]);
        prob4x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[31],null));
        prob4x6.setText(mathfacts[32]);
        prob4x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[32],null));
        prob4x7.setText(mathfacts[33]);
        prob4x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[33],null));
        prob4x8.setText(mathfacts[34]);
        prob4x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[34],null));
        prob4x9.setText(mathfacts[35]);
        prob4x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[35],null));

        prob5x1.setText(mathfacts[36]);
        prob5x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[36],null));
        prob5x2.setText(mathfacts[37]);
        prob5x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[37],null));
        prob5x3.setText(mathfacts[38]);
        prob5x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[38],null));
        prob5x4.setText(mathfacts[39]);
        prob5x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[39],null));
        prob5x5.setText(mathfacts[40]);
        prob5x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[40],null));
        prob5x6.setText(mathfacts[41]);
        prob5x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[41],null));
        prob5x7.setText(mathfacts[42]);
        prob5x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[42],null));
        prob5x8.setText(mathfacts[43]);
        prob5x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[43],null));
        prob5x9.setText(mathfacts[44]);
        prob5x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[44],null));

        prob6x1.setText(mathfacts[45]);
        prob6x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[45],null));
        prob6x2.setText(mathfacts[46]);
        prob6x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[46],null));
        prob6x3.setText(mathfacts[47]);
        prob6x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[47],null));
        prob6x4.setText(mathfacts[48]);
        prob6x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[48],null));
        prob6x5.setText(mathfacts[49]);
        prob6x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[49],null));
        prob6x6.setText(mathfacts[50]);
        prob6x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[50],null));
        prob6x7.setText(mathfacts[51]);
        prob6x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[51],null));
        prob6x8.setText(mathfacts[52]);
        prob6x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[52],null));
        prob6x9.setText(mathfacts[53]);
        prob6x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[53],null));

        prob7x1.setText(mathfacts[54]);
        prob7x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[54],null));
        prob7x2.setText(mathfacts[55]);
        prob7x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[55],null));
        prob7x3.setText(mathfacts[56]);
        prob7x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[56],null));
        prob7x4.setText(mathfacts[57]);
        prob7x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[57],null));
        prob7x5.setText(mathfacts[58]);
        prob7x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[58],null));
        prob7x6.setText(mathfacts[59]);
        prob7x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[59],null));
        prob7x7.setText(mathfacts[60]);
        prob7x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[60],null));
        prob7x8.setText(mathfacts[61]);
        prob7x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[61],null));
        prob7x9.setText(mathfacts[62]);
        prob7x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[62],null));

        prob8x1.setText(mathfacts[63]);
        prob8x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[63],null));
        prob8x2.setText(mathfacts[64]);
        prob8x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[64],null));
        prob8x3.setText(mathfacts[65]);
        prob8x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[65],null));
        prob8x4.setText(mathfacts[66]);
        prob8x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[66],null));
        prob8x5.setText(mathfacts[67]);
        prob8x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[67],null));
        prob8x6.setText(mathfacts[68]);
        prob8x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[68],null));
        prob8x7.setText(mathfacts[69]);
        prob8x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[69],null));
        prob8x8.setText(mathfacts[70]);
        prob8x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[70],null));
        prob8x9.setText(mathfacts[71]);
        prob8x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[71],null));

        prob9x1.setText(mathfacts[72]);
        prob9x1.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[72],null));
        prob9x2.setText(mathfacts[73]);
        prob9x2.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[73],null));
        prob9x3.setText(mathfacts[74]);
        prob9x3.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[74],null));
        prob9x4.setText(mathfacts[75]);
        prob9x4.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[75],null));
        prob9x5.setText(mathfacts[76]);
        prob9x5.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[76],null));
        prob9x6.setText(mathfacts[77]);
        prob9x6.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[77],null));
        prob9x7.setText(mathfacts[78]);
        prob9x7.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[78],null));
        prob9x8.setText(mathfacts[79]);
        prob9x8.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[79],null));
        prob9x9.setText(mathfacts[80]);
        prob9x9.setTextColor(ResourcesCompat.getColor(getResources(),mathfactsCorrect[80],null));


        if (function.equals("Addition")){
            topx1.setText("+1");
            topx2.setText("+2");
            topx3.setText("+3");
            topx4.setText("+4");
            topx5.setText("+5");
            topx6.setText("+6");
            topx7.setText("+7");
            topx8.setText("+8");
            topx9.setText("+9");


            sidex1.setText("1");
            sidex2.setText("2");
            sidex3.setText("3");
            sidex4.setText("4");
            sidex5.setText("5");
            sidex6.setText("6");
            sidex7.setText("7");
            sidex8.setText("8");
            sidex9.setText("9");

        }

        if (function.equals("Subtraction")){
            topx1.setText("=1");
            topx2.setText("=2");
            topx3.setText("=3");
            topx4.setText("=4");
            topx5.setText("=5");
            topx6.setText("=6");
            topx7.setText("=7");
            topx8.setText("=8");
            topx9.setText("=9");


            sidex1.setText("-1");
            sidex2.setText("-2");
            sidex3.setText("-3");
            sidex4.setText("-4");
            sidex5.setText("-5");
            sidex6.setText("-6");
            sidex7.setText("-7");
            sidex8.setText("-8");
            sidex9.setText("-9");
        }

        if(function.equals("Multiplication")){
                topx1.setText("x1");
                topx2.setText("x2");
                topx3.setText("x3");
                topx4.setText("x4");
                topx5.setText("x5");
                topx6.setText("x6");
                topx7.setText("x7");
                topx8.setText("x8");
                topx9.setText("x9");


                sidex1.setText("1");
                sidex2.setText("2");
                sidex3.setText("3");
                sidex4.setText("4");
                sidex5.setText("5");
                sidex6.setText("6");
                sidex7.setText("7");
                sidex8.setText("8");
                sidex9.setText("9");

        }

        if(function.equals("Division")){
            topx1.setText("=1");
            topx2.setText("=2");
            topx3.setText("=3");
            topx4.setText("=4");
            topx5.setText("=5");
            topx6.setText("=6");
            topx7.setText("=7");
            topx8.setText("=8");
            topx9.setText("=9");


            sidex1.setText("/1");
            sidex2.setText("/2");
            sidex3.setText("/3");
            sidex4.setText("/4");
            sidex5.setText("/5");
            sidex6.setText("/6");
            sidex7.setText("/7");
            sidex8.setText("/8");
            sidex9.setText("/9");

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String admin = "AdminExists";

        final SharedPreferences settings = getSharedPreferences(admin, MODE_PRIVATE);
        final String adminString = settings.getString(admin,"none");

        if(adminString.equals(studentName)){
            Intent in = new Intent(getApplicationContext(), Progress.class);
            in.putExtra("Name",studentName);
            startActivity(in);
        }else {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            in.putExtra("Name",studentName);
            startActivity(in);
        }

        finish();
    }
}
