package com.example.drmagdy.helthy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        Thread thread = new Thread() {

            @Override
            public void run() {

                try {

                    sleep(3000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                } finally {


                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();


                }
                super.run();
            }
        };
        thread.start();
    }
}
