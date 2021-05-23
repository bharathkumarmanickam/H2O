package com.example.h2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class civibook extends AppCompatActivity {
    EditText quantity, percost,tcost;
    String qua;
    Button cal,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civibook);
        quantity = (EditText) findViewById(R.id.quan);
        percost = (EditText) findViewById(R.id.rpq);
        tcost = (EditText)findViewById(R.id.tc);
        cal = (Button) findViewById(R.id.byc);
        back = (Button) findViewById(R.id.gth);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qua = quantity.getText().toString();
                if(!TextUtils.isEmpty(qua)){

                }else{
                    quantity.setError("Quantity cannot be emepty");
                    quantity.requestFocus();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(civibook.this,civihome.class));
                finish();
            }
        });
    }
}