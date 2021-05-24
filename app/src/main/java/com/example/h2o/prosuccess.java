package com.example.h2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class prosuccess extends AppCompatActivity {
    TextView bid;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prosuccess);
        bid = (TextView) findViewById(R.id.bookid);
        id = getIntent().getStringExtra("bid");
        bid.setText(id);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(prosuccess.this,proopenorders.class));
        finish();
    }
}