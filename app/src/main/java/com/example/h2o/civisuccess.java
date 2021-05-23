package com.example.h2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class civisuccess extends AppCompatActivity {
    TextView id;
    String idget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civisuccess);
        idget = getIntent().getStringExtra("id").toString();
        id.setText(idget);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(civisuccess.this,civihome.class));
    }
}