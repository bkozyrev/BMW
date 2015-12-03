package com.utro.bmw;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.utro.bmw.utils.HttpsImageLoader;

/**
 * Created by Egor on 15.04.2015.
 */
public class BMW extends Application {

    public String MIX_PANEL_TOKEN = "e342bf3e04130314e19b3059281b6c97";
    public static MixpanelAPI mixpanel;

    @Override
    public void onCreate() {


        HttpsImageLoader.trustAllHosts();
        mixpanel = MixpanelAPI.getInstance(this, MIX_PANEL_TOKEN);
        mixpanel.track("BMW App - onCreate called", null);
    }

    public static boolean isTablet(Context context) {
//        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        //return (xlarge || large);
        return large;
    }

    public static String getDisplayColumns(Activity activity) {//TODO mContext
        String str = "";
        if (isTablet(activity)) {
            str = "large";
        }else{
            str = "normal";
        }
        return str;
    }

}