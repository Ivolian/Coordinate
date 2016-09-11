package com.unicorn.coordinate.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.utils.ConfigUtils;


public class ImageHelper {

    public static String getImgUrl(String partUrl) {
        return ConfigUtils.getImageBaseUrl() + partUrl;
    }

    public static void loadPicture(String imgUrl, ImageView imageView) {
        Context context = SimpleApplication.getInstance();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

}
