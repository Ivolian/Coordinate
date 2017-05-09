package com.unicorn.coordinate.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;

import java.io.File;


public class GlideUtils {

    public static void loadPicture(String imgUrl, ImageView imageView) {
        Glide.with(SimpleApplication.getInstance())
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .crossFade()
                .into(imageView);
    }

    public static void loadPicture(File img, ImageView imageView) {
        Glide.with(SimpleApplication.getInstance())
                .load(img)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .crossFade()
                .into(imageView);
    }



}
