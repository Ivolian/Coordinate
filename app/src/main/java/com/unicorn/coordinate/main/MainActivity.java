package com.unicorn.coordinate.main;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews() {
        initViewpager();
    }


    // ======================== initViewpager =========================

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private void initViewpager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectBottomTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        selectBottomTab(0);
    }


    // ======================== 底部栏 =========================

    private int[] drawableResSelected = {R.drawable.home, R.drawable.message, R.drawable.atlas, R.drawable.profile};

    private int[] drawableResUnselected = {R.drawable.home_u, R.drawable.message_u, R.drawable.atlas_u, R.drawable.profile_u};

    @BindViews({R.id.home, R.id.message, R.id.picture, R.id.profile})
    List<PercentLinearLayout> bottomTabList;

    @OnClick({R.id.home, R.id.message, R.id.picture, R.id.profile})
    public void bottomTabOnClick(PercentLinearLayout bottomTab) {
        if (!ClickHelper.isSafe()) {
            return;
        }

        int bottomTabId = bottomTab.getId();
        if (bottomTabId == R.id.message) {
            if (!ConfigUtils.checkLogin(this)) {
                return;
            }
        }

        int index = bottomTabList.indexOf(bottomTab);
        selectBottomTab(index);
    }

    private void selectBottomTab(int position) {
        for (PercentLinearLayout bottomTab : bottomTabList) {
            ImageView imageView = (ImageView) bottomTab.getChildAt(0);
            TextView textView = (TextView) bottomTab.getChildAt(1);
            int index = bottomTabList.indexOf(bottomTab);
            boolean selected = (index == position);
            imageView.setImageResource(selected ? drawableResSelected[index] : drawableResUnselected[index]);
            textView.setTextColor(ContextCompat.getColor(this, selected ? R.color.colorPrimary : R.color.md_grey_400));
        }
        viewPager.setCurrentItem(position, false);
    }


}
