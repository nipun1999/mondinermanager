package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by agarw on 2/17/2018.
 */

public class confirmTablesAdapter extends RecyclerView.Adapter<confirmTablesAdapter.MyViewHolder> {

    Context context;
    private Vector<getConfirmTablesStatus> confirmStatusItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tableno;


        public MyViewHolder(View view) {
            super(view);
            tableno = (TextView) (view.findViewById(R.id.tablenotxt));


        }


    }

    public confirmTablesAdapter(Vector<getConfirmTablesStatus> confirmStatus, Context context) {
        this.confirmStatusItems = confirmStatus;
        this.context = context;
    }

    @Override
    public confirmTablesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View confirmStatusView = inflater.inflate(R.layout.confirmtablegrid, parent, false);
        return new confirmTablesAdapter.MyViewHolder(confirmStatusView);

    }

    @Override
    public void onBindViewHolder(final confirmTablesAdapter.MyViewHolder holder, final int position) {

        try {
        holder.tableno.setText("Table No. " + confirmStatusItems.get(position).getNumber().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tabledetailsintent = new Intent(view.getContext(), tabledetails.class);
                tabledetailsintent.putExtra("number", confirmStatusItems.get(position).getTablekey().toString());
                view.getContext().startActivity(tabledetailsintent);
            }
        });
        }catch (Exception e){


        }




    }

    @Override
    public int getItemCount() {
        return confirmStatusItems.size();
    }

}
