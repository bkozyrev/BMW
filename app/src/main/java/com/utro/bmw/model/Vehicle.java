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

public class Vehicle implements Parcelable{

    private String Title, Url, Price, Order;
    private static String URL_FIRST_PART = "http://appvillage.ru/bmw/api/get_info.php?type=image&order=";
    private static String URL_SECOND_PART = "&width=320&angle=70";

    public Vehicle(String title, String url, String price, String order) {
        Title = title;
        Url = url;
        Price = price;
        Order = order;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }

    public String getOrder() {
        return Order;
    }

    public String getPrice() {
        return Price;
    }

    public Vehicle(Parcel in) {
        this.Title = in.readString();
        this.Url = in.readString();
        this.Price = in.readString();
        this.Order = in.readString();
    }

    public Vehicle(JSONObject json_categories)
    {
        try {
            Title = json_categories.getString("model_descr");
            Order = json_categories.getString("order");
            Price = json_categories.getString("price");
            Url = URL_FIRST_PART + Order + URL_SECOND_PART;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getAllVehiclesOrders(final GetListHandler<Vehicle> handler, String series)
    {
        RequestParams params = new RequestParams();
        Server.get("type=autos&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId() + "&series=" + series + "&demos=no", params, new JsonHttpResponseHandler() {
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
                ArrayList<Vehicle> vehicles = fromJson(booksJSON);

                handler.done(vehicles);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }

    public static void getAllVehiclesDemos(final GetListHandler<Vehicle> handler)
    {
        RequestParams params = new RequestParams();
        Server.get("type=autos&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId() + "&demo=yes", params, new JsonHttpResponseHandler() {
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
                ArrayList<Vehicle> vehicles = fromJson(booksJSON);

                handler.done(vehicles);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }


    public static ArrayList<Vehicle> fromJson(JSONArray jsonArray) {
        ArrayList<Vehicle> ArrayVehicles = new ArrayList<Vehicle>(jsonArray.length());
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
                ArrayVehicles.add(new Vehicle(newsJson));
            }

        }

        return ArrayVehicles;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Title);
        parcel.writeString(Url);
        parcel.writeString(Price);
        parcel.writeString(Order);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
