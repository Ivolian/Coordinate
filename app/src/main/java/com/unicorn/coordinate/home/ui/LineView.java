package com.unicorn.coordinate.home.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.utils.DensityUtils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class LineView extends TextView {

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //

    GradientDrawable bgSelected;
    GradientDrawable bgUnSelected;

    private void init() {
        // selected
        bgSelected = new GradientDrawable();
        bgSelected.setColor(Color.parseColor("#65C0F2"));
        bgSelected.setCornerRadius(12);
        // unselected
        bgUnSelected = new GradientDrawable();
        bgUnSelected.setColor(Color.WHITE);
        bgUnSelected.setCornerRadius(10);
        bgUnSelected.setStroke(DensityUtils.dip2px(getContext(), 1), ContextCompat.getColor(getContext(), R.color.md_grey_400));

        //
        setTextSize(COMPLEX_UNIT_DIP, 16);
        setPadding(DensityUtils.dip2px(getContext(), 16), DensityUtils.dip2px(getContext(), 8),
                DensityUtils.dip2px(getContext(), 16), DensityUtils.dip2px(getContext(), 8));

        unselect();
    }

    public void select() {
        setTextColor(Color.WHITE);
        setBackgroundDrawable(bgSelected);
        select = true;
    }

    public void unselect() {
        setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_400));
        setBackgroundDrawable(bgUnSelected);
        select = false;
    }

    boolean select = false;


    public void onClick() {
        if (select) {
            unselect();
        } else {
            select();
        }
    }
}
