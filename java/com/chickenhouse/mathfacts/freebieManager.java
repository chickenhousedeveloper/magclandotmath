package com.chickenhouse.mathfacts;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class freebieManager extends AsyncTask<String, Void, String> {

    Context context;
    summaryDBHelper sumDB;



    freebieManager (Context cntxt) {
        context = cntxt;
    }


    void update() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException, NoSuchAlgorithmException, KeyManagementException {
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
    protected String doInBackground(String... strings) {

        sumDB = new summaryDBHelper(context);

        String url = "https://magclan.tech/accept-gift.php";

        try {
            String Email = strings[0];


            URL url_conn = new URL(url);


            HttpsURLConnection conn = (HttpsURLConnection)url_conn.openConnection();
            conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");



            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());

            String post_data = URLEncoder.encode("clmn1_val", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");
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
            //System.out.println("just resutls" + result);
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
            System.out.println(Integer.valueOf(result));
        } catch (Exception e) {
            System.out.println("Looks like the result in null...");
        }

        try {

            if(!result.isEmpty()  & Integer.valueOf(result)>0) {

                try {
                sumDB.getWritableDatabase();

                // How many emails are currently available to the admin
                int numEmailRemaining = sumDB.getNumOfEmailsRemain();

                // Number of Emails gifted to the user
                int giftEmails = Integer.valueOf(result);

                sumDB.insertSummary("EmailAllotment", "none", -1, -1, -1, -1, -1, giftEmails + numEmailRemaining, "999");
                sumDB.close();
                } catch (Exception e) {
                    System.out.println("Number of Emails Remaining Not populated!!");
                }

            }else{
                System.out.println("No Gift Emails");
            }

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
