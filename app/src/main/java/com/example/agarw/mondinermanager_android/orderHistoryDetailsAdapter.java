package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by agarw on 3/13/2018.
 */

public class orderHistoryDetailsAdapter extends RecyclerView.Adapter<orderHistoryDetailsAdapter.MyViewHolder> {
    Context context;
    private Vector<getorderHistoryDetails> dishitems;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, pricetxt, quantity, totalfield;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.orderHistoryDetailsdishnamerow);
            pricetxt = (TextView) view.findViewById(R.id.orderHistoryDetailspricerow);
            quantity = (TextView) view.findViewById(R.id.orderHistoryDetailsquantityrow);

        }
    }
    public orderHistoryDetailsAdapter(Vector<getorderHistoryDetails> dish, Context context) {
        this.dishitems = dish;
        this.context = context;
    }

    @Override
    public orderHistoryDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderHistoryDetailsview = inflater.inflate(R.layout.orderhistorydetailsrow, parent, false);
        return new orderHistoryDetailsAdapter.MyViewHolder(orderHistoryDetailsview);

    }
    @Override
    public void onBindViewHolder(final orderHistoryDetailsAdapter.MyViewHolder holder, final int position) {

        holder.quantity.setText(Integer.toString(dishitems.get(position).getQuantity()));
        holder.pricetxt.setText(dishitems.get(position).getPrice().toString());
        holder.name.setText(dishitems.get(position).getName().toString());


    }
    @Override
    public int getItemCount() {
        return dishitems.size();
    }

}
