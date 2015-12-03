package com.utro.bmw.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Egor on 26.06.2015.
 */
public class ToolBarWithRecyclerViewAnimation {

    public static void setAnimation(RecyclerView recyclerView, final Toolbar toolBarView, final View shadow)
    {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    hideToolbarBy(dy);
                } else {
                    showToolbarBy(dy);
                }
            }

            private void hideToolbarBy(int dy) {
                if (cannotHideMore(dy)) {
                    toolBarView.setTranslationY(-toolBarView.getBottom());
                    shadow.setTranslationY(-toolBarView.getBottom());
                } else {
                    toolBarView.setTranslationY(toolBarView.getTranslationY() - dy);
                    shadow.setTranslationY(shadow.getTranslationY() - dy);
                    //shadow.setTranslationY(shadow.getTranslationY() - dy);
                }

/*
                if(cannotHideMoreSHadow(dy)){
                    shadow.setTranslationY(-shadow.getBottom());
                } else {

                }*/
            }
            private boolean cannotHideMore(int dy) {
                return Math.abs(toolBarView.getTranslationY() - dy)>toolBarView.getBottom();
            }

            private void showToolbarBy(int dy) {
                if (cannotShowMore(dy)) {
                    toolBarView.setTranslationY(0);
                } else {
                    toolBarView.setTranslationY(toolBarView.getTranslationY() - dy);

                }

                if (cannotShowMoreShadow(dy)) {
                    shadow.setTranslationY(0);
                }else
                    shadow.setTranslationY(shadow.getTranslationY() - dy);
            }
            private boolean cannotShowMore(int dy) {
                return toolBarView.getTranslationY() - dy > 0;
            }

            private boolean cannotShowMoreShadow(int dy){
                return shadow.getTranslationY() - dy > 0;
            }
        });
    }

}
