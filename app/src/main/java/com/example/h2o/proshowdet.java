package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class proshowdet extends AppCompatActivity {
    String bookid,prokey,userid,mobileno,veri,verii;
    TextView fname,fadd,fmobile,fquan,fcost;
    Button call,com;
    DatabaseReference databaseReference,databaseReference2;
    ProgressDialog progressDialog;
    ImageView comp1;
    TextView comp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proshowdet);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching client details");
        progressDialog.setCancelable(false);
        progressDialog.show();
        bookid = getIntent().getStringExtra("boid");
        prokey = getIntent().getStringExtra("prokey");
        veri = getIntent().getStringExtra("veri");
        fname = (TextView) findViewById(R.id.name);
        fadd = (TextView) findViewById(R.id.address);
        fmobile = (TextView) findViewById(R.id.mob);
        fquan = (TextView) findViewById(R.id.quan);
        fcost = (TextView) findViewById(R.id.totcost);
        call = (Button) findViewById(R.id.call);
        com = (Button) findViewById(R.id.co);
        comp1 = (ImageView) findViewById(R.id.comp1);
        comp2 = (TextView) findViewById(R.id.comp2);
        if(veri.equals("1")){
            call.setVisibility(INVISIBLE);
            com.setVisibility(INVISIBLE);
            comp1.setVisibility(VISIBLE);
            comp2.setVisibility(VISIBLE);
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+mobileno));
                startActivity(callIntent);
            }
        });

        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatetask();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                if(key.equals(bookid)){
                    userid = snapshot.child("userid").getValue().toString();
                    String toquan = snapshot.child("quantity").getValue().toString();
                    String tcost = snapshot.child("tc").getValue().toString();
                    fquan.setText(toquan);
                    fcost.setText(tcost);
                    grabvalues();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void updatetask() {
         verii = "1";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to close the request");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                details det = new details(prokey,verii);
                FirebaseDatabase.getInstance().getReference("confirm").child(bookid)
                        .setValue(det)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(proshowdet.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(proshowdet.this,proconorders.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(proshowdet.this, "Failures Retry again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    private void grabvalues() {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String mainid = snapshot.getKey();
                if(mainid.equals(userid)){
                        String name = snapshot.child("full").getValue().toString();
                        String add = snapshot.child("address").getValue().toString();
                        mobileno = snapshot.child("mobile").getValue().toString();
                        fname.setText(name);
                        fadd.setText(add);
                        fmobile.setText(mobileno);
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(proshowdet.this,proconorders.class));
        finish();
    }
}