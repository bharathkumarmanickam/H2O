package com.example.h2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class proconfirm extends AppCompatActivity {
    TextView fname,fadd,fmobile,fquan,fcost;
    Button con,back;
    String userid,tc,quan,bookid;
    DatabaseReference databaseReference;
    String name, address, mobile;
    ProgressDialog progressDialog;
    FirebaseUser user;
    String logkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proconfirm);
        user = FirebaseAuth.getInstance().getCurrentUser();
        logkey = user.getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait fetching for results......");
        progressDialog.show();
        userid = getIntent().getStringExtra("userkey");
        tc = getIntent().getStringExtra("cost");
        quan = getIntent().getStringExtra("quantity");
        bookid = getIntent().getStringExtra("id");
        fname = (TextView) findViewById(R.id.name);
        fadd = (TextView) findViewById(R.id.address);
        fmobile = (TextView) findViewById(R.id.mob);
        fquan = (TextView) findViewById(R.id.quan);
        fcost = (TextView) findViewById(R.id.totcost);
        fcost.setText(tc);
        fquan.setText(quan);
        con = (Button) findViewById(R.id.confirm);
        back = (Button) findViewById(R.id.gb);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookconfirm();
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(proconfirm.this,proopenorders.class));
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    String username = snapshot.getKey();
                    if(username.equals(userid)){
                    name = snapshot.child("full").getValue().toString();
                    address = snapshot.child("address").getValue().toString();
                    mobile = snapshot.child("mobile").getValue().toString();
                    fname.setText(name);
                    fadd.setText(address);
                    fmobile.setText(mobile);
                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(proconfirm.this, userid, Toast.LENGTH_SHORT).show();
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

    private void bookconfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure? once booked we can't cancel the order");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressDialog.setMessage("Wait! booking is all yours");
                progressDialog.show();
                takebook();
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

    private void takebook() {
    FirebaseDatabase.getInstance().getReference("confirm").child(bookid)
            .setValue(logkey)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            update();
                        }else{
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(proconfirm.this, "Retry connection failure......", Toast.LENGTH_SHORT).show();
                        }
                }
            });


    }

    private void update() {
        String status = "1";
        booking book = new booking(userid,quan,tc,status);
        FirebaseDatabase.getInstance().getReference("bookings").child(bookid)
                .setValue(book)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(progressDialog.isShowing()){
                            progressDialog.isShowing();
                        }
                            if(task.isSuccessful()){
                                Intent intent = new Intent(proconfirm.this,prosuccess.class);
                                intent.putExtra("bid",bookid);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(proconfirm.this, "Error updating", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}