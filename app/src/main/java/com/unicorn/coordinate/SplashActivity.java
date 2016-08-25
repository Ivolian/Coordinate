package com.unicorn.coordinate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f);
//设置动画时间
        scaleAnimation.setDuration(2000);


    }

}
