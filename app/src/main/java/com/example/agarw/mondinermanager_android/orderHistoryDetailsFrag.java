package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class orderHistoryDetailsFrag extends Fragment {

    Vector<getorderHistoryDetails> dishItemsVector = new Vector<>();
    int totalprice;
    private DatabaseReference databaseOrderHistoryDetailsRef;
    private orderHistoryDetailsAdapter adapter;
    private String codevalue,orderID,userID;
    private String tableno;
    private RecyclerView orderHistoryDetailsRecycler;
    private TextView totalBillText;
    private FirebaseAuth mAuth;
    private SharedPreferences restaurantIDpref;

    public orderHistoryDetailsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview = inflater.inflate(R.layout.fragment_order_history_details, container, false);
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        mAuth = FirebaseAuth.getInstance();
        orderID = getArguments().getString("orderID");
        userID = mAuth.getCurrentUser().getUid();
        databaseOrderHistoryDetailsRef = FirebaseDatabase.getInstance().getReference().child("owners").child(userID).child("orderHistory").child(orderID).child("dishes");
        orderHistoryDetailsRecycler = (RecyclerView) (rootview.findViewById(R.id.orderHistoryDetailsRecycler));
        adapter = new orderHistoryDetailsAdapter(dishItemsVector, getActivity());
        orderHistoryDetailsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderHistoryDetailsRecycler.setAdapter(adapter);
        totalBillText = (TextView) (rootview.findViewById(R.id.totaltxt));

       return rootview;
    }
    @Override
    public void onStart() {
        super.onStart();

        totalprice = 0;
        databaseOrderHistoryDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dishItemsVector.clear();
                for (final DataSnapshot dishid : dataSnapshot.getChildren()) {
                    final getorderHistoryDetails dish = new getorderHistoryDetails();
                    dish.setName(dishid.child("name").getValue().toString());
                    dish.setPrice(dishid.child("price").getValue().toString());
                    dish.setQuantity(Integer.parseInt(dishid.child("quantity").getValue().toString()));
                    dishItemsVector.add(dish);
                    totalprice += Integer.parseInt(dishid.child("price").getValue().toString()) * (Integer.parseInt(dishid.child("quantity").getValue().toString()));
                }
                totalBillText.setText(Integer.toString(totalprice));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
