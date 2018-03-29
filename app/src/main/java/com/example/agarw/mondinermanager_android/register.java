package com.example.agarw.mondinermanager_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText name, phone, email;
    private Button register;
    private DatabaseReference ownerDatabaseRef;
    private FirebaseAuth mAuth;
    private String namevalue;
    private String phonevalue;
    private String emailvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) (findViewById(R.id.nametxt));
        phone = (EditText) (findViewById(R.id.phonetxt));
        email = (EditText) (findViewById(R.id.emailtxt));
        register = (Button) (findViewById(R.id.registerbtn));
        mAuth = FirebaseAuth.getInstance();
        ownerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("owners").child(mAuth.getCurrentUser().getUid().toString());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namevalue = name.getText().toString();
                phonevalue = phone.getText().toString();
                emailvalue = email.getText().toString();
                ownerDatabaseRef.child("name" +
                        "").setValue(namevalue);
                ownerDatabaseRef.child("email").setValue(emailvalue);
                ownerDatabaseRef.child("contact").setValue(phonevalue);
                Intent home = new Intent(register.this, HomeActivity.class);
                startActivity(home);
            }
        });


    }
}
