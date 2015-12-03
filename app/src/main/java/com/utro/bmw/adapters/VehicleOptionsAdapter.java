package com.utro.bmw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utro.bmw.R;
import com.utro.bmw.model.VehicleOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 25.04.2015.
 */
public class VehicleOptionsAdapter extends BaseAdapter{

    Context context;

    protected List<VehicleOption> listOptions;
    LayoutInflater inflater;

    public VehicleOptionsAdapter(Context context, ArrayList<VehicleOption> listOptions){
        this.listOptions = listOptions;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public int getCount(){
        return listOptions.size();
    }
    public VehicleOption getItem(int position){
        return listOptions.get(position);
    }
    public long getItemId(int position){
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.vehicle_option_item, parent, false);
            holder.Description = (TextView)convertView.findViewById(R.id.option_description);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.Description.setText(listOptions.get(position).getDescription());

        return convertView;
    }
    private class ViewHolder{
        TextView Description;
    }
}
