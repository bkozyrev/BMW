package com.utro.bmw.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.utro.bmw.BMW;
import com.utro.bmw.R;
import com.utro.bmw.adapters.DividerItemDecoration;
import com.utro.bmw.adapters.MainMenuAdapter;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.utro.bmw.Utility.GetTintDrawable;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView list;
    private ImageView imgTop;
    CircularProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setToolBarThemeWhite();

        imgTop = (ImageView) findViewById(R.id.img_main);

        list = (RecyclerView) findViewById(R.id.main_menu_list);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());

        int Columns = 1;
        String str = BMW.getDisplayColumns(this);
        switch(str){
            case "large":
                Columns = 3;
                break;
            case "normal":
                Columns = 1;
                list.setBackground(GetTintDrawable(mContext, imgTop));
                break;
        }

        switch(getScreenOrientation()){
            case Configuration.ORIENTATION_PORTRAIT:

                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                break;
        }
        list.setLayoutManager(new StaggeredGridLayoutManager(Columns, StaggeredGridLayoutManager.VERTICAL));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        list.setAdapter(new MainMenuAdapter(mContext, getResources().getStringArray(R.array.main_menu_items), MainActivity.this));
        list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.main_rl:
                int position = list.getChildLayoutPosition(view);
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, NewsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, TestDriveActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, SeriesActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, AccessoriesCategoriesActivity.class);
                        break;
                    case 4:
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, ServicesListActivity.class);
                        break;
                }
                if(intent != null) {
                    startActivity(intent);
                }
        }
    }


    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_main;
    }

    @Override
    protected String getTitleToolBar() {
        return null;
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return false;
    }

    @Override
    protected boolean getDisplayShowTitleEnabled() {
        return false;
    }

}
