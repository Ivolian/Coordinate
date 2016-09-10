package com.unicorn.coordinate.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.user.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @OnClick(R.id.profile)
    public void profileOnClick() {
        if (ClickHelper.isSafe()) {
            // TODO 假设尚未登录
            startActivity(LoginActivity.class);
        }
    }

    public void initViews() {
        initViewpager();
    }

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private void initViewpager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

}
