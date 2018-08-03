package com.userapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.userapp.com.userapp.utility.Singleton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UDeatils extends Activity {


    private UserFirbase d;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_layout);
        init();
    }

    private void init() {

        TextView pName = (TextView) findViewById(R.id.pName);
        TextView pID = (TextView) findViewById(R.id.pID);
        TextView pdate = (TextView) findViewById(R.id.pdate);
        TextView pLoc = (TextView) findViewById(R.id.pL);

        Button senNO = (Button) findViewById(R.id.senNO);
        senNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncSNO().execute();
            }
        });

        Button b = (Button) findViewById(R.id.gnacnuBTN);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            Gson gson = new Gson();
            String f = getIntent().getExtras().getString("obj");
            d = gson.fromJson(f, UserFirbase.class);
            pName.setText(d.getNameIDString());
            pID.setText(d.getIdString());
            pdate.setText(d.getCurrentDateTime());
            pLoc.setText(d.getdL());
        }//


    }


    class AsyncSNO extends AsyncTask<Void, Void, Void>

    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sNo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final SweetAlertDialog pDialog = new SweetAlertDialog(UDeatils.this,
                    SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText("Successful");
            pDialog.setContentText("Sent Successfully");
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    pDialog.dismiss();
                    finish();
                }
            });
        }
    }

    private void sNo() throws Exception {

        JSONObject json = new JSONObject();

        json.put("to", d.getToken());
        JSONObject info = new JSONObject();
        info.put("title", "notification title"); // Notification title
        info.put("body", "message body"); // Notification
        // body
        json.put("notification", info);

        // Create connection to send GCM Message request.
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "key=AIzaSyA_ZKlVSRgfEHSFdMfTIn3XaZyI2TkEEWo");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Send GCM message content.
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(json.toString().getBytes());

        // Read GCM response.
        InputStream inputStream = conn.getInputStream();
        String resp = convertStreamToString(inputStream);
        System.out.println(resp);
        System.out.println("Check your device/emulator for notification or logcat for " +
                "confirmation of the receipt of the GCM message.");
    }//

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().trim();
    }
}
