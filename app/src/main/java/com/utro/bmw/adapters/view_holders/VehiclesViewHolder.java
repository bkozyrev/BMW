package com.utro.bmw.adapters.view_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.R;

public class VehiclesViewHolder extends RecyclerView.ViewHolder {
    private TextView Title, Price;
    private ImageView Image;

    public VehiclesViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);

        Title = (TextView) itemView.findViewById(R.id.vehicle_item_description);
        Price = (TextView) itemView.findViewById(R.id.vehicle_item_price);
        Image = (ImageView)itemView.findViewById(R.id.vehicle_item_img);

        itemView.setOnClickListener(listener);
    }

    public void setTitle(String title){
        Title.setText(title);
    }

    public void setPrice(String price){
        Price.setText(price);
    }

    public void setImage(Context context, String url) {

        Glide.with(context)
                .load(url)
                .into(Image);
        //Image.setImageResource(R.drawable.vehicle_1);
    }
}
