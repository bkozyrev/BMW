package com.utro.bmw.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utro.bmw.BMW;
import com.utro.bmw.server.Server;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 123 on 26.06.2015.
 */
public class Service {

    private String Date, Time, Status;

    public Service(String date, String time, String status){
        Date = date;
        Time = time;
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Service(JSONObject json_categories)
    {
        String year, month, day, hour, minute;
        try {
            year = json_categories.getString("order_date_year");
            month = json_categories.getString("order_date_month");
            day = json_categories.getString("order_date_day");
            hour = json_categories.getString("order_date_hour");
            minute = json_categories.getString("order_date_minute");
            Date = year + "." + month + "." + day;
            Time = hour + ":" + minute;
            Status = json_categories.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getAllServices(final GetListHandler<Service> handler)
    {
        RequestParams params = new RequestParams();
        Log.d("ServicesListActivity", "type=service&action=list&uid=" + BMW.mixpanel.getDistinctId());
        Server.get("type=service&action=list&uid=" + BMW.mixpanel.getDistinctId(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    onSuccess(statusCode, headers, response.getJSONArray("items"));
                } catch (JSONException e) {
                    onFailure(statusCode, headers, e.getMessage(), e);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray booksJSON) {
                ArrayList<Service> services = fromJson(booksJSON);

                handler.done(services);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
                handler.error(responseString + "&" + statusCode);
            }

        });
    }

    public static void postOrder(String firstName, String surName, String middlename, String phone, String email, String date)
    {
        RequestParams params = new RequestParams();
        Server.get("uid=" + BMW.mixpanel.getDistinctId() + "&type=service&action=add&dealerid=25444&firstname=" + firstName + "&middlename=" + middlename + "&lastname=" + surName + "&phone=" + phone + "&email=" + email + "&date=" + date, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("postOrder", "1" + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray booksJSON) {
                Log.d("postOrder", "2" + statusCode + booksJSON.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("postOrder", "3" + statusCode + " " + responseString);
            }
        });
    }


    public static ArrayList<Service> fromJson(JSONArray jsonArray) {
        ArrayList<Service> ArrayServices = new ArrayList<Service>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject newsJson = null;
            try {
                newsJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (newsJson != null) {
                ArrayServices.add(new Service(newsJson));
            }

        }

        return ArrayServices;
    }
}
