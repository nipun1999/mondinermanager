package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
public class addOnFragment extends Fragment {

    Vector<getAddOnStatus> addOnItemsVector = new Vector<>();
    private DatabaseReference databaseAddOnRef;
    private addOnAdapter adapter;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private RecyclerView addOnRecyclerView;



    public addOnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_add_on, container, false);

        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);

        databaseAddOnRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("dishes");
        addOnRecyclerView = (RecyclerView) (rootview.findViewById(R.id.addOnRecycler));
        addOnRecyclerView.setHasFixedSize(true);
        addOnRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new addOnAdapter(addOnItemsVector, getActivity());
        addOnRecyclerView.setAdapter(adapter);

        return rootview;
    }
    @Override
    public void onStart(){
        super.onStart();

        databaseAddOnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addOnItemsVector.clear();
                for (final DataSnapshot dishid : dataSnapshot.getChildren()){
                    final getAddOnStatus addOn = new getAddOnStatus();
                    try{
                        if(dishid.child("addOn").equals("true")){
                            addOn.setName(dishid.child("name").getValue().toString());
                            addOn.setCategory(dishid.child("category").getValue().toString());
                            addOnItemsVector.add(addOn);
                        }
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
