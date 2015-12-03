package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.MainMenuViewHolder;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuViewHolder> {
    private final Context mContext;
    private String[] menuItems;
    private final View.OnClickListener listener;

    public MainMenuAdapter(final Context context, final String[] menuItems, final View.OnClickListener listener){
        mContext = context;
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @Override
    public MainMenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_menu_item, viewGroup, false);
        return new MainMenuViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(MainMenuViewHolder holder, int position) {
        holder.setText(menuItems[position]);

        switch (position){
            case 0:
                holder.Image.setImageResource(R.drawable.icon_user);
                break;
            case 1:
                holder.Image.setImageResource(R.drawable.icon_refresh);
                break;
            case 2:
                holder.Image.setImageResource(R.drawable.icon_service);
                break;
            case 3:
                holder.Image.setImageResource(R.drawable.icon_gear);
                break;
            case 4:
                holder.Image.setImageResource(R.drawable.icon_help);
                break;
            case 5:
                holder.Image.setImageResource(R.drawable.icon_support);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }
}
