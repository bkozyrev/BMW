package com.utro.bmw.adapters.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utro.bmw.R;

public class MainMenuViewHolder extends RecyclerView.ViewHolder {
    private TextView Title;
    public ImageView Image;

    public MainMenuViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);

        Title = (TextView) itemView.findViewById(R.id.txt_item);
        Image = (ImageView)itemView.findViewById(R.id.img_item);

        itemView.setOnClickListener(listener);
    }

    public void setText(String title){
        Title.setText(title);
    }
}
