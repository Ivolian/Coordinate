package com.unicorn.coordinate.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.f2prateek.dart.Dart;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dart.inject(this);
    }

    public void startActivity(Class activityClazz){
        Intent intent =new Intent(this,activityClazz);
        startActivity(intent);
    }

    public void initViews(){

    }

}
