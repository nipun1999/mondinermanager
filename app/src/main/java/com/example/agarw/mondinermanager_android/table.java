package com.example.agarw.mondinermanager_android;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class table extends Fragment {

    private RecyclerView mTableList;
    private DatabaseReference mdatabase;

    private DatabaseReference mdatabasecallWaiter;
    private String codevalue;
    private String callwaiterstring;
    private String tablenogridkey;
    private SharedPreferences restaurantIDpref;
    private ProgressDialog mprogress;

    private FirebaseRecyclerAdapter<table, RecyclerView.ViewHolder> firebaseRecyclerAdapter;


    public table() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table2, container, false);

        GridLayoutManager tableGridLayout = new GridLayoutManager(getContext(), 2);

        tableGridLayout.getReverseLayout();


        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);


        mdatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table");
        mdatabase.keepSynced(true);
        mprogress = new ProgressDialog(this.getContext());

        mTableList = (RecyclerView) (view.findViewById(R.id.tablelist));
        mTableList.setHasFixedSize(true);
        mTableList.setLayoutManager(tableGridLayout);





        return view;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<gettable, tableNumberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<gettable, tableNumberViewHolder>(
                gettable.class,
                R.layout.tablegrid,
                tableNumberViewHolder.class,
                mdatabase

        ) {
            @Override
            protected void populateViewHolder(final tableNumberViewHolder viewHolder, gettable model, final int position) {
                viewHolder.settablenumber("Table No. " + model.getNumber());
                String tableno = getRef(position).getKey().toString();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tabledetailsintent = new Intent(getActivity(), tabledetails.class);
                        tabledetailsintent.putExtra("number", getRef(position).getKey().toString());
                        tabledetailsintent.putExtra("codevalue", codevalue);
                        startActivity(tabledetailsintent);
                    }

                });

                viewHolder.setCallWaiter(model.getCallWaiter(), getRef(position).getKey().toString(), tableno.substring(tableno.length() - 2));

                try {
                    viewHolder.setconfirm(model.getConfirmStatus(), tableno.substring(tableno.length() - 2));
                } catch (Exception e) {

                }


            }

        };
        mTableList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class tableNumberViewHolder extends RecyclerView.ViewHolder {

        Button callwaiter;
        TextView tablenotxtview;
        String codevalue;
        DatabaseReference mdatabase;
        View callWaiterView;
        View confirmStatusView;


        public tableNumberViewHolder(View itemView) {
            super(itemView);


            SharedPreferences restaurantIDpref = itemView.getContext().getSharedPreferences("myPref", Context.
                    MODE_WORLD_READABLE);
            codevalue = restaurantIDpref.getString("restaurantID", null);
            callwaiter = (Button) (itemView.findViewById(R.id.callwaiterbutton));
            tablenotxtview = (TextView) (itemView.findViewById(R.id.tablenotxt));
            callWaiterView = (View) (itemView.findViewById(R.id.callWaiterView));
            confirmStatusView = (View) (itemView.findViewById(R.id.confirmStatusView));
            callwaiter.setVisibility(View.INVISIBLE);
            mdatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table");
            mdatabase.keepSynced(true);
            callWaiterView.setBackgroundColor(Color.TRANSPARENT);
            callwaiter.setVisibility(View.INVISIBLE);
            confirmStatusView.setBackgroundColor(Color.TRANSPARENT);
        }


        public void setCallWaiter(String callWaiter, final String key, String tableno) {

            if (callWaiter.toString().equals("true")) {
//                callWaiterView.setBackgroundColor(Color.RED);
//                callwaiter.setVisibility(View.VISIBLE);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(itemView.getContext())
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Waiter Called on Table No " + tableno)
                                .setContentText("Please Call The Waiter on Table No " + tableno);

                Intent resultIntent = new Intent(itemView.getContext(), HomeActivity.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                itemView.getContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

// Sets an ID for the notification
                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager)itemView.getContext().getSystemService(NOTIFICATION_SERVICE);
                mBuilder.setAutoCancel(true);
// Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }

            callwaiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mdatabase.child(key).child("callWaiter").setValue("false");
                }
            });

        }

        public void setconfirm(String confirmStatus, String tableno) {


            if (confirmStatus.toString().equals("false")) {
//                confirmStatusView.setBackgroundColor(Color.GREEN);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(itemView.getContext())
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("New Order on Table No " + tableno)
                                .setContentText("Please Confirm The New Order");

                Intent resultIntent = new Intent(itemView.getContext(), HomeActivity.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                itemView.getContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

// Sets an ID for the notification
                int mNotificationId = 002;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager)itemView.getContext().getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mBuilder.setAutoCancel(true);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());


            }

        }


        public void settablenumber(String number) {
            TextView post_tableno = (TextView) (itemView.findViewById(R.id.tablenotxt));
            post_tableno.setText(number);
        }

    }

}



