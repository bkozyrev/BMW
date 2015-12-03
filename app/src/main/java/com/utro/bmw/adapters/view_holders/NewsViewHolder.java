package com.utro.bmw.adapters.view_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.R;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NewsViewHolder extends RecyclerView.ViewHolder{
    private ImageView Image;
    private TextView Title;
    private CircularProgressBar bar;

    public NewsViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);
        Image = (ImageView) itemView.findViewById(R.id.img);
        Title = (TextView) itemView.findViewById(R.id.header);
        bar = (CircularProgressBar) itemView.findViewById(R.id.bar);

        itemView.setOnClickListener(listener);
    }


    public void setImage(Context context, String url) {
        Glide.with(context)
                .load(url).crossFade(1000)
                .into(Image);
    }


    public void setTitle(String title) {
        Title.setText(title);
    }

    public void cancelBar()
    {
        bar.setVisibility(View.GONE);
    }

    public void startBar()
    {
        bar.setVisibility(View.VISIBLE);
    }

}