package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.AccessoriesCategoriesViewHolder;
import com.utro.bmw.model.Category;

import java.util.ArrayList;

/**
 * Created by 123 on 15.04.2015.
 */
public class AccessoriesCategoriesAdapter extends RecyclerView.Adapter<AccessoriesCategoriesViewHolder> {

    private final Context mContext;
    private ArrayList<Category> menuItems;
    private final View.OnClickListener listener;

    public AccessoriesCategoriesAdapter(final Context context, final ArrayList<Category> menuItems, final View.OnClickListener listener){
        mContext = context;
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @Override
    public AccessoriesCategoriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_menu_item, viewGroup, false);
        return new AccessoriesCategoriesViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(AccessoriesCategoriesViewHolder holder, int position) {
        holder.setText(menuItems.get(position).getTitle());
        //holder.setImage(mContext, menuItems.get(position).getUrl());
        /*switch(position){
            case 0:
                holder.Image.setImageResource(R.drawable.icon_user);
                break;
            case 1:
                holder.Image.setImageResource(R.drawable.clothes);
                break;
            case 2:
                holder.Image.setImageResource(R.drawable.icon_service);
                break;
            case 3:
                holder.Image.setImageResource(R.drawable.icon_gear);
                break;
        }*///TODO icons
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
