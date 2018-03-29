package com.example.agarw.mondinermanager_android;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class offersfragment extends Fragment {

    private RecyclerView offersListRecycler;
    private DatabaseReference offersDatabaseRef;
    private ProgressDialog mprogress;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private FloatingActionButton addOffersBtn;
    public offersfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_offersfragment, container, false);
        restaurantIDpref = this.getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);

//        Intent offers = offers.this.getIntent();
//        codevalue = offers.getStringExtra("codevalue");

        offersListRecycler = (RecyclerView) (rootview.findViewById(R.id.offersrecycler));
        offersListRecycler.setHasFixedSize(true);
        offersListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mprogress = new ProgressDialog(getActivity());
        offersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("offers");
        addOffersBtn = (FloatingActionButton)(rootview.findViewById(R.id.addOffersFrag));
        return rootview;


    }

    @Override
    public void onStart() {
        super.onStart();


        mprogress.setMessage("Loading Data from Server");
        mprogress.show();

        addOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addoffersfragment = new addoffers();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, addoffersfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FirebaseRecyclerAdapter<offersget, offersviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<offersget, offersviewholder>(
                offersget.class,
                R.layout.offerrow,
                offersviewholder.class,
                offersDatabaseRef


        ) {
            @Override
            protected void populateViewHolder(offersviewholder viewHolder, offersget model, final int position) {
                viewHolder.settitle(model.getTitle());
                viewHolder.setdescription(model.getDescription());
                viewHolder.setImage(getActivity(), model.getImageURL());
                final String post_id = getRef(position).getKey().toString();
                mprogress.dismiss();
                viewHolder.deleteOfferButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setCancelable(true);
                        builder.setTitle("Remove Offer ?");
                        builder.setMessage("Are you sure you want to remove this Offer ? ");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        offersDatabaseRef.child(post_id).removeValue();
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
                });


            }


        };


        offersListRecycler.setAdapter(firebaseRecyclerAdapter);


    }

    public static class offersviewholder extends RecyclerView.ViewHolder {
        Button deleteOfferButton;
        View mview;

        public offersviewholder(View itemView) {
            super(itemView);
            mview = itemView;
            deleteOfferButton = (Button)(mview.findViewById(R.id.deleteOfferBtn));
        }


        public void settitle(String title) {
            TextView post_desc = (TextView) (mview.findViewById(R.id.offersrowtitle));
            post_desc.setText(title);
        }

        public void setdescription(String description) {
            TextView post_numb = (TextView) (mview.findViewById(R.id.offersrowdesc));
            post_numb.setText(description);
        }

        public void setImage(Context ctx, String imageURL) {
            ImageView post_image = (ImageView) (mview.findViewById(R.id.offersrowimage));
            Picasso.with(ctx).load(imageURL).into(post_image);
        }


    }

}
