package com.utro.bmw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.utro.bmw.utils.BlurBuilder;

public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            totalHeight += listItem.getPaddingTop();
            totalHeight += listItem.getPaddingBottom();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static Drawable GetTintDrawable(Context mContext, ImageView imgWithBitmap){

        Bitmap blurredBitmap = BlurBuilder.blur(mContext, ((BitmapDrawable) imgWithBitmap.getDrawable()).getBitmap());

        Bitmap newBitmap = Bitmap.createBitmap(blurredBitmap.getWidth(), blurredBitmap.getHeight(), blurredBitmap.getConfig());
        Canvas newCanvas = new Canvas(newBitmap);

        Paint p = new Paint(Color.WHITE);
        p.setColorFilter(new PorterDuffColorFilter(mContext.getResources().getColor(R.color.main_menu_bck_color), PorterDuff.Mode.SRC_OVER));
        newCanvas.drawBitmap(blurredBitmap, 0, 0, p);

        BitmapDrawable bmp = new BitmapDrawable(newBitmap);
        return bmp;
    }
}
