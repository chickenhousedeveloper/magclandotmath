package com.chickenhouse.mathfacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;

import static android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;

/**
 * Created by Jesse on 7/27/2019.
 */

public class createAdminActivity extends Activity {

    LoginDBHelper loginDB;

    String admin = "AdminExists";
    String pinVal1 = "";
    String pinVal2 = "";

    String status = "";
    String defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
    String defSPD = "10";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.createadmin);

        final SharedPreferences settings = getSharedPreferences(admin, MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        final String adminExists = settings.getString(admin,"none");

        Date today = new Date();

        final Button createAdmin = (Button) findViewById(R.id.createAdmin);
        final EditText adminID = (EditText) findViewById(R.id.adminID);
        final EditText adminEmail = (EditText) findViewById(R.id.adminEmail);
        final EditText newPin = (EditText) findViewById(R.id.newPin);
        final EditText newPinVal = (EditText) findViewById(R.id.newPinValidation);
        final CheckBox overThirteen = (CheckBox) findViewById(R.id.overthirteen);
        CheckBox termscondprivacy = (CheckBox) findViewById(R.id.termsConditionsPrivacy);
        final CheckBox ads_approval = (CheckBox) findViewById(R.id.ads_approval);

        loginDB = new LoginDBHelper(this);

        onlineDBHelper onDBHelper = new onlineDBHelper(this);

        try {
            onDBHelper.updateOnlineDBHelper();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        termscondprivacy.setMovementMethod(LinkMovementMethod.getInstance());


        ArrayList<String> names = new ArrayList();
        ArrayList<String> password = new ArrayList();

        try {
            names = loginDB.availableUsers(names);
        }catch (Exception e){

        }

        try {
            password = loginDB.associatedPins(password);
        }catch(Exception e){

        }

        final ArrayList<String> namesFinal = names;
        final ArrayList<String> namesPassword = password;

        newPinVal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                pinVal1 = newPin.getText().toString();
                pinVal2 = newPinVal.getText().toString();

                if(!pinVal1.equals(pinVal2)){
                    newPinVal.setBackgroundColor(getResources().getColor(R.color.yellow));
                }

                return false;
            }
        });

        createAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int goodAdminLogin = 0;

                final Date today = new Date();

                String enteredName = adminID.getText().toString().toUpperCase();
                String enteredEmail = adminEmail.getText().toString().toLowerCase();
                String enteredPin1 = newPin.getText().toString();
                String enteredPin2 = newPinVal.getText().toString();

                CheckBox overThirteen = (CheckBox) findViewById(R.id.overthirteen);
                CheckBox termscondprivacy = (CheckBox) findViewById(R.id.termsConditionsPrivacy);

                if(!overThirteen.isChecked()){
                    overThirteen.setBackgroundColor(getResources().getColor(R.color.yellow));
                    Toast.makeText(getApplicationContext(),"Administrator must be over 18 years old. Please talk to a parent, guardian or teacher to setup the app.", Toast.LENGTH_SHORT).show();
                }else{
                    goodAdminLogin = goodAdminLogin + 1;
                    overThirteen.setBackgroundColor(getResources().getColor(R.color.white));
                }

                if(!termscondprivacy.isChecked()){
                    termscondprivacy.setBackgroundColor(getResources().getColor(R.color.yellow));
                    Toast.makeText(getApplicationContext(),"In order to use this app (or allow others under your care to use it), you must agree to the terms of service and the privacy policy for yourself and those under your care.", Toast.LENGTH_SHORT).show();
                }else{
                    goodAdminLogin = goodAdminLogin + 1;
                    termscondprivacy.setBackgroundColor(getResources().getColor(R.color.white));
                }

                if(enteredName.length()<3){
                    adminID.setBackgroundColor(getResources().getColor(R.color.yellow));
                    Toast.makeText(getApplicationContext(),"Administrator ID must be 3 or more characters", Toast.LENGTH_SHORT).show();
                }else{
                    goodAdminLogin = goodAdminLogin + 1;
                    adminID.setBackgroundColor(getResources().getColor(R.color.white));
                }

                if(enteredEmail.contains("@") & enteredEmail.length()>7){
                    goodAdminLogin = goodAdminLogin + 1;
                    adminEmail.setBackgroundColor(getResources().getColor(R.color.white));
                }else{
                    adminEmail.setBackgroundColor(getResources().getColor(R.color.yellow));
                    Toast.makeText(getApplicationContext(),"Administrator Account Requires a Genuine Email Account", Toast.LENGTH_SHORT).show();
                }

                if(!enteredPin1.equals(enteredPin2)){
                    newPin.setBackgroundColor(getResources().getColor(R.color.yellow));
                    newPinVal.setBackgroundColor(getResources().getColor(R.color.yellow));

                    Toast.makeText(getApplicationContext(),"PIN ENTRIES DO NOT MATCH", Toast.LENGTH_SHORT).show();
                }else{
                    goodAdminLogin = goodAdminLogin + 1;
                    newPin.setBackgroundColor(getResources().getColor(R.color.white));
                    newPinVal.setBackgroundColor(getResources().getColor(R.color.white));
                }

                if(goodAdminLogin>=5){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String dateToStr = format.format(today);
                    String approval = String.valueOf(ads_approval.isChecked());

                    if(!namesFinal.contains(enteredName)){
                        loginDB.createNewUser(enteredName, enteredPin1, enteredEmail, dateToStr, defSPD ,defSelection);
                        onDBHelper.execute(approval, enteredEmail);
                        createAdmin.setEnabled(false);
                        status = "Admin Account Created";
                    }else{
                        loginDB.updateUserPin(enteredName,enteredPin1,enteredEmail,dateToStr);
                        onDBHelper.execute(approval,enteredEmail);
                        createAdmin.setEnabled(false);
                        status = "Admin Account Updated";
                    }

                   // loginDB.createNewUser(enteredName, enteredPin1, enteredEmail, dateToStr);
                    editor.putString(admin,enteredName);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),status + ": Admin rights assigned to " + enteredName, Toast.LENGTH_SHORT).show();


                    if(status.equals("Admin Account Created")){

                        // Send user to Admin Home page
                        Intent intent = new Intent(getApplicationContext(), newLogin.class);
                        intent.putExtra("Name", enteredName);
                        startActivity(intent);
                        finish();

                    } else {
                        // Send user to Admin Home page
                        Intent intent = new Intent(getApplicationContext(), adminHomePage.class);
                        intent.putExtra("Name", enteredName);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        });

        // Set up summary database

        summaryDBHelper sumDB = new summaryDBHelper(this);

        if(!sumDB.exists()){
            sumDB.insertSummary("EmailAllotment","none",-1,-1,-1,-1,-1,25,"999");
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent info = getIntent();
        final String studentName = info.getStringExtra("Name");


        if(studentName == null){
            Intent main = new Intent(getApplicationContext(),createAdminActivity.class);
            main.putExtra("Name",studentName);
            System.out.println("Nope Stay Here! " + studentName + " you must create an admin account!");
            startActivity(main);
            finish();
        }else {
            Intent main = new Intent(getApplicationContext(), adminHomePage.class);
            main.putExtra("Name",studentName);
            System.out.println("Thank you " + studentName + " you are free to go.");
            startActivity(main);
            finish();
        }
    }

}
