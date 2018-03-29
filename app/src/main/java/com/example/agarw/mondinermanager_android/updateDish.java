package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class updateDish extends AppCompatActivity {

    private String dishid;
    private Button updateDishBtn, deleteDishBtn;
    private TextView categoryTextView;
    private EditText nameEditText, priceEditText;
    private RadioGroup dishTypeRadioGroup;
    private RadioButton vegradio, nonvegradio,eggradio;
    private String codevalue;
    private DatabaseReference databaseDishRef;
    private SharedPreferences restaurantIDpref;
    private RadioButton typeRdioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dish);

        final Intent updatedish = getIntent();
        dishid = updatedish.getStringExtra("dishid");
        vegradio = (RadioButton)(findViewById(R.id.vegradio));
        nonvegradio = (RadioButton)(findViewById(R.id.nonvegradio));
        eggradio = (RadioButton)(findViewById(R.id.eggradio));
        nameEditText = (EditText) (findViewById(R.id.nametxt));
        priceEditText = (EditText) (findViewById(R.id.pricetxt));
        categoryTextView = (TextView)(findViewById(R.id.categoryTextView));
        dishTypeRadioGroup = (RadioGroup) (findViewById(R.id.dishtypegroup));
        updateDishBtn = (Button)(findViewById(R.id.updateDishBtn));
        deleteDishBtn = (Button)(findViewById(R.id.deleteDishBtn));
        restaurantIDpref = this.getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        databaseDishRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("dishes").child(dishid);


            databaseDishRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        categoryTextView.setText(dataSnapshot.child("category").getValue().toString());
                        nameEditText.setText(dataSnapshot.child("name").getValue().toString());
                        priceEditText.setText(dataSnapshot.child("price").getValue().toString());
                        if(dataSnapshot.child("type").getValue().toString().equals("Veg")){
                            vegradio.setChecked(true);
                        }else if(dataSnapshot.child("type").getValue().toString().equals("Non-Veg")){
                            nonvegradio.setChecked(true);
                        }else if(dataSnapshot.child("type").getValue().toString().equals("Egg")){
                            eggradio.setChecked(true);
                        }
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            updateDishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId = dishTypeRadioGroup.getCheckedRadioButtonId();
                    typeRdioButton = (RadioButton) (findViewById(selectedId));
                    String dishtype = typeRdioButton.getText().toString();
                    Map<String, Object> taskmap = new HashMap<String, Object>();
                    taskmap.put("name", nameEditText.getText().toString());
                    taskmap.put("price", priceEditText.getText().toString());
                    taskmap.put("type", dishtype);
                    taskmap.put("category", categoryTextView.getText().toString());
                    databaseDishRef.updateChildren(taskmap);
                    Toast.makeText(updateDish.this, "Dish updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent menuintent = new Intent(updateDish.this, tabbed_menu.class);
                    startActivity(menuintent);
                }
            });

            deleteDishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete Dish ?");
                    builder.setMessage("Are you sure you want to delete this dish ? ");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(updateDish.this, "Dish deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent menuintent = new Intent(updateDish.this, tabbed_menu.class);
                                    startActivity(menuintent);
                                    databaseDishRef.removeValue();

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });


    }
}
