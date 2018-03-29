package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class tabledetails extends AppCompatActivity {

    private DatabaseReference cartDatabaseReference;
    private DatabaseReference billDatabaseReference;
    private DatabaseReference tableNoDatabaseReference;
    private DatabaseReference databaseorderhistory;
    private RecyclerView dishlist;
    private String codevalue;
    Vector<getbill> billitems = new Vector<>();
    Vector<getuserid> userIdItems = new Vector<>();
    private tabledetailsadapter adapter;
    private Button confirm;
    private String tableno;
    private String confirmStatus;
    Vector<getdishdetails> dishitems = new Vector<>();
    int totalprice=0;
    private Button clear,bill;
    private TextView total;
    private SharedPreferences restaurantIDpref;
    private FirebaseAuth mAuth;
    private String date;
    private int totalquantity;
    private int totalPriceForOrderHistory;
    private DatabaseReference userOrderHistoryDatabaseReference;
    private DatabaseReference userIdDatabaseReference;
    String orderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabledetails);

        final Intent tabledetailsintent = getIntent();
        tableno = tabledetailsintent.getStringExtra("number");
        restaurantIDpref = getSharedPreferences("myPref", Context.MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);

        mAuth = FirebaseAuth.getInstance();
        confirm = (Button)(findViewById(R.id.confirmbutton));
        clear = (Button)(findViewById(R.id.clearbutton));
        bill = (Button)(findViewById(R.id.billbtn));

        total = (TextView)(findViewById(R.id.totaltxt));

        cartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("cart");
        tableNoDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno);
        userOrderHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        userIdDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("activeUsers");
        cartDatabaseReference.keepSynced(true);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dishlist = (RecyclerView) findViewById(R.id.dishlistrecycler);
        dishlist.setLayoutManager(new LinearLayoutManager(this));

        adapter = new tabledetailsadapter(dishitems, this);
        dishlist.setAdapter(adapter);

//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference orderiddatabase = FirebaseDatabase.getInstance().getReference().push();
//                orderid = orderiddatabase.getKey().toString();
//                for(int j =0; j<userIdItems.size(); j++){
//                    userOrderHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdItems.get(j).getUserid()).child("orderHistory").child(orderid);
//                   for(int i = 0; i<billitems.size();i++){
//                       userOrderHistoryDatabaseReference.child("date").setValue(date);
//                       userOrderHistoryDatabaseReference.child("restaurantID").setValue(codevalue);
//                       userOrderHistoryDatabaseReference.child("dishes").child(billitems.get(i).getDishID()).setValue(billitems.get(i));
//                       userOrderHistoryDatabaseReference.child("total").setValue(totalPriceForOrderHistory);
//                }
//
//
//
//
//                }
//
//                databaseorderhistory = FirebaseDatabase.getInstance().getReference().child("owners").child(mAuth.getCurrentUser().getUid().toString()).child("orderHistory").child(orderid);
//
//                for(int k =0; k<billitems.size(); k++){
//                    databaseorderhistory.child("dishes").child(billitems.get(k).getDishID()).setValue(billitems.get(k));
//
//                }
//
//                databaseorderhistory.child("date").setValue(date);
//                databaseorderhistory.child("restaurantID").setValue(codevalue);
//                databaseorderhistory.child("total").setValue(totalPriceForOrderHistory);
//
//            cartDatabaseReference.removeValue();
//            billDatabaseReference.removeValue();
//            userIdDatabaseReference.removeValue();
//            tableNoDatabaseReference.child("confirmStatus").setValue("true");
//            tableNoDatabaseReference.child("callWaiter").setValue("false");
//            tableNoDatabaseReference.child("availability").setValue("true");
//            total.setText("");
//
//
//
//            }
//        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.INVISIBLE);
                tableNoDatabaseReference.child("confirmStatus").setValue("true");

                for(int i=0; i<dishitems.size(); i++){
                    totalquantity=0;
                    int flag=0;
                    if(billitems.size()!=0){
                        for(int j = 0; j<billitems.size(); j++){
                            if(dishitems.get(i).getId().equals(billitems.get(j).getDishID())){
                                totalquantity = billitems.get(j).getQuantity() + dishitems.get(i).getQuantity();
                                DatabaseReference updatebill = billDatabaseReference.child(billitems.get(j).getDishID());
                                Map<String, Object> taskmap = new HashMap<String, Object>();
                                taskmap.put("quantity", billitems.get(j).getQuantity() + dishitems.get(i).getQuantity());
                                updatebill.updateChildren(taskmap);
                                flag=1;
                                  //billDatabaseReference.child(billItemsVector.get(j).getDishID()).child("quantitytxt").setValue(totalquantity);
                            }
                        }

                        if (flag==0){
                            billDatabaseReference.child(dishitems.get(i).getId()).setValue(dishitems.get(i));
                        }
                    }else{
                        billDatabaseReference.child(dishitems.get(i).getId()).setValue(dishitems.get(i));
                    }

                }

                cartDatabaseReference.removeValue();

            }
        });


        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bill = new Intent(getApplicationContext(), bill.class);
                bill.putExtra("number",tableno );
                bill.putExtra("codevalue", codevalue);
                startActivity(bill);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();


        confirm.setVisibility(View.INVISIBLE);

        billDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table").child(tableno).child("currentOrder").child("bill");

        billDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                billitems.clear();
                for (DataSnapshot dishid : dataSnapshot.getChildren()) {
                    getbill dish = new getbill();

                    dish.setName(dishid.child("name").getValue().toString());
                    dish.setPrice(dishid.child("price").getValue().toString());
                    dish.setQuantity(Integer.parseInt(dishid.child("quantity").getValue().toString()));
                    dish.setDishID(dishid.getKey().toString());
                    billitems.add(dish);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tableNoDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try{
                   confirmStatus = dataSnapshot.child("confirmStatus").getValue().toString();
                   if(confirmStatus.equals("false")){
                       confirm.setVisibility(View.VISIBLE);
                   }

               }catch(Exception e){
                   Log.i("error", e.getMessage());
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    cartDatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dishitems.clear();
            totalprice=0;
            for (DataSnapshot dishid : dataSnapshot.getChildren()) {
                try{
                    getdishdetails dish = new getdishdetails();
                    dish.setId(dishid.getKey());
                    dish.setName(dishid.child("name").getValue().toString());
                    dish.setPrice(dishid.child("price").getValue().toString());
                    dish.setQuantity(Integer.parseInt(dishid.child("quantity").getValue().toString()));
                    dishitems.add(dish);

                    totalprice += Integer.parseInt(dishid.child("price").getValue().toString())*(Integer.parseInt(dishid.child("quantity").getValue().toString()));

                }catch (Exception e){

                }


            }

            total.setText(Integer.toString(totalprice));
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






