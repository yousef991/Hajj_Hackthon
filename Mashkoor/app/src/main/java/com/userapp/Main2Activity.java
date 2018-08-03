package com.userapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button but1 = (Button) findViewById(R.id.uBtnns);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DocAct.class);
                view.getContext().startActivity(intent);}
        });

        Button but2 = (Button) findViewById(R.id.uBtnns2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DocAct.class);
                view.getContext().startActivity(intent);}
        });

        Button but3 = (Button) findViewById(R.id.uBtnns3);
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DocAct.class);
                view.getContext().startActivity(intent);}
        });
    }
}
