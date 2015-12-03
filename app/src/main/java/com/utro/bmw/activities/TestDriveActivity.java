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
import com.utro.bmw.model.Vehicle;

import java.util.ArrayList;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;

/**
 * Created by 123 on 27.06.2015.
 */
public class TestDriveActivity extends BaseActivity implements View.OnClickListener{

    static String VEHICLE_KEY = "vehicle_key";

    private RecyclerView list;
    ArrayList<Vehicle> Array;
    int lastPositionOfRecyclerView = 0;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_vehicles;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_test_drive);
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

        if(Array == null) {
            Array = new ArrayList<>();
            loadVehicles();
        }else{
            list.setAdapter(new VehiclesAdapter(mContext, Array, TestDriveActivity.this, true));
            list.scrollToPosition(lastPositionOfRecyclerView);
        }

        setAnimation(list, mToolBar, mShadowView);

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
    /*Lifecycle of Activity end*/

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.item_test_drive:
                int position = list.getChildLayoutPosition(view);
                Intent intent = new Intent(TestDriveActivity.this, SingleVehicleActivityFromTestDrive.class);
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
        Vehicle.getAllVehiclesDemos(new GetListHandler<Vehicle>() {
            @Override
            public void done(ArrayList<Vehicle> data) {
                mBar.setVisibility(View.INVISIBLE);
                Array = data;
                list.setAdapter(new VehiclesAdapter(mContext, Array, TestDriveActivity.this, true));
            }

            @Override
            public void error(String responseError) {
                mBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }
}
