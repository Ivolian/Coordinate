package com.unicorn.coordinate.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.coordinate.home.HomeFragment;
import com.unicorn.coordinate.pictrue.AtlasFragment;


public class MainPagerAdapter extends FragmentStatePagerAdapter {


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
            case 2:
                return new AtlasFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


}
