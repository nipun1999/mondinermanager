package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by agarw on 3/13/2018.
 */

public class orderhistoryadapter  extends RecyclerView.Adapter<orderhistoryadapter.MyViewHolder> {
    Context context;
    private Vector<getOrderHistoryId> orderHistoryIdVector;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderHistoryIdText;


        public MyViewHolder(View view) {
            super(view);
            orderHistoryIdText = (TextView)(view.findViewById(R.id.orderHistoryId));
        }
    }

    public orderhistoryadapter(Vector<getOrderHistoryId> orderId, Context context) {
        this.orderHistoryIdVector = orderId;
        this.context = context;
    }

    @Override
    public orderhistoryadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderHistoryView = inflater.inflate(R.layout.orderhistoryrow, parent, false);
        return new orderhistoryadapter.MyViewHolder(orderHistoryView);

    }

    @Override
    public void onBindViewHolder(final orderhistoryadapter.MyViewHolder holder, final int position) {


        holder.orderHistoryIdText.setText(orderHistoryIdVector.get(position).getOrderID().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                orderHistoryDetailsFrag ldf = new orderHistoryDetailsFrag();
                Bundle args = new Bundle();
                args.putString("orderID", orderHistoryIdVector.get(position).getOrderID().toString());
                ldf.setArguments(args);

//Inflate the fragment
                manager.beginTransaction().add(R.id.fragment_container, ldf).commit();

//                Intent orderDetails = new Intent(v.getContext(),orderHistoryDetails.class);
//                orderDetails.putExtra("orderID", orderHistoryIdVector.get(position).getOrderID().toString() );
//                v.getContext().startActivity(orderDetails);
            }
        });
    }
    @Override
    public int getItemCount() {
        return orderHistoryIdVector.size();
    }


}
