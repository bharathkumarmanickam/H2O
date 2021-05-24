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

import java.util.regex.Pattern;

public class register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner myspinner;
    String text;
    EditText fname,mob,mobto,add,email,ppass,conpas;
    Button register;
    TextView login;
    FirebaseAuth mauth;
    String full,mobile,mobiletwo,address,mail,pass,conpaswd,type;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myspinner = (Spinner) findViewById(R.id.spinner1);
        myspinner.setOnItemSelectedListener(register.this);

        builder = new AlertDialog.Builder(register.this);
        builder.setCancelable(true);
        builder.setTitle("H2O");
        mauth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.name);
        mob = (EditText) findViewById(R.id.mobone);
        mobto = (EditText) findViewById(R.id.mobtwo);
        add = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        ppass = (EditText) findViewById(R.id.pass);
        conpas = (EditText) findViewById(R.id.conpass);
        register = (Button) findViewById(R.id.registerbut);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full = fname.getText().toString().trim();
                mobile = mob.getText().toString().trim();
                mobiletwo = mobto.getText().toString().trim();
                address = add.getText().toString().trim();
                mail = email.getText().toString().trim();
                pass = ppass.getText().toString().trim();
                conpaswd = conpas.getText().toString().trim();
                type = text;

                if(!TextUtils.isEmpty(full)){
                    if(!TextUtils.isEmpty(mobile)){
                        if(!TextUtils.isEmpty(mobiletwo)){
                            if(!TextUtils.isEmpty(address)){
                                if(!TextUtils.isEmpty(mail)){
                                    if(!TextUtils.isEmpty(pass)){
                                        if(!TextUtils.isEmpty(conpaswd)){
                                            if(pass.equals(conpaswd)){
                                                if(PASSWORD_PATTERN.matcher(pass).matches()) {
                                                    progressDialog = new ProgressDialog(register.this);
                                                    progressDialog.setMessage("Registerin client Please wait.....");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.setTitle("H2O");
                                                    progressDialog.show();

                                                    mauth.createUserWithEmailAndPassword(mail, pass)
                                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        details det = new details(full, mobile, mobiletwo, address, mail, pass, conpaswd, type);
                                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                .setValue(det).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Intent intent = new Intent(register.this, MainActivity.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                } else {
                                                                                    if (progressDialog.isShowing()) {
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                    builder.setMessage("Please ReTry Again...");
                                                                                    builder.show();
                                                                                }
                                                                            }
                                                                        });

                                                                    } else {
                                                                        if (progressDialog.isShowing()) {
                                                                            progressDialog.dismiss();
                                                                        }
                                                                        builder.setMessage("Please Try Again...");
                                                                        builder.show();
                                                                    }
                                                                }
                                                            });
                                                }else{
                                                    ppass.setError("Password should contain one upper case and one lower case and one specail character and one number");
                                                    ppass.requestFocus();
                                                }

                                            }else{
                                                ppass.setError("password and confirm password should be same");
                                                ppass.requestFocus();
                                                conpas.requestFocus();
                                            }
                                        }else{
                                            conpas.setError("Confirm password cannot be empty");
                                            conpas.requestFocus();
                                        }
                                    }else{
                                        ppass.setError("Password cannot be empty");
                                        ppass.requestFocus();
                                    }
                                }else{
                                    email.setError("Mail Id cannot be empty");
                                    email.requestFocus();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getSelectedItem().toString();
        Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}