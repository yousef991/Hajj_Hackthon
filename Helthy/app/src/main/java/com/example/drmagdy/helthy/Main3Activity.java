package com.example.drmagdy.helthy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;


public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.but2);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivityForResult(new Intent(Main3Activity.this,Main4Activity.class), 0);
            }
        });

        RelativeLayout relativeclic2 =(RelativeLayout)findViewById(R.id.but3);
        relativeclic2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivityForResult(new Intent(Main3Activity.this,Main5Activity.class), 0);
            }
        });

        RelativeLayout relativeclic3 =(RelativeLayout)findViewById(R.id.but4);
        relativeclic3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivityForResult(new Intent(Main3Activity.this,Main6Activity.class), 0);
            }
        });

    }
}