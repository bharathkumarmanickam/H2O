package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class register extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Spinner myspinner;
    String text;
    EditText fname,mob,mobto,add,mail,pass,conpas;
    Button register;
    TextView login;
    FirebaseAuth mauth;
    String full,mobile,mobiletwo,address,email,paswd,conpaswd,type;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myspinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setOnItemClickListener(this);

        builder = new AlertDialog.Builder(register.this);
        builder.setCancelable(true);
        builder.setTitle("H2O");
        mauth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.name);
        mob = (EditText) findViewById(R.id.mobone);
        mobto = (EditText) findViewById(R.id.mobtwo);
        add = (EditText) findViewById(R.id.address);
        mail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        conpas = (EditText) findViewById(R.id.conpass);
        register = (Button) findViewById(R.id.registerbut);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(register.this);
                progressDialog.setMessage("Registerin client Please wait.....");
                progressDialog.setCancelable(false);
                progressDialog.setTitle("H2O");
                progressDialog.show();
                full = fname.getText().toString().trim();
                mobile = mob.getText().toString().trim();
                mobiletwo = mobto.getText().toString().trim();
                address = add.getText().toString().trim();
                email = mail.getText().toString().trim();
                paswd = pass.getText().toString().trim();
                conpaswd = conpas.getText().toString().trim();
                type = text;

                if(!TextUtils.isEmpty(full)){
                    if(!TextUtils.isEmpty(mobile)){
                        if(!TextUtils.isEmpty(mobiletwo)){
                            if(!TextUtils.isEmpty(address)){
                                if(!TextUtils.isEmpty(email)){
                                    if(!TextUtils.isEmpty(paswd)){
                                        if(!TextUtils.isEmpty(conpaswd)){
                                            if(paswd == conpaswd){
                                                mauth.fetchSignInMethodsForEmail(email)
                                                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                                boolean check = !task.getResult().getSignInMethods().isEmpty();
                                                                if(!check){

                                                                    mauth.createUserWithEmailAndPassword(email,paswd)
                                                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        details det = new details(full,mobile,mobiletwo,address,email,paswd,conpaswd);
                                                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                                .setValue(det).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful()){
                                                                                                    Intent intent = new Intent(register.this, MainActivity.class);
                                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                    startActivity(intent);
                                                                                                }
                                                                                                else{
                                                                                                    if(progressDialog.isShowing()){
                                                                                                        progressDialog.dismiss();
                                                                                                    }
                                                                                                    builder.setMessage("Please Try Again...");
                                                                                                    builder.show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }else{
                                                                                        builder.setMessage("Please Try Again...");
                                                                                        builder.show();
                                                                                    }
                                                                                }
                                                                            });

                                                                }else{
                                                                    Toast.makeText(register.this,"Email already exists",Toast.LENGTH_SHORT).show();
                                                                    mail.setError("Email already exists");
                                                                    mail.requestFocus();
                                                                    return;

                                                                }
                                                            }
                                                        });
                                            }else{
                                                pass.setError("password and confirm password should be same");
                                                pass.requestFocus();
                                                conpas.requestFocus();
                                            }
                                        }else{
                                            conpas.setError("Confirm password cannot be empty");
                                            conpas.requestFocus();
                                        }
                                    }else{
                                        pass.setError("Password cannot be empty");
                                        pass.requestFocus();
                                    }
                                }else{
                                    mail.setError("Mail Id cannot be empty");
                                    mail.requestFocus();
                                }
                            }else{
                                add.setError("Address cannot be empty");
                                add.requestFocus();
                            }
                        }else{
                             mobto.setError("Alternate number cannot be empty");
                             mobto.requestFocus();
                        }
                    }else{
                        mob.setError("Mobile Number Cannot be Empty");
                        mob.requestFocus();
                    }
                }else{
                    fname.setError("Full name cannot be empty");
                    fname.requestFocus();
                }
            }
        });
        login = (TextView) findViewById(R.id.logincta);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        Toast.makeText(register.this,text,Toast.LENGTH_SHORT).show();
    }

}