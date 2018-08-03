package com.example.drmagdy.helthy;

import android.content.Intent;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class Main2Activity extends AppCompatActivity {


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
        setContentView(R.layout.activity_main2);
        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.but);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivityForResult(new Intent(Main2Activity.this,Main3Activity.class), 0);
            }
        });

    }

}
