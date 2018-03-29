package com.example.agarw.mondinermanager_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class offers extends AppCompatActivity {

    private RecyclerView offersListRecycler;
    private DatabaseReference databaseOffersRef;
    private ProgressDialog mprogress;
    private String codevalue;
    private SharedPreferences restaurantIDpref;
    private FloatingActionButton addOffersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        addOffersBtn = (FloatingActionButton)(findViewById(R.id.addoffers));
        restaurantIDpref = getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        offersListRecycler = (RecyclerView) (findViewById(R.id.offersrecycler));
        offersListRecycler.setHasFixedSize(true);
        offersListRecycler.setLayoutManager(new LinearLayoutManager(this));
        mprogress = new ProgressDialog(offers.this);
        databaseOffersRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("offers");

    }

    @Override
    protected void onStart() {
        super.onStart();

        addOffersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new offersfragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mprogress.setMessage("Loading Data from Server");
        mprogress.show();


        FirebaseRecyclerAdapter<offersget, offers.offersviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<offersget, offersviewholder>(
                offersget.class,
                R.layout.offerrow,
                offers.offersviewholder.class,
                databaseOffersRef


        ) {
            @Override
            protected void populateViewHolder(offers.offersviewholder viewHolder, offersget model, int position) {
                viewHolder.settitle(model.getTitle());
                viewHolder.setdescription(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImageURL());
                mprogress.dismiss();

            }


        };


        offersListRecycler.setAdapter(firebaseRecyclerAdapter);


    }

    public static class offersviewholder extends RecyclerView.ViewHolder {
        View mview;


        public offersviewholder(View itemView) {
            super(itemView);
            mview = itemView;
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
