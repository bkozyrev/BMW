package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.SeriesViewHolder;
import com.utro.bmw.model.Series;

import java.util.ArrayList;

/**
 * Created by 123 on 02.06.2015.
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesViewHolder> {
    private final Context mContext;
    private final View.OnClickListener listener;
    private final ArrayList<Series> series;

    public SeriesAdapter(final Context context, ArrayList<Series> series, final View.OnClickListener listener){
        mContext = context;
        this.listener = listener;
        this.series = series;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.series_item, viewGroup, false);
        return new SeriesViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, int position) {
        holder.setTitle(series.get(position).getTitle());
        holder.setImage(mContext, series.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return series.size();
    }
}
