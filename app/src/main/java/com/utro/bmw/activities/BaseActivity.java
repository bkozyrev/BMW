package com.utro.bmw.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.utro.bmw.R;

/**
 * Created by Egor on 15.04.2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolBar;
    protected View mShadowView;
    protected ProgressBar mBar;
    protected Context mContext;
    protected TextView mToolBarTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceIdentifier());
        loadComponents();
        loadInfoView();
        initializeToolBar();
    }

    protected void onResume(){
        super.onResume();
        if(mShadowView!=null) mShadowView.setTranslationY(0);
        mToolBar.setTranslationY(0);
    }

    private void loadComponents() {
        mContext = getApplicationContext();
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mShadowView = (View) findViewById(R.id.shadow);
        mBar = (ProgressBar) mToolBar.findViewById(R.id.bar);
        mToolBarTitle = (TextView) findViewById(R.id.toolbar_title);
    }

    private void loadInfoView() {
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    protected void initializeToolBar() {
        if (mToolBar != null) {
            mToolBar.setTitle(getTitleToolBar());
            getSupportActionBar().setDisplayHomeAsUpEnabled(getDisplayHomeAsUp());
            getSupportActionBar().setHomeButtonEnabled(getHomeButtonEnabled());
            getSupportActionBar().setDisplayShowTitleEnabled(getDisplayShowTitleEnabled());
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_grey600_24dp);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            if(getTitleToolBar()!=null) setTitleToolBar(getTitleToolBar());
            //TODO everywhere change method getTitleToolBar;
        }
    }

    protected abstract int getLayoutResourceIdentifier();

    protected abstract String getTitleToolBar();

    protected void setToolBarColor(int Color){
        mToolBar.setBackgroundColor(Color);
    }

    protected void setTitleToolBar(String text){
        mToolBarTitle.setText(text);
    }

    protected void setTitleToolBarColor(int Color){
        mToolBarTitle.setTextColor(Color);
    }

    protected void setToolBarThemeBlue(){
        mToolBar.setBackgroundColor(getResources().getColor(R.color.blue));
        mToolBarTitle.setTextColor(getResources().getColor(R.color.toolbar_text_white));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
    }

    protected void setToolBarThemeWhite(){
        mToolBar.setBackgroundColor(getResources().getColor(R.color.white));
        mToolBarTitle.setTextColor(getResources().getColor(R.color.toolbar_text_gray));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_grey600_24dp));
    }

    protected abstract boolean getDisplayHomeAsUp();

    protected abstract boolean getHomeButtonEnabled();

    protected abstract boolean getDisplayShowTitleEnabled();

}