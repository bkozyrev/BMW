package com.utro.bmw.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.utro.bmw.BMW;
import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.NewsListAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.News;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;


public class NewsActivity extends BaseActivity implements  View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    static String KEY = "News_key";
    ArrayList<News> Array;
    boolean mLoading = false;

    private RecyclerView list;
    //SwipeRefreshLayout refreshLayout;

    NewsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        mBar = (CircularProgressBar) findViewById(R.id.bar);
        mBar.setVisibility(View.VISIBLE);

        setToolBarThemeWhite();

        //refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        //refreshLayout.setOnRefreshListener(this);

        int Columns = 1;
        String str = BMW.getDisplayColumns(this);
        switch(str){
            case "large":
                switch(getScreenOrientation()){
                    case Configuration.ORIENTATION_PORTRAIT:
                        Columns = 2;
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        Columns = 3;
                        break;
                }
                break;
            case "normal":
                Columns = 1;
                break;
        }

        list = (RecyclerView) findViewById(R.id.news_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new StaggeredGridLayoutManager(Columns, StaggeredGridLayoutManager.VERTICAL));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new NewsListAdapter(mContext, new ArrayList<News>(), this);
        list.setAdapter(mAdapter);

        setAnimation(list, mToolBar, mShadowView);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Array = savedInstanceState.getParcelableArrayList("news");
        mLoading = savedInstanceState.getBoolean("loading");
        Log.d("BMWLOG", "onRestoreInstanceState");
    }


    @Override
    protected void onResume(){
        super.onResume();


        if(Array==null){
            if(!mLoading) {
                loadNews();
            }
        }else{
            mAdapter.clear();
            mAdapter.add(Array);
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("news", Array);
        outState.putBoolean("loading", mLoading);
        Log.d("BMWLOG", "onSaveInstanceState");
    }


    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_news;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_news);
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return true;
    }

    @Override
    protected boolean getDisplayShowTitleEnabled() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.item_news:
                int position = list.getChildLayoutPosition(v);
                Intent intent = new Intent(NewsActivity.this, CurrentNewsActivity.class);
                intent.putExtra("IMG", ((GlideBitmapDrawable)((ImageView)v.findViewById(R.id.img)).getDrawable()).getBitmap());
                intent.putExtra(KEY, mAdapter.getItem(position));
                startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNews()
    {
        News.getAllNews(new GetListHandler<News>() {
            @Override
            public void done(ArrayList<News> data) {
                mBar.setVisibility(View.GONE);
                //refreshLayout.setRefreshing(false);
                Array = data;
                //mAdapter.clear();
                mAdapter.add(data);
                mLoading = false;

            }

            @Override
            public void error(String responseError) {
                mBar.setVisibility(View.GONE);
                Log.e("News loading error: ", responseError);
                mLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        //refreshLayout.setRefreshing(true);
        mAdapter.clear();
        loadNews();
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }
}
