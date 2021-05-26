package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class civiorders extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    DatabaseReference mref;
    DatabaseReference databaseReference,reference,ref1;
    ProgressDialog progressDialog;
    ListView lv;
    String userid,bookid;
    FirebaseUser user;
    String qid,tc,oid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civiorders);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait Fetching Details........");
        progressDialog.setCancelable(false);
        progressDialog.show();

        lv = (ListView) findViewById(R.id.orderscheck);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(civiorders.this,bookingwatch.class);
                String  itemValue    = (String) lv.getItemAtPosition(position);
                intent.putExtra("bookid",itemValue);
                startActivity(intent);
                finish();


            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                oid = snapshot.child("userid").getValue().toString();
                if(oid.equals(userid)){
                    qid = snapshot.child("quantity").getValue().toString();
                    tc = snapshot.child("tc").getValue().toString();
                    bookid = snapshot.getKey();

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            arrayList.add(bookid);

                            adapter.notifyDataSetChanged();
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
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
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
}