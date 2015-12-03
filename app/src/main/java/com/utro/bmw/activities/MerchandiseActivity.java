package com.utro.bmw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.MerchandiseAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.Merchandise;

import java.util.ArrayList;

import static com.utro.bmw.utils.ToolBarWithRecyclerViewAnimation.setAnimation;

/**
 * Created by 123 on 25.04.2015.
 */
public class MerchandiseActivity extends BaseActivity implements View.OnClickListener{

    static String MERCH_KEY = "merch_key";

    private RecyclerView list;
    ArrayList<Merchandise> Array;
    int categoryNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setToolBarThemeWhite();

        categoryNumber = getIntent().getIntExtra(AccessoriesCategoriesActivity.CATEGORY_KEY, -1);

        Array = new ArrayList<>();

        list = (RecyclerView) findViewById(R.id.merch_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        list.setOnClickListener(this);

        setAnimation(list, mToolBar, mShadowView);

        loadMerch();


    }

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_merchandise;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_accessories);
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
    public void onClick(View view){
        switch(view.getId()){
            case R.id.merch_ll:
                int position = list.getChildLayoutPosition(view);
                Intent intent = new Intent(MerchandiseActivity.this, CurrentMerchandiseActivity.class);
                intent.putExtra(MERCH_KEY, Array.get(position));
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

    private void loadMerch()
    {
        Merchandise.getAllMerch(new GetListHandler<Merchandise>() {
            @Override
            public void done(ArrayList<Merchandise> data) {
                Array = data;
                list.setAdapter(new MerchandiseAdapter(mContext, Array, MerchandiseActivity.this));
            }

            @Override
            public void error(String responseError) {

            }
        }, categoryNumber);
    }
}
