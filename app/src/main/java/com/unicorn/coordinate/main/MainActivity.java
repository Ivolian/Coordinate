package com.unicorn.coordinate.main;

import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.EventBusActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.ReadMessageEvent;
import com.unicorn.coordinate.message.event.RefreshMessageEvent;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.UpdateUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends EventBusActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        UpdateUtils.checkUpdate(this);
    }

    public void initViews() {
        initViewpager();
        initMessageCount();
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


    // ======================== readMessageEvent =========================

    @Subscribe
    public void onEvent(ReadMessageEvent readMessageEvent) {
        viewPager.setCurrentItem(1, true);
    }


    // ======================== getMessageCount =========================

    @BindView(R.id.messageCount)
    TextView messageCount;

    private void initMessageCount() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1000);
        gradientDrawable.setColor(ContextCompat.getColor(this, R.color.md_red_400));
        messageCount.setBackgroundDrawable(gradientDrawable);
        getMessageCount();
    }

    private void getMessageCount() {
        String url = messageCountUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        int cnt = data.getInt("Cnt");
//        if (cnt > 0) {
            messageCount.setVisibility(View.VISIBLE);
//        }
    }

    private String messageCountUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getnewmsgcount?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    @Subscribe
    public void refreshMessage(RefreshMessageEvent refreshMessageEvent) {
        getMessageCount();
    }

}
