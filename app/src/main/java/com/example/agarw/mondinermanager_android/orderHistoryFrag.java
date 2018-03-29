package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class orderHistoryFrag extends Fragment {
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private RecyclerView orderHistoryRecycler;
    private DatabaseReference databaseOrderHistoryRef;
    private FirebaseAuth mAuth;
    private String ownerId;
    private CalendarView calender;
    private Button getDateButton;
    String dateformatted;
    int d,m,y;
    Vector<getOrderDate> orderDateVector = new Vector<>();

    public orderHistoryFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_order_history, container, false);
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
//        orderHistoryRecycler = (RecyclerView)(findViewById(R.id.orderhistoryrecycler));
        mAuth = FirebaseAuth.getInstance();
        ownerId = mAuth.getCurrentUser().getUid();
        databaseOrderHistoryRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child("owners").child(ownerId).child("orderHistory");
        calender = (CalendarView)(rootview.findViewById(R.id.simpleCalendarView));
        getDateButton = (Button)(rootview.findViewById(R.id.getDateButton));
        long currentdate = calender.getDate();
        calender.setMaxDate(currentdate);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                d = dayOfMonth;
                m = month;
                y = year;
            }
        });
        getDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m+1>9){
                    dateformatted = Integer.toString(y)+"-"+Integer.toString(m+1)+"-"+Integer.toString(d);
                }else{
                    dateformatted = Integer.toString(y)+"-"+"0"+Integer.toString(m+1)+"-"+Integer.toString(d);
                }

                orderHistoryDateWiseFrag ldf = new orderHistoryDateWiseFrag();
                Bundle args = new Bundle();
                args.putString("date", dateformatted);
                ldf.setArguments(args);

//Inflate the fragment
                getFragmentManager().beginTransaction().add(R.id.fragment_container, ldf).commit();


            }
        });

        return rootview;
    }


}
