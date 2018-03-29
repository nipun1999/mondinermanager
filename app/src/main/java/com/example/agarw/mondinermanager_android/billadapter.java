package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by agarw on 1/26/2018.
 */

public class billadapter extends RecyclerView.Adapter<billadapter.MyViewHolder> {

    Context context;
    private Vector<getbill> billItemsVector;
    public int price;
    public int quantity;
    public static int total;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nametxt, pricetxt, quantitytxt, totalfield;


        public MyViewHolder(View view) {
            super(view);
            nametxt = (TextView) view.findViewById(R.id.dishnamerow);
            pricetxt = (TextView) view.findViewById(R.id.pricerow);
            quantitytxt = (TextView) view.findViewById(R.id.quantityrow);

        }
    }

    public billadapter(Vector<getbill> dish, Context context) {
        this.billItemsVector = dish;
        this.context = context;
    }

    @Override
    public billadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View billview = inflater.inflate(R.layout.billrow, parent, false);
        return new billadapter.MyViewHolder(billview);

    }

    @Override
    public void onBindViewHolder(final billadapter.MyViewHolder holder, final int position) {

        holder.quantitytxt.setText(Integer.toString(billItemsVector.get(position).getQuantity()));
        holder.pricetxt.setText(billItemsVector.get(position).getPrice().toString());
        holder.nametxt.setText(billItemsVector.get(position).getName().toString());


    }

    @Override
    public int getItemCount() {
        return billItemsVector.size();
    }


}
