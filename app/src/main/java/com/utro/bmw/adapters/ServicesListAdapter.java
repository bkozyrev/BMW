package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.ServicesListViewHolder;
import com.utro.bmw.model.Service;

import java.util.ArrayList;

/**
 * Created by 123 on 26.06.2015.
 */
public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListViewHolder>{

    private final Context mContext;
    private final View.OnClickListener listener;
    private final ArrayList<Service> services;

    public ServicesListAdapter(final Context context, ArrayList<Service> services, final View.OnClickListener listener){
        mContext = context;
        this.listener = listener;
        this.services = services;
    }

    @Override
    public ServicesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.service_item, viewGroup, false);
        return new ServicesListViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ServicesListViewHolder holder, int position) {
        holder.setDate(services.get(position).getDate());
        holder.setTime(services.get(position).getTime());
        holder.setStatus(services.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
