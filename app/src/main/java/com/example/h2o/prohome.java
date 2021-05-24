package com.example.h2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class prohome extends AppCompatActivity {
    Button one,two,three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prohome);


        one = (Button) findViewById(R.id.cta11);
        two = (Button) findViewById(R.id.cta22);
        three = (Button) findViewById(R.id.cta33);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(prohome.this,proopenorders.class));
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(prohome.this,proconorders.class));
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(prohome.this,MainActivity.class));
                finish();
            }
        });
    }
}