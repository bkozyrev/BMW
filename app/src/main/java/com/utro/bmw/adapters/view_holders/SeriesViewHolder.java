package com.utro.bmw.adapters.view_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.R;

/**
 * Created by 123 on 02.06.2015.
 */
public class SeriesViewHolder extends RecyclerView.ViewHolder {

    TextView Title;
    ImageView Image;

    public SeriesViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);

        Title = (TextView)itemView.findViewById(R.id.service_title);
        Image = (ImageView)itemView.findViewById(R.id.series_img);

        itemView.setOnClickListener(listener);
    }

    public void setTitle(String title){
        Title.setText("BMW " + title + " серии");
    }

    public void setImage(Context context, String url){
        Glide.with(context).load(url).into(Image);
    }
}
