package com.example.agarw.mondinermanager_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView = null;
    Toolbar toolbarhome = null;

    private Button submitt;
    private String codevalue;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference databaseOwnerReference;
    private SharedPreferences restaurantIDpref;
    private ImageView imageViewNav;
    private DatabaseReference databaseInfoRef;
    private TextView navTextView;
    private StorageReference mStorage;
    private String intentCodeAddDish;
    private String intentCodeAddCuisine;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private String[] pageTitle = {"Tables", "Confirm Order", "Call Waiter"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        restaurantIDpref = getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        toolbarhome = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbarhome);


        mAuth = FirebaseAuth.getInstance();
        databaseOwnerReference = FirebaseDatabase.getInstance().getReference().child("owners");


        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent login = new Intent(HomeActivity.this, login.class);
                    startActivity(login);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthlistener);
        mStorage = FirebaseStorage.getInstance().getReference();

        databaseOwnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid().toString())) {
                        if (codevalue == null) {
                            Intent codepage = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(codepage);
                        }
                    } else {
                        Intent registerpage = new Intent(HomeActivity.this, register.class);
                        startActivity(registerpage);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//         setSupportActionBar(toolbar);


        //set our fragment initially




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with yo
// ur own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarhome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });








    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbarhome.setTitle("Tables");

        Details.REST_ID = codevalue;

        try{
            databaseInfoRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("info");
            databaseInfoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageURL = dataSnapshot.child("imageURL").getValue().toString();
                    imageViewNav = (ImageView)(findViewById(R.id.imageView));
                    navTextView = (TextView) (findViewById(R.id.textViewNav));
                    try{
                        navTextView.setText(dataSnapshot.child("name").getValue().toString());
                        Glide.with(getApplicationContext()).load(imageURL).into(imageViewNav);
                    }catch (Exception e){

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }




        Intent addDishIntent = getIntent();
        String type;
        type = addDishIntent.getStringExtra("type");


        if(type != null){
            if(type.equals("addDish")){
                adddish fragment = new adddish();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                toolbarhome.setTitle("Add Dish");

            }else if(type.equals("addCuisine")){
                AddCategory fragment = new AddCategory();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                toolbarhome.setTitle("Add Cuisine");

            }
        }else{
            if (codevalue != null) {
//                table fragment = new table();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                fragmentTransaction.commit();
//                toolbar.setTitle("Tables");

            }
        }





    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void initListener() {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addusernav) {


            Adddetails fragment = new Adddetails();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            toolbarhome.setTitle("Profile");

            // Handle the camera action

        } else if (id == R.id.addoffersnav) {
            offersfragment fragment = new offersfragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbarhome.setTitle("Offers");

        } else if (id == R.id.menunav) {

            Intent menu = new Intent(HomeActivity.this, tabbed_menu.class);
            startActivity(menu);
            toolbarhome.setTitle("Menu");

        } else if (id == R.id.signoutnav) {

            mAuth.signOut();
            restaurantIDpref = getSharedPreferences("myPref", Context.
                    MODE_WORLD_WRITEABLE);
            restaurantIDpref.edit().remove("restaurantID").commit();
        } else if (id == R.id.changerestaurantnav) {
            restaurantIDpref = getSharedPreferences("myPref", Context.
                    MODE_WORLD_WRITEABLE);
            restaurantIDpref.edit().remove("restaurantID").commit();
            Intent codepage = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(codepage);
        } else if (id == R.id.sortedtablesnav) {

            Intent tablessorted = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(tablessorted);
        } else if (id == R.id.addOnNav){
            addOnFrag fragmentaddOn = new addOnFrag();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragmentaddOn);
            fragmentTransaction.commit();
            toolbarhome.setTitle("Add Ons");
        }else if (id == R.id.orderHistoryNav){
            orderHistoryFrag fragment = new orderHistoryFrag();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbarhome.setTitle("Order History");


//            Intent orderHistory = new Intent(HomeActivity.this, orderHistory.class);
//            startActivity(orderHistory);
//            toolbarhome.setTitle("Order History");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

}
