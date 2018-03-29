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
public class confirmTables extends Fragment {

    private RecyclerView confirmTablesRecycler;
    private DatabaseReference databaseConfirmStatus;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private confirmTablesAdapter adapter;
    Vector<getConfirmTablesStatus> confirmStatusitems = new Vector<>();
    public confirmTables() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_fragment_confirm_tables, container, false);

        restaurantIDpref = this.getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);

        confirmTablesRecycler = (RecyclerView)(rootview.findViewById(R.id.confirmTablesRecycler));

        GridLayoutManager tableGridLayout = new GridLayoutManager(getContext(), 2);

        tableGridLayout.getReverseLayout();

        confirmTablesRecycler.setHasFixedSize(true);
        confirmTablesRecycler.setLayoutManager(tableGridLayout);

        adapter = new confirmTablesAdapter(confirmStatusitems, this.getContext());
        confirmTablesRecycler.setAdapter(adapter);






        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            databaseConfirmStatus = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table");
            databaseConfirmStatus.keepSynced(true);

        }catch (Exception e){

        }






    }

    @Override
    public void onResume(){
        super.onResume();
        try{

            databaseConfirmStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    confirmStatusitems.clear();

                    for (DataSnapshot tableid : dataSnapshot.getChildren()){
                        getConfirmTablesStatus table = new getConfirmTablesStatus();

                        if(tableid.child("confirmStatus").getValue().toString().equals("false")){
                            table.setNumber(tableid.child("number").getValue().toString());
                            table.setTablekey(tableid.getKey().toString());
//                        NotificationCompat.Builder mBuilder =
//                                new NotificationCompat.Builder(getContext())
//                                        .setSmallIcon(R.drawable.ic_launcher_background)
//                                        .setContentTitle("New Order on Table No " + tableid.getKey().toString())
//                                        .setContentText("Please Confirm The New Order");
//
//                        Intent resultIntent = new Intent(getContext(), HomeActivity.class);
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
//                        int mNotificationId = 002;
//// Gets an instance of the NotificationManager service
//                        NotificationManager mNotifyMgr =
//                                (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
//// Builds the notification and issues it.
//                        mBuilder.setAutoCancel(true);
//                        mNotifyMgr.notify(mNotificationId, mBuilder.build());



                            confirmStatusitems.add(table);
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

}
