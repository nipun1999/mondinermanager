package com.example.agarw.mondinermanager_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Ishaan.
 */

public class MenuAdapter extends RecyclerView.Adapter<IndianViewHolder> {

    private ArrayList<Menu> menus;
    private ArrayList<String> dishIDs = new ArrayList<>();
    public MenuAdapter(ArrayList<Menu> menuArray, ArrayList<String> dishIDs) {
        menus = menuArray;
        this.dishIDs = dishIDs;

    }

    @Override
    public IndianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newView = layoutInflater.inflate(R.layout.item_indian_menu, parent, false);

        return new IndianViewHolder(newView);
    }


    @Override
    public void onBindViewHolder(final IndianViewHolder holder, final int position) {
        holder.populate(menus.get(position), dishIDs.get(position));
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
}
