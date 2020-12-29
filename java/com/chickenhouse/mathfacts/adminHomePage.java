package com.chickenhouse.mathfacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import static com.chickenhouse.mathfacts.fdbkEmail.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE;

/**
 * Created by Jesse on 7/28/2019.
 */

public class adminHomePage extends Activity {

    String adminName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.adminhome);

        Intent input = getIntent();

        adminName = input.getStringExtra("Name");

        final Button usersettings = (Button) findViewById(R.id.removeCreateAccount);
        final Button adminsettings = (Button) findViewById(R.id.AdminSettings);
        final Button studentProgress = (Button) findViewById(R.id.studentProgress);
        final Button fdbk = (Button) findViewById(R.id.feedback);
        final Button purchaseEmails = (Button) findViewById(R.id.purchaseEmails);

        usersettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userSettings = new Intent(getApplicationContext(),newLogin.class);
                userSettings.putExtra("Name",adminName);
                startActivity(userSettings);
                finish();
            }
        });

        adminsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminSettings = new Intent(getApplicationContext(),createAdminActivity.class);
                adminSettings.putExtra("Name",adminName);
                startActivity(adminSettings);
                finish();
            }
        });

        studentProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentprogress = new Intent(getApplicationContext(),Progress.class);
                studentprogress.putExtra("Name",adminName);
                startActivity(studentprogress);
                finish();
            }
        });

        fdbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentprogress = new Intent(getApplicationContext(),fdbkEmail.class);
                studentprogress.putExtra("Name",adminName);
                startActivity(studentprogress);
                finish();
            }
        });

        purchaseEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(getApplicationContext(),InAppPurchaseActivity.class);
                logout.putExtra("Name",adminName);
                startActivity(logout);
                finish();
            }
        });


        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent main = new Intent(getApplicationContext(),Login.class);
        startActivity(main);
        finish();
    }
}
