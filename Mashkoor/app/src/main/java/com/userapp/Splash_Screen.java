package com.userapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash_Screen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.splash__screen_layout);
        Thread thread = new Thread() {

            @Override
            public void run() {

                try {

                    sleep(3000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                } finally {


                    Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
                super.run();
            }
        };
        thread.start();
    }

}
