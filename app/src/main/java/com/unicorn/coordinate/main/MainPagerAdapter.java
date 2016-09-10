package com.unicorn.coordinate.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.coordinate.home.HomeFragment;


public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public String[] TITLES = {
            "首页", "消息", "图集", "我的"
    };

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return new HomeFragment();
            case 1:
//                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
     return 1;
//        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[0];
//        return TITLES[position];
    }

}
