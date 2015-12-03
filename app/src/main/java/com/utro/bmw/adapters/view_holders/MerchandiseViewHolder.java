package com.utro.bmw.adapters.view_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.utils.HttpsImageLoader;
import com.utro.bmw.R;

public class MerchandiseViewHolder extends RecyclerView.ViewHolder{
    private TextView Title, Price, Description;
    private ImageView Image;

    public MerchandiseViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);

        Title = (TextView) itemView.findViewById(R.id.merch_item_title);
        Description = (TextView) itemView.findViewById(R.id.merch_item_descr);
        Price = (TextView) itemView.findViewById(R.id.merch_item_price);
        Image = (ImageView)itemView.findViewById(R.id.merch_item_img);

        itemView.setOnClickListener(listener);
    }

    public void setTitle(String title){
        Title.setText(title);
    }

    public void setDescription(String description){
        String tempDescription = description;
        if(tempDescription.length() > 100) {
            tempDescription = tempDescription.substring(0, 100) + "...";
        }
        Description.setText(tempDescription);
    }

    public void setPrice(String price){
        Price.setText(price);
    }

    public void setImage(Context context, String url) {

        HttpsImageLoader.trustAllHosts();
        Glide.with(context).load(url).into(Image);

    }
}
