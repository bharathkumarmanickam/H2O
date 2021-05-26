package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class proconorders extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ListView lv;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String userid,bookid,key,veri,tempbookid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proconorders);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait fetching your booked orders");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid();
        lv = (ListView) findViewById(R.id.listview);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count= 0;
                String  itemValue    = (String) lv.getItemAtPosition(position);
                String[] lines = itemValue.split("\\r?\\n");
                for (String line : lines) {
                    if(count == 0){

                    }else{
                        tempbookid = line;
                    }
                    count++;
                }
                Intent intent = new Intent(proconorders.this,proshowdet.class);
                intent.putExtra("boid",tempbookid);
                intent.putExtra("prokey",userid);
                startActivity(intent);
                finish();

                       }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("confirm");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                key = snapshot.child("logkey").getValue().toString();
                veri = snapshot.child("veri").getValue().toString();
                if(userid.equals(key)){
                    bookid = snapshot.getKey();
                    adapter.add("Order No : \n"+bookid);
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