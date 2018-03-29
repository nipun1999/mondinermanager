package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

/**
 * Created by agarw on 2/17/2018.
 */

public class callWaiterAdapter extends RecyclerView.Adapter<callWaiterAdapter.MyViewHolder> {

    Context context;
    private Vector<getCallWaiter> callWaitersitems;
    public DatabaseReference mdatabase;
    private String codevalue;
    private SharedPreferences restaurantIDpref;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tableno;
        public Button callWaiterButton;


        public MyViewHolder(View view) {
            super(view);
            tableno = (TextView) (view.findViewById(R.id.tablenotxt));
            callWaiterButton = (Button)(view.findViewById(R.id.callwaiterbutton));

        }


    }

    public callWaiterAdapter(Vector<getCallWaiter> callWaiter, Context context) {
        this.callWaitersitems = callWaiter;
        this.context = context;
    }

    @Override
    public callWaiterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View callWaiterView = inflater.inflate(R.layout.callwaitergrid, parent, false);
        return new callWaiterAdapter.MyViewHolder(callWaiterView);

    }

    @Override
    public void onBindViewHolder(final callWaiterAdapter.MyViewHolder holder, final int position) {

        restaurantIDpref = holder.itemView.getContext().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);


        try {
            holder.tableno.setText("Table No. " + callWaitersitems.get(position).getNumber().toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tabledetailsintent = new Intent(view.getContext(), tabledetails.class);
                    tabledetailsintent.putExtra("number", callWaitersitems.get(position).getTableid().toString());
                    view.getContext().startActivity(tabledetailsintent);
                }
            });
        }catch (Exception e){


        }

        try {

            holder.callWaiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mdatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("table");
                    mdatabase.keepSynced(true);
                    mdatabase.child(callWaitersitems.get(position).getTableid().toString()).child("callWaiter").setValue("false");
                }
            });
        }catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {
        return callWaitersitems.size();
    }

}
