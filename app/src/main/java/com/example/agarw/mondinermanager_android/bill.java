package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class bill extends AppCompatActivity {
    Vector<getbill> billItemsVector = new Vector<>();
    int totalprice;
    private DatabaseReference databaseBillRef;
    private DatabaseReference billDatabaseReference;
    private billadapter adapter;
    private String codevalue;
    private String tableno;
    private RecyclerView billListRecycler;
    private TextView totalBillText;
    private SharedPreferences restaurantIDpref;
    private Button checkout;
    private DatabaseReference userOrderHistoryDatabaseReference;
    private DatabaseReference userIdDatabaseReference;
    private DatabaseReference databaseorderhistory;
    private DatabaseReference cartDatabaseReference;
    private DatabaseReference tableNoDatabaseReference;
    Vector<getuserid> userIdItems = new Vector<>();
    private FirebaseAuth mAuth;
    private String date;
    String orderid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        restaurantIDpref = getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        Intent bill = getIntent();
        tableno = bill.getStringExtra("number");
//        codevalue = bill.getStringExtra("codevalue");
        databaseBillRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("bill");
        billListRecycler = (RecyclerView) (findViewById(R.id.billrecycler));
        billListRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new billadapter(billItemsVector, this);
        billListRecycler.setAdapter(adapter);
        totalBillText = (TextView) (findViewById(R.id.billtotaltxt));
        checkout = (Button)(findViewById(R.id.clearbutton));
        userOrderHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        userIdDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("activeUsers");
        cartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("cart");
        tableNoDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mAuth = FirebaseAuth.getInstance();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        billDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("bill");


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference orderiddatabase = FirebaseDatabase.getInstance().getReference().push();
                orderid = orderiddatabase.getKey().toString();
                for(int j =0; j<userIdItems.size(); j++){
                    userOrderHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdItems.get(j).getUserid()).child("orderHistory").child(orderid);
                    for(int i = 0; i<billItemsVector.size();i++){
                        userOrderHistoryDatabaseReference.child("date").setValue(date);
                        userOrderHistoryDatabaseReference.child("restaurantID").setValue(codevalue);
                        userOrderHistoryDatabaseReference.child("dishes").child(billItemsVector.get(i).getDishID()).setValue(billItemsVector.get(i));
                        userOrderHistoryDatabaseReference.child("total").setValue(totalprice);
                    }




                }

                databaseorderhistory = FirebaseDatabase.getInstance().getReference().child("owners").child(mAuth.getCurrentUser().getUid().toString()).child("orderHistory").child(orderid);

                for(int k =0; k<billItemsVector.size(); k++){
                    databaseorderhistory.child("dishes").child(billItemsVector.get(k).getDishID()).setValue(billItemsVector.get(k));

                }

                databaseorderhistory.child("date").setValue(date);
                databaseorderhistory.child("total").setValue(totalprice);

                cartDatabaseReference.removeValue();
                billDatabaseReference.removeValue();
                userIdDatabaseReference.removeValue();
                tableNoDatabaseReference.child("confirmStatus").setValue("true");
                tableNoDatabaseReference.child("callWaiter").setValue("false");
                tableNoDatabaseReference.child("availability").setValue("true");
                totalprice = 0;
                totalBillText.setText("0");


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


        databaseBillRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalprice = 0;
                billItemsVector.clear();
                try {
                    for (final DataSnapshot dishid : dataSnapshot.getChildren()) {
                        final getbill dish = new getbill();
                        dish.setName(dishid.child("name").getValue().toString());
                        dish.setPrice(dishid.child("price").getValue().toString());
                        dish.setQuantity(Integer.parseInt(dishid.child("quantity").getValue().toString()));
                        dish.setDishID(dishid.getKey().toString());
                        billItemsVector.add(dish);
                        totalprice += Integer.parseInt(dishid.child("price").getValue().toString()) * (Integer.parseInt(dishid.child("quantity").getValue().toString()));
                    }

                } catch (Exception e) {

                }


                totalBillText.setText(Integer.toString(totalprice));
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userIdDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIdItems.clear();
                for(DataSnapshot userid : dataSnapshot.getChildren()){
                    getuserid user = new getuserid();
                    user.setUserid(userid.getKey());
                    userIdItems.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
