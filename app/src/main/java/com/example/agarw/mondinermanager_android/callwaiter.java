package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class callwaiter extends Fragment {

    private RecyclerView callWaiterRecycler;
    private DatabaseReference databaseConfirmStatus;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private callWaiterAdapter adapter;
    Vector<getCallWaiter> callWaiteritems = new Vector<>();

    public callwaiter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_callwaiter, container, false);


        restaurantIDpref = this.getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);

        callWaiterRecycler = (RecyclerView)(rootview.findViewById(R.id.callWaiterRecycler));

        GridLayoutManager tableGridLayout = new GridLayoutManager(getContext(), 2);

        tableGridLayout.getReverseLayout();

        callWaiterRecycler.setHasFixedSize(true);
        callWaiterRecycler.setLayoutManager(tableGridLayout);

        adapter = new callWaiterAdapter(callWaiteritems, this.getContext());
        callWaiterRecycler.setAdapter(adapter);





        return rootview;

    }
    @Override
    public void onResume(){
        super.onResume();
        try{
            databaseConfirmStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    callWaiteritems.clear();

                    for (DataSnapshot tableid : dataSnapshot.getChildren()){
                        getCallWaiter table = new getCallWaiter();

                        if(tableid.child("callWaiter").getValue().toString().equals("true")){
                            table.setNumber(tableid.child("number").getValue().toString());
                            table.setTableid(tableid.getKey().toString());
//                        NotificationCompat.Builder mBuilder =
//                                new NotificationCompat.Builder(getContext())
//                                        .setSmallIcon(R.drawable.ic_launcher_background)
//                                        .setContentTitle("Waiter Called on Table No " + tableid.getKey().toString())
//                                        .setContentText("Please Call The Waiter on Table No " + tableid.getKey().toString());
//
//                        Intent resultIntent = new Intent(getContext(),TablesSorted.class);
//
//// Because clicking the notification opens a new ("special") activity, there's
//// no need to create an artificial back stack.
//                        PendingIntent resultPendingIntent =
//                                PendingIntent.getActivity(
//                                        getContext(),
//                                        0,
//                                        resultIntent,
//                                        PendingIntent.FLAG_UPDATE_CURRENT
//                                );
//                        mBuilder.setContentIntent(resultPendingIntent);
//
//// Sets an ID for the notification
//                        int mNotificationId = 001;
//// Gets an instance of the NotificationManager service
//                        NotificationManager mNotifyMgr =
//                                (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
//                        mBuilder.setAutoCancel(true);
//// Builds the notification and issues it.
//                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                            callWaiteritems.add(table);
                        }

                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch (Exception e){

        }

    }

    @Override
    public void onStart(){
        super.onStart();
        try{
            databaseConfirmStatus = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table");
            databaseConfirmStatus.keepSynced(true);

            databaseConfirmStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    callWaiteritems.clear();

                    for (DataSnapshot tableid : dataSnapshot.getChildren()){
                        getCallWaiter table = new getCallWaiter();

                        if(tableid.child("callWaiter").getValue().toString().equals("true")){
                            table.setNumber(tableid.child("number").getValue().toString());
                            callWaiteritems.add(table);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }



    }

}
