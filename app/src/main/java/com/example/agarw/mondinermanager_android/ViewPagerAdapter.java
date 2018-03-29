package com.example.agarw.mondinermanager_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by agarw on 2/19/2018.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new AllTables();
        } else if (position == 1) {
            return new confirmTables();
        } else return new callwaiter();
    }

    @Override
    public int getCount() {
        return 3;
    }




}
