package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class civibook extends AppCompatActivity {
    EditText quantity, percost,tcost;
    String qua,userid,id,finalcost;
    Button cal,back,calcu;
    int call;
    FirebaseUser user;
    DatabaseReference reference,reference1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civibook);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userid = user.getUid();
        quantity = (EditText) findViewById(R.id.quan);
        percost = (EditText) findViewById(R.id.rpq);
        percost.setText("40 X Rate Per Quantity");
        tcost = (EditText)findViewById(R.id.tc);
        cal = (Button) findViewById(R.id.byc);
        back = (Button) findViewById(R.id.gth);
        calcu = (Button) findViewById(R.id.calculate);

        calcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String cost = quantity.getText().toString().trim();
               if(!TextUtils.isEmpty(cost)){
                   int number = Integer.parseInt(cost);
                   int totcost = number * 40;
                   finalcost = String.valueOf(totcost);
                   tcost.setText(finalcost);
               }else{
                   quantity.setError("Quantity cannot be empty");
                   quantity.requestFocus();
               }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(civibook.this,civihome.class));
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qua = quantity.getText().toString();
                int ran = randomnumbers(999999,999999999);
                id = String.valueOf(ran);
                String status = "0";
                if(!TextUtils.isEmpty(qua)){
                    booking book = new booking(userid,qua,finalcost,status);
                    FirebaseDatabase.getInstance().getReference("bookings").child(id)
                            .setValue(book)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(civibook.this,civisuccess.class);
                                        intent.putExtra("ids",id);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(civibook.this, "some problem in booking try again....", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(civibook.this,civihome.class));
                                        finish();
                                    }
                                }
                            });
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

    private int randomnumbers(int min, int max){
        return (new Random()).nextInt((max-min)+1)+min;
    }
}