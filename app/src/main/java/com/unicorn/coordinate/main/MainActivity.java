package com.unicorn.coordinate.main;

import android.os.Bundle;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.user.LoginActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.profile)
    public void profileOnClick(){
        if (ClickHelper.isSafe()){
            // TODO 假设尚未登录
            startActivity(LoginActivity.class);
        }
    }



}
