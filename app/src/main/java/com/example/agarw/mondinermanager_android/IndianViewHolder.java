package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by agarw on 1/26/2018.
 */

public class IndianViewHolder extends RecyclerView.ViewHolder {

    private final DatabaseReference dishesDatabaseRef;
    private TextView itemNameTxt;
    private TextView itemPriceTxt;
    private TextView itemVegTxt;
    private Button decrement;
    private Button increment;
    private TextView quantityDisplay;
    private com.example.agarw.mondinermanager_android.Menu menu;
    private String dishId;
    private Switch availability;
    private SharedPreferences restaurantIDpref;
    private String codevalue;
    private Button editButton;



    public IndianViewHolder(final View itemView) {
        super(itemView);

        restaurantIDpref = itemView.getContext().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);


        itemNameTxt = itemView.findViewById(R.id.item_name_indian);
        itemPriceTxt = itemView.findViewById(R.id.item_price_indian);
        itemVegTxt = itemView.findViewById(R.id.item_veg_indian);
        availability = itemView.findViewById(R.id.availabilityswitch);
        editButton = itemView.findViewById(R.id.editButton);
        this.dishesDatabaseRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("dishes");

        //.child("dishes");
        availability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    dishesDatabaseRef.child(dishId).child("availability").setValue("true");
                } else {
                    dishesDatabaseRef.child(dishId).child("availability").setValue("false");
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updatedish = new Intent(itemView.getContext(), updateDish.class);
                updatedish.putExtra("dishid", dishId);
                itemView.getContext().startActivity(updatedish);

            }
        });

    }


    public void populate(final com.example.agarw.mondinermanager_android.Menu menu, final String dishId) {

        this.menu = menu;
        this.dishId = dishId;
        itemNameTxt.setText(menu.getItemName());
        itemPriceTxt.setText(menu.getItemPrice());
        itemVegTxt.setText(menu.getVegNonVeg());

        try {
            if (menu.getAvailability().equals("true")) {
                availability.setChecked(true);
            } else {
                availability.setChecked(false);
            }
        } catch (Exception e) {

        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dishesDatabaseRef.child(dishId).child("addOn").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if(dataSnapshot.getValue().toString().equals("true")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Already in AddOns");
                                builder.setMessage("The selected dish is already in AddOns, do you want to delete ?");
                                builder.setPositiveButton("Delete",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dishesDatabaseRef.child(dishId).child("addOn").setValue("false");
                                                Toast.makeText(itemView.getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Add to AddOns");
                                builder.setMessage("Are You Sure You Want to Add to AddOns ?");
                                builder.setPositiveButton("Add",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dishesDatabaseRef.child(dishId).child("addOn").setValue("true");
                                                Toast.makeText(itemView.getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }





}

