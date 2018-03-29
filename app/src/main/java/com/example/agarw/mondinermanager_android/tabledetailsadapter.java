package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

/**
 * Created by agarw on 1/17/2018.
 */

public class tabledetailsadapter extends RecyclerView.Adapter<tabledetailsadapter.MyViewHolder> {

     Context context;
    private Vector<getdishdetails> dishitems;
    public int price;
    public int quantity;
    public static int total;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, pricetxt, quantity,totalfield;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.dishnamerow);
            pricetxt = (TextView) view.findViewById(R.id.pricerow);
            quantity = (TextView) view.findViewById(R.id.quantityrow);

        }


        public void setPrice(String dishID, final Integer quantity ){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(Details.REST_ID).child("menu/dishes").child(dishID);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    pricetxt.setText(dataSnapshot.child("price").getValue().toString());
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    total += (Integer.parseInt(dataSnapshot.child("price").getValue().toString())*quantity);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }








    }

    public tabledetailsadapter(Vector<getdishdetails> dish, Context context) {
        this.dishitems = dish;
        this.context = context;
    }

        


    @Override
    public tabledetailsadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View billview = inflater.inflate(R.layout.dishrow, parent, false);
        return new tabledetailsadapter.MyViewHolder(billview);

    }

    @Override
    public void onBindViewHolder(final tabledetailsadapter.MyViewHolder holder, final int position) {

        holder.quantity.setText(Integer.toString(dishitems.get(position).getQuantity()));

        holder.pricetxt.setText(dishitems.get(position).getPrice().toString());
        holder.name.setText(dishitems.get(position).getName().toString());








    }

    @Override
    public int getItemCount() {
        return dishitems.size();
    }

}
