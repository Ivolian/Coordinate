package com.unicorn.coordinate.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.utils.DensityUtils;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space = DensityUtils.dip2px(SimpleApplication.getInstance(),12);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) % 2 != 0) {
            outRect.right = space;
        }

        if (parent.getChildLayoutPosition(view) <= 1) {
            outRect.top = space;
        }
    }
}
