package com.example.agarw.mondinermanager_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategory extends Fragment {

    private EditText categoryName;
    private Button addCategoryButton, table;
    private DatabaseReference databaseCategoryReference;
    private String codevalue;
    private SharedPreferences restaurantIDpref;

    public AddCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        final  View rootview = inflater.inflate(R.layout.fragment_add_category, container, false);
        categoryName = (EditText)(rootview.findViewById(R.id.categorytxt));
        addCategoryButton = (Button) (rootview.findViewById(R.id.addbtn));
        databaseCategoryReference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("categories").push();

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category = categoryName.getText().toString();
                databaseCategoryReference.child("title").setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(),"Your Category has been added",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return rootview;
    }


}
