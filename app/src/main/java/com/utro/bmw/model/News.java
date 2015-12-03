package com.utro.bmw.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.utro.bmw.BMW;
import com.utro.bmw.server.Server;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Egor on 12.04.2015.
 */
public class News implements Parcelable{

    private String Id;
    private String Title;
    private String Img_url;
    private Bitmap Img_bitmap;
    private String Description;

    public News() {
    }

    public News(String id, String title, String img_url, String description) {
        Id = id;
        Title = title;
        Img_url = img_url;
        Description = description;
    }

    public News(JSONObject json_news)
    {
            try {
                Id = json_news.getString("id");
            } catch (JSONException e) {
                Id = "";
                e.printStackTrace();
            }
            try{
                Title = json_news.getString("title");
            } catch (JSONException e) {
                Title = "";
                e.printStackTrace();
            }
            try{
                Img_url=json_news.getString("img_url");
            } catch (JSONException e) {
                Img_url = "";
                e.printStackTrace();
            }
            try{
                Description=json_news.getString("body");
            } catch (JSONException e) {
                Description = "";
                e.printStackTrace();
            }
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImg_url() {
        return Img_url;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Bitmap getImg_bitmap() {
        return Img_bitmap;
    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        Img_bitmap = img_bitmap;
    }

    public static void getAllNews(final GetListHandler<News> handler)
    {
        RequestParams params = new RequestParams();
        Server.get("type=news&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId(), params, new JsonHttpResponseHandler() {
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
            public void onSuccess(int statusCode, Header[] headers, JSONArray newsJSON) {
                ArrayList<News> news = fromJson(newsJSON);

                handler.done(news);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

        });
    }

    public static void getCurrentNews(final GetItemHandler<News> handler, final String id){
        RequestParams params = new RequestParams();
        Server.get("news_id=" + id + "&dealerid=25444&uid=" + BMW.mixpanel.getDistinctId() + "&view=html", params, new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.error(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                handler.done(new News(id, null, null, responseString));
            }
        });
    }

    public static ArrayList<News> fromJson(JSONArray jsonArray) {
        ArrayList<News> ArrayNews = new ArrayList<News>(jsonArray.length());
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
                ArrayNews.add(new News(newsJson));
            }

        }

        return ArrayNews;
    }

    public News(Parcel in) {
        this.Id = in.readString();
        this.Title = in.readString();
        this.Img_url = in.readString();
        this.Description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Id);
        parcel.writeString(Title);
        parcel.writeString(Img_url);
        parcel.writeString(Description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
