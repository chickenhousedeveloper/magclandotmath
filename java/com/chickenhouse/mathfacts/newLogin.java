package com.chickenhouse.mathfacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.chickenhouse.mathfacts.fdbkEmail.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE;

/**
 * Created by Jesse on 4/14/2018.
 */

public class newLogin extends Activity {

    LoginDBHelper loginDB;

    public String studentName = "GUEST";
    String email = "PreferredEmail";
    String defaultSpeed = "10";
    String defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlogin);

        final Date today = new Date();

        final SharedPreferences settings = getSharedPreferences(email, MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        final String adminExists = settings.getString(email,null);

        final Button createLogin = (Button) findViewById(R.id.createLogin);
        final EditText newName = (EditText) findViewById(R.id.newName);
        final EditText newPin = (EditText) findViewById(R.id.newPin);
        final EditText newPinVal = (EditText) findViewById(R.id.newPinValidation);
        final EditText newAddress = (EditText) findViewById(R.id.newEmail);
        final CheckBox ads_approval = (CheckBox) findViewById(R.id.ads_approval);


        Intent input = getIntent();

        try {
            studentName = input.getStringExtra("Name");
        } catch (Exception e) {
            studentName = "GUEST";
        }

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

        final ArrayList<String> finalPassword = password;
        final ArrayList<String> finalNames = names;

        newAddress.setText(settings.getString(email,null));

        // CREATE NEW LOGIN
        createLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String newStudent = newName.getText().toString().toUpperCase();
                String newPassword = newPin.getText().toString();
                String newPasswordVal = newPinVal.getText().toString();
                String newEmailAddress = newAddress.getText().toString();


                if (!newEmailAddress.equals(settings.getString(email,null))) {
                    editor.putString(email, newEmailAddress);
                    editor.commit();
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = format.format(today);

                int createUpdate = 0;

                if(newPassword.equals(newPasswordVal)){
                    createUpdate = createUpdate + 1;
                }else{
                    Toast.makeText(getApplicationContext(), "Your pins do not match.", Toast.LENGTH_SHORT).show();
                }

                if(newEmailAddress.length()>5){
                    createUpdate = createUpdate + 1;
                }else{
                    Toast.makeText(getApplicationContext(), "A valid email is necessary.", Toast.LENGTH_SHORT).show();
                }

                if(createUpdate>1) {

                    String approval = String.valueOf(ads_approval.isChecked());

                    if (!finalNames.contains(newStudent)) {
                        loginDB.createNewUser(newStudent, newPassword, newEmailAddress, dateToStr, defaultSpeed, defSelection);
                        onDBHelper.execute(approval,newEmailAddress);
                        createLogin.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Welcome " + newStudent.toUpperCase(), Toast.LENGTH_SHORT).show();

                    } else {

                        if(loginDB.getDefaultSpeedByName(new String[]{studentName}).equals(defaultSpeed)) {

                            loginDB.updateUserPin(newStudent, newPassword, newEmailAddress, dateToStr);
                            onDBHelper.execute(approval,newEmailAddress);
                            createLogin.setEnabled(false);
                            Toast.makeText(getApplicationContext(), newStudent.toUpperCase() + " your pin is updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            loginDB.updateUserPinandSpeed(newStudent, newPassword, newEmailAddress, dateToStr, defaultSpeed);
                            onDBHelper.execute(approval,newEmailAddress);
                            createLogin.setEnabled(false);
                            Toast.makeText(getApplicationContext(), newStudent.toUpperCase() + " your pin and default speed are updated.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    Intent in = new Intent(getApplicationContext(), adminHomePage.class);
                    in.putExtra("Name",studentName);
                    startActivity(in);

                    finish();
                }

                newName.setText("");
                newPin.setText("");
                newPinVal.setText("");
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

            Intent in = new Intent(getApplicationContext(), adminHomePage.class);
            in.putExtra("Name",studentName);
            startActivity(in);

        finish();
    }


}
