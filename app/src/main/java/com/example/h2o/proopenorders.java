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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class proopenorders extends AppCompatActivity {

    FirebaseAuth auth;
    ListView list;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    String userid,qua,tc,sta,bookid;
    String tempbookid,tempuserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proopenorders);
        list = (ListView) findViewById(R.id.openorders);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait Fetching open orders........");
        progressDialog.setCancelable(false);
        progressDialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count= 0;
                String  itemValue    = (String) list.getItemAtPosition(position);
                String[] lines = itemValue.split("\\r?\\n");
                for (String line : lines) {
                    if(count == 0){

                    }else if (count == 1){
                        tempbookid = line;
                    }else{
                        tempuserid = line;
                    }
                    count++;
                }
                Intent intent = new Intent(proopenorders.this,proconfirm.class);
                intent.putExtra("userkey",tempuserid);
                intent.putExtra("id",tempbookid);
                startActivity(intent);
                finish();


            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        list.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               sta = snapshot.child("status").getValue().toString();
               if(sta.equals("0")){
                   userid = snapshot.child("userid").getValue().toString();
                   qua = snapshot.child("quantity").getValue().toString();
                   bookid = snapshot.getKey();
                   adapter.add(qua + " Required \n"+bookid+"\n"+userid);
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

            }
        });



    }
}