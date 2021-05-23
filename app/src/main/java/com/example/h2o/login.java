package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText uname,upass;
    Button butauth;
    TextView nu,ft;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    AlertDialog.Builder builder;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type = getIntent().getStringExtra("type");
        uname = (EditText) findViewById(R.id.username);
        upass = (EditText) findViewById(R.id.password);
        butauth = (Button) findViewById(R.id.button);
        nu = (TextView) findViewById(R.id.newuser);
        ft = (TextView) findViewById(R.id.forgot);
        builder = new AlertDialog.Builder(login.this);
        builder.setCancelable(true);
        builder.setTitle("H2O");
        ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                want to create forgot
            }
        });
    nu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(login.this,register.class));
            finish();
        }
    });

    butauth.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = uname.getText().toString().trim();
            String password = upass.getText().toString().trim();
            if(!TextUtils.isEmpty(username) && Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                if (!TextUtils.isEmpty(password)) {
                    progressDialog = new ProgressDialog(login.this);
                    progressDialog.setMessage("Please wait while logging in.......");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(username,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    if (task.isSuccessful()) {
                                            if(type == "0"){
                                                startActivity(new Intent(login.this, civihome.class));
                                                finish();
                                            }else{
                                                startActivity(new Intent(login.this, prohome.class));
                                                finish();
                                            }

                                    } else {
                                        builder.setMessage("Incorrect username or password....Try again");
                                        builder.show();
                                    }
                                }
                            });
                } else {
                    upass.setError("Password cannot be empty");
                    upass.requestFocus();
                    return;
                }
            }else{
                uname.setError("mail id cannot be empty and should be valid email address");
                uname.requestFocus();
                return;
            }
        }
    });
    }

}