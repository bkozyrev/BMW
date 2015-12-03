package com.utro.bmw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utro.bmw.R;
import com.utro.bmw.adapters.view_holders.NewsViewHolder;
import com.utro.bmw.model.News;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder>{

    private final Context mContext;
    private ArrayList<News> news;
    private final View.OnClickListener listener;

    public NewsListAdapter(final Context context, final ArrayList<News> news, final View.OnClickListener listener){
        mContext = context;
        this.news = news;
        this.listener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setTitle(news.get(position).getTitle());
        holder.setImage(mContext, news.get(position).getImg_url());
        holder.cancelBar();
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void clear(){
        if(news.size()!=0)
            for(int i=news.size();i>=1;i--) {
                news.remove(i - 1);
                notifyItemRemoved(i-1);
            }

    }

    public void add(ArrayList<News> newsData) {
        int s = news.size();
        for(int i=news.size();i<s+newsData.size();i++) {
            news.add(newsData.get(i-s));
            notifyItemInserted(news.size());
        }
    }

    public News getItem(int position)
    {
        return news.get(position);
    }

}


