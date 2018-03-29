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

public class addOnAdapter extends RecyclerView.Adapter<addOnAdapter.MyViewHolder> {
    Context context;
    private Vector<getAddOnStatus> addOnItemsVector;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nametxt, categorytxt;


        public MyViewHolder(View view) {
            super(view);
            nametxt = (TextView) view.findViewById(R.id.addOnName);
            categorytxt = (TextView) view.findViewById(R.id.addOnCategory);


        }
    }

    public addOnAdapter(Vector<getAddOnStatus> addOnStatus, Context context) {
        this.addOnItemsVector = addOnStatus;
        this.context = context;
    }

    @Override
    public addOnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View addOnView = inflater.inflate(R.layout.addonrow, parent, false);
        return new addOnAdapter.MyViewHolder(addOnView);

    }

    @Override
    public void onBindViewHolder(final addOnAdapter.MyViewHolder holder, final int position) {

        holder.categorytxt.setText(addOnItemsVector.get(position).getCategory().toString());
        holder.nametxt.setText(addOnItemsVector.get(position).getName().toString());


    }
    @Override
    public int getItemCount() {
        return addOnItemsVector.size();
    }


}
