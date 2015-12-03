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

public class Category implements Parcelable {
    private String Title, Url;

    public Category(String title, String url) {
        Title = title;
        Url = url;
    }

    public Category(Parcel in) {
        Title = in.readString();
        Url = in.readString();
    }

    public Category(JSONObject json_categories)
    {
        try {
            Url = json_categories.getString("picURL");
            Title = json_categories.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public static void getAllCategories(final GetListHandler<Category> handler)
    {
        RequestParams params = new RequestParams();
        Server.get("type=categories&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId(), params, new JsonHttpResponseHandler() {//TODO поменять url
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
                ArrayList<Category> categories = fromJson(booksJSON);

                handler.done(categories);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }

    public static ArrayList<Category> fromJson(JSONArray jsonArray) {
        ArrayList<Category> ArrayCategories = new ArrayList<Category>(jsonArray.length());
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
                ArrayCategories.add(new Category(newsJson));
            }

        }

        return ArrayCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Title);
        parcel.writeString(Url);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

}
