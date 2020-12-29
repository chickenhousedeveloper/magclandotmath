package com.chickenhouse.mathfacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Set.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.sql.DataSource;

public class onlineDBHelper extends AsyncTask<String, Void, String> {

    Context context;


    onlineDBHelper (Context cntxt) {
        context = cntxt;}


    void updateOnlineDBHelper() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException, NoSuchAlgorithmException, KeyManagementException {
        ProviderInstaller.ProviderInstallListener installListener = new ProviderInstaller.ProviderInstallListener(){

            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int i, Intent intent) {

            }
        };

        ProviderInstaller.installIfNeededAsync(context.getApplicationContext(), installListener);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null,null,null);
    }


    @Override
    protected String doInBackground(String... params) {

        String administrator = params[0];

        String url = "https://magclan.tech/magClanMathListUpdate.php";

            try {
                String Email = params[1];


                URL url_conn = new URL(url);


                HttpsURLConnection conn = (HttpsURLConnection)url_conn.openConnection();
                conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");



                OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());

                String post_data = URLEncoder.encode("clmn1_val", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8") + "&" +
                        URLEncoder.encode("clmn2_val", "UTF-8") + "=" + URLEncoder.encode(administrator, "UTF-8");
                        ;

                outputStream.write(post_data);
                outputStream.flush();
                outputStream.close();


                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                conn.disconnect();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }

        return null;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            System.out.println(result);
        }catch (Exception e){
            System.out.println("Apparently the feedback was null");
        }

        try {
            // Disconnect proxy
            System.setProperty("https.proxyHost", null);
        }catch (Exception e){

        }

    }



}
