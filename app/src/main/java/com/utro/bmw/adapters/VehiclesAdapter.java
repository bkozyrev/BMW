package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.VehiclesViewHolder;
import com.utro.bmw.model.Vehicle;

import java.util.ArrayList;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesViewHolder> {

    private final Context mContext;
    private ArrayList<Vehicle> menuItems;
    private final View.OnClickListener listener;
    private boolean isTestDrive;

    public VehiclesAdapter(final Context context, final ArrayList<Vehicle> menuItems, final View.OnClickListener listener, boolean isTestDrive){
        mContext = context;
        this.menuItems = menuItems;
        this.listener = listener;
        this.isTestDrive = isTestDrive;
    }

    @Override
    public VehiclesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if(isTestDrive){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_drive_item, viewGroup, false);
        }else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicles_item, viewGroup, false);
        }
        return new VehiclesViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(VehiclesViewHolder holder, int position) {
        holder.setTitle(menuItems.get(position).getTitle());
        holder.setImage(mContext, menuItems.get(position).getUrl());

        if(!isTestDrive){
            holder.setPrice(menuItems.get(position).getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
