package com.utro.bmw.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.utro.bmw.BMW;
import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.VehiclesAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.Series;
import com.utro.bmw.model.Vehicle;

import java.util.ArrayList;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;

/**
 * Created by 123 on 17.04.2015.
 */
public class VehiclesActivity extends BaseActivity implements View.OnClickListener {

    static String VEHICLE_KEY = "vehicle_key";

    private RecyclerView list;
    ArrayList<Vehicle> Array;
    Series currentSeries;
    int lastPositionOfRecyclerView = 0;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_vehicles;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_vehicles);
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

    /*Lifecycle of Activity start*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setToolBarThemeWhite();

        currentSeries = getIntent().getParcelableExtra(SeriesActivity.SERIES_KEY);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Array = savedInstanceState.getParcelableArrayList("array");
        lastPositionOfRecyclerView = savedInstanceState.getInt("lastPosition");
    }

    protected void onResume() {
        super.onResume();

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

        list = (RecyclerView) findViewById(R.id.vehicles_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new StaggeredGridLayoutManager(Columns, StaggeredGridLayoutManager.VERTICAL));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        list.setOnClickListener(this);

        setAnimation(list, mToolBar, mShadowView);

        if(Array == null) {
            //Array = new ArrayList<>();
            loadVehicles();
        }else{
            list.setAdapter(new VehiclesAdapter(mContext, Array, VehiclesActivity.this, false));
            list.scrollToPosition(lastPositionOfRecyclerView);
            mBar.setVisibility(View.INVISIBLE);
        }



    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("array", Array);
        StaggeredGridLayoutManager llm = (StaggeredGridLayoutManager)list.getLayoutManager();
        int array[] = new int[3];
        array = llm.findFirstCompletelyVisibleItemPositions(array);
        lastPositionOfRecyclerView = array[0];
        outState.putInt("lastPosition", lastPositionOfRecyclerView);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.item_vehicles:
                int position = list.getChildLayoutPosition(view);
                Intent intent = new Intent(VehiclesActivity.this, SingleVehicleActivity.class);
                intent.putExtra(VEHICLE_KEY, Array.get(position));
                startActivity(intent);
                break;
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

    private void loadVehicles()
    {
        mBar.setVisibility(View.VISIBLE);

        String series = "";
        if(currentSeries != null) {
            series = currentSeries.getTitle();
        }

        Vehicle.getAllVehiclesOrders(new GetListHandler<Vehicle>() {
            @Override
            public void done(ArrayList<Vehicle> data) {
                Array = data;
                list.setAdapter(new VehiclesAdapter(mContext, Array, VehiclesActivity.this, false));
                mBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void error(String responseError) {
                mBar.setVisibility(View.INVISIBLE);
            }
        }, series);
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }
}
