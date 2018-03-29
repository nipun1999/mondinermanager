package com.example.agarw.mondinermanager_android;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tabbed_menu extends AppCompatActivity {
    private ViewPager mViewPager;
    private int counter;
    private String[] cArray;
    private String[] cIdArray;
    private ArrayList<Tabs> tabsInfo = new ArrayList<>();
    private TextView noItemText;
    TabLayout tabLayout;
    DatabaseReference mMenuRef;
    private DatabaseReference mTableRef;
    private SharedPreferences restaurantIDpref;
    private String codevalue;
    FloatingActionButton fab, addDishFloatingButton, addCuisineFloatingButton;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_menu);
        restaurantIDpref = getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
//        String rest_id = getIntent().getStringExtra("codevalue");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fabLayout1= (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2= (LinearLayout) findViewById(R.id.fabLayout2);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        addDishFloatingButton = (FloatingActionButton) findViewById(R.id.addDishFloatingButton);
        addCuisineFloatingButton = (FloatingActionButton) findViewById(R.id.addCuisineFloatingButton);
        fabBGLayout=findViewById(R.id.fabBGLayout);


        noItemText = findViewById(R.id.no_item_text);
        tabLayout = findViewById(R.id.tabs);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        addCuisineFloatBtn = (FloatingActionButton) (findViewById(R.id.addCuisineFloatButton));
//        addDishFloatBtn = (FloatingActionButton)(findViewById(R.id.addDishFloatBtn));
//        floatingmenu = (FloatingActionMenu)(findViewById(R.id.fab_menu));


        mViewPager = findViewById(R.id.container);
        mMenuRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("menu").child("categories");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });





        mMenuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //         Log.e("Tabbed_Menu:", "in onDataChange" + dataSnapshot.child("info")
                //               .child("servesCuisine").getValue(String.class));
                //String cuisine = dataSnapshot.child("info").child("servesCuisine").getValue(String.class);
                tabsInfo.clear();

                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Tabs tab = new Tabs();
                    tab.setCatID(childSnapshot.getKey());
                    tab.setCatName(childSnapshot.child("title").getValue(String.class));
                    tab.setCatPriority(childSnapshot.child("priorityNos").getValue(String.class));
                    tabsInfo.add(tab);

                }
//                Log.e("Tabbed_Menu:", "in onDataChange" + dataSnapshot.getValue(String.class));
//                String cuisine = dataSnapshot.getValue(String.class);
//                cArray = cuisine.split(",");
//                for(int i=0; i<cArray.length; i++){
//                    cArray[i] = cArray[i].trim();
//                }
//                Arrays.sort(cArray);
//                counter = 0;
//                for (String aCArray : cArray) {
//                    Log.e("Tabbed_Menu", aCArray);
//                    if (aCArray != null) {
//                        Details.CUISINE_ARRAY.add(aCArray);
//                        counter++;
//                        Log.e("Tabbed_Menu :", "counter - " + counter);
//                    }
//                }
//                cIdArray = new String[counter];

//                setupDishesID();
                setupViewPager();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Tabbed_Menu", "DatabaseError onCancelled" + databaseError.toString());
            }
        });




        //fab


    }

    @Override
    protected void onStart(){
        super.onStart();
//
        addDishFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDishintent = new Intent(tabbed_menu.this, HomeActivity.class);
                addDishintent.putExtra("type", "addDish");
                startActivity(addDishintent);
            }
        });

        addCuisineFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCuisineintent = new Intent(tabbed_menu.this, HomeActivity.class);
                addCuisineintent.putExtra("type", "addCuisine");
                startActivity(addCuisineintent);
            }
        });


    }

    private void setupViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        Details.CUISINE_INDEX = 0;
//        for(int i=0; i<tabsInfo.size(); i++){
//            for(int j=i+1; j<tabsInfo.size(); j++){
//                if(Integer.parseInt(tabsInfo.get(i).getCatPriority())>Integer.parseInt(tabsInfo.get(j).getCatPriority())){
//                    Tabs temp = new Tabs();
//                    temp = tabsInfo.get(i);
//                    tabsInfo.re.get(i) = tabsInfo.get(j);
//
//                }
//            }
//        }
        TabFragment tabs[] = new TabFragment[tabsInfo.size()];
        for (int i = 0; i < tabsInfo.size(); i++) {

//            Log.e("Tabbed_Menu -- ", "cArray[i] : " + cArray[i] + "cIdArray : " + cIdArray[i] + " Current Cuisines : " + Details.CUISINE_ID_ARRAY.toString());
            tabs[i] = TabFragment.getInstance(tabsInfo.get(i));
            adapter.addFrag(tabs[i], tabsInfo.get(i).getCatName());
        }
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Log.e("Tabbed_Menu", "no of frags " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
//            noItemText.setText("No" + title + "items to show");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }


    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isFABOpen){
            closeFABMenu();
        }else{
            super.onBackPressed();
        }
    }




}
