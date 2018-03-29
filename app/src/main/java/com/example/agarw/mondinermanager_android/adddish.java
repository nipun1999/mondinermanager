package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class adddish extends Fragment {

    private DatabaseReference databaseAddDishRef;
    private EditText nametxt, pricetxt;
    private Button addDishButton;
    private Spinner dishCategorySpinner;
    private String codevalue;
    private String categorynospace;
    private RadioGroup dishTypeRadioGroup;
    private RadioButton typeRdioButton;
    private DatabaseReference databaseCategoryRef;
    private SharedPreferences restaurantIDpref;


    public adddish() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_adddish, container, false);

        nametxt = (EditText) (rootview.findViewById(R.id.nametxt));
        pricetxt = (EditText) (rootview.findViewById(R.id.pricetxt));
        dishCategorySpinner = (Spinner) (rootview.findViewById(R.id.dishcategoryspinner));
        addDishButton = (Button) (rootview.findViewById(R.id.adddishbtn));
        dishTypeRadioGroup = (RadioGroup) (rootview.findViewById(R.id.dishtypegroup));
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        databaseCategoryRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu");
        databaseCategoryRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> category = new ArrayList<String>();
                for (DataSnapshot categorysnapshot : dataSnapshot.getChildren()) {
                    String categoryname = categorysnapshot.child("title").getValue(String.class);
                    if (categoryname != null) {
                        category.add(categoryname);
                    }
                }
                try {
                    ArrayAdapter<String> categoryadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem, category);
                    categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dishCategorySpinner.setAdapter(categoryadapter);
                } catch (Exception e) {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nametxt.getText().toString().trim();
                String price = pricetxt.getText().toString().trim();
                int selectedId = dishTypeRadioGroup.getCheckedRadioButtonId();
                typeRdioButton = (RadioButton) (rootview.findViewById(selectedId));
                String dishtype = typeRdioButton.getText().toString();
                String category = String.valueOf(dishCategorySpinner.getSelectedItem());
                categorynospace = category.replace(" ", "");
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price) && !dishtype.isEmpty()) {
                    databaseAddDishRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("dishes").push();
                    databaseAddDishRef.child("name").setValue(name);
                    databaseAddDishRef.child("price").setValue(price);
                    databaseAddDishRef.child("type").setValue(dishtype);
                    databaseAddDishRef.child("availability").setValue("true");
                    databaseAddDishRef.child("category").setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Your Dish has been added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return rootview;
    }
}
