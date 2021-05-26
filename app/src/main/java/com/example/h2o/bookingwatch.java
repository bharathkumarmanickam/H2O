package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class bookingwatch extends AppCompatActivity {
    TextView fquan,fcost,status;
    Button call,gb;
    String cost,quan,bookid,uid,number,id;
    DatabaseReference databaseReference,ref2,ref3,ref4;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingwatch);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for Provider Details.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        bookid = getIntent().getStringExtra("bookid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        fquan = (TextView) findViewById(R.id.quan);
        fcost = (TextView) findViewById(R.id.totcost);

        ref4 = FirebaseDatabase.getInstance().getReference("bookings");
        ref4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String bid = snapshot.getKey();
                if(bid.equals(bookid)){
                    cost = snapshot.child("tc").getValue().toString();
                    quan = snapshot.child("quantity").getValue().toString();
                    fquan.setText(quan);
                    fcost.setText(cost);
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



        status = (TextView) findViewById(R.id.status);
        call = (Button) findViewById(R.id.callp);
        gb = (Button) findViewById(R.id.gb);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
        });

        gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bookingwatch.this,civiorders.class));
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String boid = snapshot.getKey();
                if(boid.equals(bookid)){
                    String sta = snapshot.child("status").getValue().toString();
                    if(sta.equals("1")){
                        ref2 = FirebaseDatabase.getInstance().getReference("confirm");
                        ref2.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String bid = snapshot.getKey();
                                if(bid.equals(bookid)){
                                    id = snapshot.child("logkey").getValue().toString();
                                    ref3 = FirebaseDatabase.getInstance().getReference("Users");
                                    ref3.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            String cid = snapshot.getKey();
                                            if(cid.equals(id)){
                                                number = snapshot.child("mobile").getValue().toString();
                                                call.setVisibility(View.VISIBLE);
                                                status.setText("ASSIGNED");
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





                    }else{
                        call.setVisibility(View.INVISIBLE);
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
    if(progressDialog.isShowing()){
        progressDialog.dismiss();
    }
    }
}