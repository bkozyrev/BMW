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

/**
 * Created by 123 on 25.04.2015.
 */
public class Merchandise implements Parcelable {

    private String Title, Url, Price, Description;

    public Merchandise(String title, String url, String price, String description) {
        Title = title;
        Url = url;
        Price = price;
        Description = description;
    }

    public Merchandise(Parcel in) {
        this.Title = in.readString();
        this.Url = in.readString();
        this.Price = in.readString();
        this.Description = in.readString();
    }

    public Merchandise(JSONObject json_categories)
    {
        try {
            Url = json_categories.getString("picURL");
            Title = json_categories.getString("name");
            Description = json_categories.getString("descr");
            Price = json_categories.getString("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl() {
        return Url;
    }

    public String getPrice() {
        return Price;
    }



    public static void getAllMerch(final GetListHandler<Merchandise> handler, int categoryNumber)
    {
        if(categoryNumber != -1) {
            RequestParams params = new RequestParams();
            Server.get("type=accessories&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId() + "&category=" + categoryNumber, params, new JsonHttpResponseHandler() {//TODO поменять url
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
                    ArrayList<Merchandise> accessories = fromJson(booksJSON);

                    handler.done(accessories);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    handler.error(responseString);
                }

            });
        }
    }

    public static ArrayList<Merchandise> fromJson(JSONArray jsonArray) {
        ArrayList<Merchandise> ArrayAccessories = new ArrayList<Merchandise>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject accessoriesJson = null;
            try {
                accessoriesJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (accessoriesJson != null) {
                ArrayAccessories.add(new Merchandise(accessoriesJson));
            }

        }

        return ArrayAccessories;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Title);
        parcel.writeString(Url);
        parcel.writeString(Price);
        parcel.writeString(Description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Merchandise createFromParcel(Parcel in) {
            return new Merchandise(in);
        }

        @Override
        public Merchandise[] newArray(int size) {
            return new Merchandise[size];
        }
    };
}
