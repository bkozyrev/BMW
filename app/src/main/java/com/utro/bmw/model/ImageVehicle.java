package com.utro.bmw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utro.bmw.BMW;
import com.utro.bmw.server.Server;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 123 on 17.05.2015.
 */
public class ImageVehicle implements Parcelable{
    private String Url;
    private int Angle;

    public String getUrl() {
        return Url;
    }

    public int getAngle() {
        return Angle;
    }

    public ImageVehicle(String url, int angle){
        Url = url;
        Angle = angle;
    }

    public ImageVehicle(JSONObject json_object) //TODO wait for server
    {
        /*try {
            Title = json_categories.getString("model_descr");
            Order = json_categories.getString("order");
            Price = json_categories.getString("price") + "$";
            for(int i = 0; i < 360; i += 10){
                Angle =
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public static void getImages(final GetListHandler<ImageVehicle> handler, String order, int width) {
        RequestParams params = new RequestParams();
        //String Order = "4114729";
        Server.get("type=image_urls&uid=" + BMW.mixpanel.getDistinctId() + "&order=" + order + "&width=" + width, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                /*try {
                    onSuccess(statusCode, headers, response.getJSONArray("items"));
                } catch (JSONException e) {
                    onFailure(statusCode, headers, e.getMessage(), e);
                }*/
                ArrayList<ImageVehicle> vehicles = fromJson(response);
                handler.done(vehicles);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray booksJSON) {
                //ArrayList<Vehicle> vehicles = fromJson(booksJSON);

                //handler.done(vehicles);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }

    public static ArrayList<ImageVehicle> fromJson(JSONObject jsonObject) {
        ArrayList<ImageVehicle> ArrayImages = new ArrayList<>(jsonObject.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < 360; i += 10) {
            String url;
            try {
                url = jsonObject.getString("" + i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            ArrayImages.add(new ImageVehicle(url, i));
        }

        return ArrayImages;
    }

    public ImageVehicle(Parcel in) {
        this.Url = in.readString();
        this.Angle = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Url);
        parcel.writeInt(Angle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public ImageVehicle createFromParcel(Parcel in) {
            return new ImageVehicle(in);
        }

        @Override
        public ImageVehicle[] newArray(int size) {
            return new ImageVehicle[size];
        }
    };
}
