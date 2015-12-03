package com.utro.bmw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.SeriesAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.Series;

import java.util.ArrayList;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;

public class SeriesActivity extends BaseActivity implements View.OnClickListener{

    static String SERIES_KEY = "series_key";
    RecyclerView list;
    ArrayList<Series> series;
    int lastPositionOfRecyclerView = 0;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_series;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_series);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarThemeWhite();

        list = (RecyclerView)findViewById(R.id.series_list);
        setAnimation(list, mToolBar, mShadowView);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        series = savedInstanceState.getParcelableArrayList("series");
        lastPositionOfRecyclerView = savedInstanceState.getInt("lastPosition");
        list = (RecyclerView)findViewById(R.id.series_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        list = (RecyclerView)findViewById(R.id.series_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        list.setOnClickListener(this);

        if(series == null){
            loadSeries();
        }
        else{
            list.setAdapter(new SeriesAdapter(mContext, series, this));
            list.scrollToPosition(lastPositionOfRecyclerView);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("series", series);
        LinearLayoutManager llm = (LinearLayoutManager)list.getLayoutManager();
        lastPositionOfRecyclerView = llm.findFirstCompletelyVisibleItemPosition();
        outState.putInt("lastPosition", lastPositionOfRecyclerView);
    }

    @Override
    public void onClick(View view){
        int position = list.getChildLayoutPosition(view);
        Intent intent = new Intent(SeriesActivity.this, VehiclesActivity.class);
        intent.putExtra(SERIES_KEY, series.get(position));
        startActivity(intent);
    }

    public void loadSeries(){
        mBar.setVisibility(View.VISIBLE);
        Series.getAllSeries(new GetListHandler<Series>() {
            @Override
            public void done(ArrayList<Series> data) {
                series = data;
                mBar.setVisibility(View.INVISIBLE);
                list.setAdapter(new SeriesAdapter(mContext, series, SeriesActivity.this));
            }
            @Override
            public void error(String responseError) {
                mBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
