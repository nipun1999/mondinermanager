package com.example.agarw.mondinermanager_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText codeTxt;
    private Button submitBtn;
    private DatabaseReference restaurantsDatabaseRef;
    private String code;
    private ProgressDialog mProgress;
    private String codeget;
    public SharedPreferences restaurantIDpref;
    private FirebaseAuth mAuth;
    private DatabaseReference ownersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codeTxt = (EditText) (findViewById(R.id.codetext));
        submitBtn = (Button) (findViewById(R.id.editBtn));
        restaurantsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("restaurants");
        mProgress = new ProgressDialog(this);
        restaurantIDpref = getSharedPreferences("myPref", Context.MODE_WORLD_WRITEABLE);
        mAuth = FirebaseAuth.getInstance();
        ownersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("owners");



        ownersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid().toString())) {

                    } else {
                        Intent registerpage = new Intent(MainActivity.this, register.class);
                        startActivity(registerpage);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkcode();
            }
        });
    }

    private void checkcode() {
        mProgress.setMessage("Checking Code");
        mProgress.show();

        code = codeTxt.getText().toString();

        if (!TextUtils.isEmpty(code)) {
            restaurantsDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(code)) {
                        SharedPreferences.Editor editor = restaurantIDpref.edit();
                        editor.putString("restaurantID", code);
                        editor.commit();
                        Intent home = new Intent(MainActivity.this, HomeActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(home);
                        mProgress.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter correct code", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed.Please try again", Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
            });
        } else {

            Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
            mProgress.dismiss();
        }
    }

}
