package com.unicorn.coordinate.task;

import android.net.Uri;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.base.WebViewActivity;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.utils.ConfigUtils;

public class LinkNoActivity extends WebViewActivity {

    @InjectExtra(Constant.K_MATCH_USER_ID)
    String matchUserId;

    @InjectExtra(Constant.K_LINK_NO)
    String linkNo;

    @Override
    protected String getUrl() {
        Uri.Builder builder = Uri.parse("http://www.chengshidingxiang.com" + "/api/question?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_USER_ID, matchUserId);
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_LINK_NO, linkNo);
        return builder.toString();
    }

}