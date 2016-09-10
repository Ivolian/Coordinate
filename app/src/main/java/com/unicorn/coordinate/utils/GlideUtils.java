package com.unicorn.coordinate.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;


public class GlideUtils {

    public static void loadPicture(String imgUrl, ImageView imageView) {
        Glide.with(SimpleApplication.getInstance())
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .crossFade()
                .into(imageView);
    }



}
