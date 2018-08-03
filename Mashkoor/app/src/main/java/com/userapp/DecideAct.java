package com.userapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.userapp.com.userapp.utility.LocationTracker;
import com.userapp.com.userapp.utility.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecideAct extends Activity {

    private LocationTracker tracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.dcde_layout);


        RelativeLayout uBtn = (RelativeLayout) findViewById(R.id.uBtn);
        uBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideAct.this, UserAct.class);
                startActivity(intent);
//        customType(MainActivity.this, getString(R.string.left_to_right));
            }
        });
        RelativeLayout docBtn = (RelativeLayout) findViewById(R.id.docBtn);
        docBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideAct.this, DocAct.class);
                startActivity(intent);
//        customType(MainActivity.this, getString(R.string.left_to_right));
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onStart() {
        super.onStart();

        Singleton.token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(
                getResources().getString(R.string.default_notification_channel_name));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT > 22) {
            checkAndRequestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Singleton.REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    } else {


                        if (ActivityCompat.shouldShowRequestPermissionRationale(DecideAct.this,
                                android.Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(DecideAct.this,
                                android.Manifest.permission.WRITE_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(DecideAct.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(DecideAct.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(DecideAct.this,
                                android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                            checkAndRequestPermissions();
                        } else {
                            //~
                        }


                        try {

                            if (Singleton.getOurInstance().isNetworkAvailable(DecideAct.this))
                                tracker = new LocationTracker(DecideAct.this, DecideAct.this);
                        } catch (Exception e) {
                            e.toString();
                        }
                    }
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    public boolean checkAndRequestPermissions() {

        int re_Ex = ContextCompat.checkSelfPermission(DecideAct.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

        int wr_Ex = ContextCompat.checkSelfPermission(DecideAct.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        //

        int acc_c = ContextCompat.checkSelfPermission(DecideAct.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        int acc_F = ContextCompat.checkSelfPermission(DecideAct.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int acc_N = ContextCompat.checkSelfPermission(DecideAct.this,
                android.Manifest.permission.ACCESS_NETWORK_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (re_Ex != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (wr_Ex != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (acc_c != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (acc_F != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (acc_N != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(DecideAct.this, listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]), Singleton.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }//
}
