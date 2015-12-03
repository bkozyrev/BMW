package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.MerchandiseViewHolder;
import com.utro.bmw.model.Merchandise;

import java.util.ArrayList;

/**
 * Created by 123 on 25.04.2015.
 */
public class MerchandiseAdapter extends RecyclerView.Adapter<MerchandiseViewHolder> {
    private final Context mContext;
    private ArrayList<Merchandise> menuItems;
    private final View.OnClickListener listener;

    public MerchandiseAdapter(final Context context, final ArrayList<Merchandise> menuItems, final View.OnClickListener listener){
        mContext = context;
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @Override
    public MerchandiseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.merch_item, viewGroup, false);
        return new MerchandiseViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(MerchandiseViewHolder holder, int position) {
        holder.setTitle(menuItems.get(position).getTitle());
        holder.setImage(mContext, menuItems.get(position).getUrl());
        holder.setPrice(menuItems.get(position).getPrice());
        holder.setDescription(menuItems.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
