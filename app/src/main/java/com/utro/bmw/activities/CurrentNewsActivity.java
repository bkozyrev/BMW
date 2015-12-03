package com.utro.bmw.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.utro.bmw.R;
import com.utro.bmw.model.GetItemHandler;
import com.utro.bmw.model.News;

/**
 * Created by 123 on 29.05.2015.
 */
public class CurrentNewsActivity extends BaseActivity {

    News currentNews;
    ImageView Image;
    WebView Body;
    static Handler mHandler;

    News currentNewsData;

    boolean mLoading = false;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_current_news;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_current_news);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Lifecycle of Activity start */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setToolBarThemeWhite();
        currentNews = getIntent().getParcelableExtra(NewsActivity.KEY);
        currentNews.setImg_bitmap((Bitmap)getIntent().getParcelableExtra("IMG"));

        Image = (ImageView)findViewById(R.id.cur_news_img);

        Body = (WebView)findViewById(R.id.cur_news_body);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BMWLOG", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("BMWLOG", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("BMWLOG", "onRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentNewsData = savedInstanceState.getParcelable("news");
        mLoading = savedInstanceState.getBoolean("loading");
        Log.d("BMWLOG", "onRestoreInstanceState");
    }


    @Override
    protected void onResume(){
        super.onResume();

        if(currentNewsData==null){
            if(!mLoading) {
                loadCurrentNews();
            }
        }else{
            fillAllElements(currentNewsData);
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("news", currentNewsData);
        outState.putBoolean("loading", mLoading);
        Log.d("BMWLOG", "onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BMWLOG", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BMWLOG", "onStop");
    }


    /*Lifecycle of Activity end*/


    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void loadCurrentNews(){
        mLoading = true;
        News.getCurrentNews( new GetItemHandler<News>() {
            @Override
            public void done(final News newsData) {
                mLoading = false;
                fillAllElements(newsData);
            }

            @Override
            public void error(String responseError) {
                Toast.makeText(mContext, responseError, Toast.LENGTH_SHORT).show();
                mLoading = false;
            }

        }, currentNews.getId());
    }

    private void fillAllElements(News newsData){
        switch(getScreenOrientation()) {
            case Configuration.ORIENTATION_PORTRAIT:
                /*Glide.with(mContext)
                        .load(newsData.getImg_url())
                        .into(ActualUI.getImg());*/
                Image.setImageBitmap(currentNews.getImg_bitmap());
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                break;
        }

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        Body.loadDataWithBaseURL("", newsData.getDescription(), mimeType, encoding, "");
    }

}

