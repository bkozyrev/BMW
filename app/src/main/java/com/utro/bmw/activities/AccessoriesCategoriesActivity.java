package com.utro.bmw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.utro.bmw.BMW;
import com.utro.bmw.R;
import com.utro.bmw.adapters.AccessoriesCategoriesAdapter;
import com.utro.bmw.model.Category;
import com.utro.bmw.model.GetListHandler;

import java.util.ArrayList;

import static com.utro.bmw.Utility.GetTintDrawable;

public class AccessoriesCategoriesActivity extends BaseActivity implements View.OnClickListener {

    static String CATEGORY_KEY = "category_key";

    private RecyclerView list;
    private ImageView imgTop;
    ArrayList<Category> Array;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_accessories_categories;
    }

    @Override
    protected String getTitleToolBar() {
        return null;//getString(R.string.main_screen);
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
        Log.d("BMWLOG", "onCreate");
        mContext = getBaseContext();

        imgTop = (ImageView) findViewById(R.id.img_main_categories);

        list = (RecyclerView) findViewById(R.id.accessories_categories_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(mContext));
        //list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));//TODO Clear

        String str = BMW.getDisplayColumns(this);
        switch(str){
            case "normal":
                list.setBackground(GetTintDrawable(mContext, imgTop));
                break;
        }

        list.setOnClickListener(this);
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
        Array = savedInstanceState.getParcelableArrayList("array");
        Log.d("BMWLOG", "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BMWLOG", "onResume ");

        if(Array == null) {
            Array = new ArrayList<>();
            loadCategories();
        }else{
            list.setAdapter(new AccessoriesCategoriesAdapter(mContext, Array, AccessoriesCategoriesActivity.this));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("array", Array);
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

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.main_rl:
                int position = list.getChildLayoutPosition(view);
                Intent intent = new Intent(AccessoriesCategoriesActivity.this, MerchandiseActivity.class);
                intent.putExtra(CATEGORY_KEY, position);
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

    private void loadCategories()
    {

        Category.getAllCategories(new GetListHandler<Category>() {
            @Override
            public void done(ArrayList<Category> data) {
                Array = data;
                list.setAdapter(new AccessoriesCategoriesAdapter(mContext, Array, AccessoriesCategoriesActivity.this));
            }

            @Override
            public void error(String responseError) {
                Toast
                        .makeText(mContext,responseError, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

}
