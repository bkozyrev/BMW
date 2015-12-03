package com.utro.bmw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.ServicesListAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.Service;

import java.util.ArrayList;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;

/**
 * Created by 123 on 26.06.2015.
 */
public class ServicesListActivity extends BaseActivity implements View.OnClickListener{

    RecyclerView list;
    ArrayList<Service> services;
    boolean isFirstTime = false;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_list_services;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_services_list);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = (RecyclerView) findViewById(R.id.services_list);

        isFirstTime = true;

        loadServices();
        setAnimation(list, mToolBar, mShadowView);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("ServicesListActivity", "onRestoreInstanceState");
        firstTime = savedInstanceState.getBoolean("firstTime");
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_btn_add:
                Intent intent = new Intent(ServicesListActivity.this, ServicesOrderActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_services_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View view){

    }

    public void loadServices(){
            mBar.setVisibility(View.VISIBLE);
        Service.getAllServices(new GetListHandler<Service>() {
            @Override
            public void done(ArrayList<Service> data) {
                mBar.setVisibility(View.INVISIBLE);
                services = data;

                list.setAdapter(new ServicesListAdapter(mContext, services, ServicesListActivity.this));
            }

            @Override
            public void error(String responseError) {
                Log.d("ServicesListActivity", responseError);
                mBar.setVisibility(View.INVISIBLE);
                if(responseError.equals("No value for items&200") && isFirstTime){
                    isFirstTime = false;
                    Intent intent = new Intent(ServicesListActivity.this, ServicesOrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
