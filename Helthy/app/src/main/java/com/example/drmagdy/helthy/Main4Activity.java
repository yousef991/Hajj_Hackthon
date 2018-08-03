package com.example.drmagdy.helthy;


import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Random;

public class Main4Activity extends AppCompatActivity{


    Button btnGetLastLocation;
    TextView textLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btnGetLastLocation = (Button) findViewById(R.id.getlastlocation);
        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Dexter.withActivity(Main4Activity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();
                textLastLocation = (TextView) findViewById(R.id.lastlocation);
                Random d = new Random();
                int R = d.nextInt(5169);
                int V = d.nextInt(2192);
                textLastLocation.setText("Latitude: 21."+R+" , Longitude : 39."+V);
            }
        });
    }
}
