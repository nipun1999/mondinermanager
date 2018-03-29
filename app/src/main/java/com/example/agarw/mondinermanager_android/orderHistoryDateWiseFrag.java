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
public class orderHistoryDateWiseFrag extends Fragment {
    String date;
    Vector<getOrderHistoryId> orderIdVector  = new Vector<>();
    private RecyclerView orderHistoryDateWiseRecycler;
    private DatabaseReference databaseorderHistoryReference;
    private orderhistoryadapter adapter;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private FirebaseAuth mAuth;
    private String ownerID;

    public orderHistoryDateWiseFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_order_history_date_wise, container, false);
        date = getArguments().getString("date");
        mAuth = FirebaseAuth.getInstance();
        ownerID = mAuth.getCurrentUser().getUid().toString();
        orderHistoryDateWiseRecycler = (RecyclerView)(rootview.findViewById(R.id.orderHistoryDateWiseRecycler));
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        databaseorderHistoryReference = FirebaseDatabase.getInstance().getReference().child("owners").child(ownerID).child("orderHistory");
        adapter = new orderhistoryadapter(orderIdVector, getActivity());
        orderHistoryDateWiseRecycler = (RecyclerView)(rootview.findViewById(R.id.orderHistoryDateWiseRecycler));
        orderHistoryDateWiseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderHistoryDateWiseRecycler.setAdapter(adapter);
        return rootview;

    }
    @Override
    public void onStart() {
        super.onStart();

        databaseorderHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderIdVector.clear();
                for (final DataSnapshot allOrderId : dataSnapshot.getChildren()) {
                    final getOrderHistoryId orderId = new getOrderHistoryId();
                    if(allOrderId.child("date").getValue().toString().equals(date)){
                        orderId.setOrderID(allOrderId.getKey().toString());
                        orderIdVector.add(orderId);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
