package com.utro.bmw.adapters.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.utro.bmw.R;

/**
 * Created by 123 on 26.06.2015.
 */
public class ServicesListViewHolder extends RecyclerView.ViewHolder {

    TextView date, time, status;

    public ServicesListViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);

        date = (TextView)itemView.findViewById(R.id.service_date);
        time = (TextView)itemView.findViewById(R.id.service_time);
        status = (TextView)itemView.findViewById(R.id.service_status);

        itemView.setOnClickListener(listener);
    }

    public void setDate(String date){
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMDD");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy.MM.DD");
        String finalDate = timeFormat.format(myDate);*/

        this.date.setText(date);
    }

    public void setTime(String time){
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMDD");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy.MM.DD");
        String finalDate = timeFormat.format(myDate);*/

        this.time.setText(time);
    }

    public void setStatus(String status){
        this.status.setText(status);
    }
}
