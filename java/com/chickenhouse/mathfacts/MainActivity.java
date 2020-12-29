package com.chickenhouse.mathfacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

        public String studentName = "GUEST";
       // boolean magClanAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        Intent input = getIntent();
        try {
            studentName = input.getStringExtra("Name");
        }catch (Exception e){

        }
        if(studentName == null){
            studentName = "GUEST";
        }

        final Button add = (Button) findViewById(R.id.Addition);
        final Button subtract = (Button) findViewById(R.id.Subtraction);
        final Button multiply = (Button) findViewById(R.id.Multiplication);
        final Button division = (Button) findViewById(R.id.Division);
        final Button login = (Button) findViewById(R.id.login);

        String defaultName = "GUEST";

        System.out.println("studentName = " + studentName);
        if(defaultName.equals(studentName)){
            login.setText("SEND RESULTS");
            System.out.println("equals default");
        }else{
            System.out.println("equals new login");
            login.setText("SEND RESULTS");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getApplicationContext(),Addition.class);
                add.putExtra("Name",studentName);
                startActivity(add);
                finish();
            }
            });

        subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sub = new Intent(getApplicationContext(),Subtraction.class);
                    sub.putExtra("Name",studentName);
                    startActivity(sub);
                    finish();
                }
                });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mult = new Intent(getApplicationContext(),Multiplication.class);
                mult.putExtra("Name",studentName);
                startActivity(mult);
                finish();
            }
        });

        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent div = new Intent(getApplicationContext(),Division.class);
                div.putExtra("Name",studentName);
                startActivity(div);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendEmail = new Intent(getApplicationContext(), sendEmail.class);
                sendEmail.putExtra("Name",studentName);
                startActivity(sendEmail);
                finish();
            }
        });
}

    @Override
    public void onBackPressed() {
        Intent log = new Intent(getApplicationContext(),Login.class);
        log.putExtra("Name",studentName);
        startActivity(log);
        finish();
    }

   /* private boolean magClanAvailable(){
        try{
            URL url = new URL("https://magclan.tech");
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());

            conn.setConnectTimeout(3000);
            conn.setReadTimeout(4000);
            conn.connect();

            return(conn.getResponseCode()==200); // connected to magclan.tech
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // no connectivity
    }

    */

}
