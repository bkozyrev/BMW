package com.utro.bmw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utro.bmw.BMW;
import com.utro.bmw.server.Server;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehicleOption implements Parcelable{
    String Description;

    public VehicleOption(String description){
        Description = description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public VehicleOption(JSONObject json_categories) {
        try {
            Description = json_categories.getString("descr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public VehicleOption(Parcel in) {
        this.Description = in.readString();
    }

    public static void getAllOptions(final GetListHandler<VehicleOption> handler, String order){
        RequestParams params = new RequestParams();
        Server.get("type=order&uid=" + BMW.mixpanel.getDistinctId() + "&order=" + order, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    onSuccess(statusCode, headers, response.getJSONArray("options"));
                } catch (JSONException e) {
                    onFailure(statusCode, headers, e.getMessage(), e);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray booksJSON) {
                ArrayList<VehicleOption> options = fromJson(booksJSON);

                handler.done(options);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }

    public static ArrayList<VehicleOption> fromJson(JSONArray jsonArray) {
        ArrayList<VehicleOption> ArrayOptions = new ArrayList<VehicleOption>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject newsJson = null;
            try {
                newsJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (newsJson != null) {
                ArrayOptions.add(new VehicleOption(newsJson));
            }
        }

        return ArrayOptions;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public VehicleOption createFromParcel(Parcel in) {
            return new VehicleOption(in);
        }

        @Override
        public VehicleOption[] newArray(int size) {
            return new VehicleOption[size];
        }
    };
}
