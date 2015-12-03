package com.utro.bmw.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.R;
import com.utro.bmw.model.Merchandise;

/**
 * Created by 123 on 26.04.2015.
 */
public class CurrentMerchandiseActivity extends BaseActivity {

    ImageView MerchImage;
    TextView Title, Description;
    Merchandise CurrentMerch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarThemeWhite();

        Title = (TextView)findViewById(R.id.cur_merch_title);
        Description = (TextView)findViewById(R.id.cur_merch_descr);
        MerchImage = (ImageView)findViewById(R.id.cur_merch_img);

        CurrentMerch = getIntent().getParcelableExtra(MerchandiseActivity.MERCH_KEY);

        Title.setText(CurrentMerch.getTitle());
        Description.setText(CurrentMerch.getDescription());

        /*ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageLoader.displayImage(currentMerch.getUrl(), MerchImage);*/

        Glide.with(mContext)
                .load(CurrentMerch.getUrl())
                .into(MerchImage);
    }

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_current_merch;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
