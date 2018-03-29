package com.example.agarw.mondinermanager_android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    private RecyclerView indianMenu;
    private TextView noItemText;
    private Tabs tabInfo = new Tabs();
    private com.google.firebase.database.Query mMenuRef;
    private DatabaseReference mTableRef;
    private MenuAdapter menuAdapter;
    private ArrayList<Menu> menus = new ArrayList<>();
    private ArrayList<String> dishIDs = new ArrayList<>();
    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment getInstance(Tabs tabInfo) {
        TabFragment tab = new TabFragment();
        tab.tabInfo = tabInfo;
        return tab;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        indianMenu = rootView.findViewById(R.id.indian_menu_rv);
        noItemText = rootView.findViewById(R.id.no_item_text);
        indianMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        noItemText.setText("No items to display for the cuisine : " + tabInfo.getCatName());
        Log.e("TabFragment", "" + Details.REST_ID + "current_cuisine : " + Details.Current_Cuisine);
        Log.e("TabFragment", "Cuisine id index " + Details.CUISINE_INDEX);
        mMenuRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(Details.REST_ID).child("menu").child("dishes").orderByChild("category").equalTo(tabInfo.getCatName());
        mTableRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child("redChillies").child("table").child(Details.TABLE_ID).child("currentOrder")/*.child("dishes")*/.child("cart");
        menuAdapter = new MenuAdapter(menus, dishIDs);
        indianMenu.setAdapter(menuAdapter);
        return rootView;




    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        mMenuRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menus.clear();
                dishIDs.clear();
                //TODO : Handle null pointer exception for price and type
                for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.child("name").exists() &&
                            childSnapshot.child("price").exists() &&
                            childSnapshot.child("type").exists()) {
                        Menu m = new Menu();
                        dishIDs.add(childSnapshot.getKey());
                        m.setItemName(childSnapshot.child("name").getValue(String.class));
                        m.setItemPrice(childSnapshot.child("price").getValue(String.class));
                        m.setVegNonVeg(childSnapshot.child("type").getValue(String.class));
                        m.setAvailability(childSnapshot.child("availability").getValue(String.class));
                        menus.add(m);
                    }
                }
                if (menus.size() != 0) {
                    noItemText.setVisibility(View.GONE);
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TabFragment", "on cancelled" + databaseError.toString());
            }
        });

    }




}
