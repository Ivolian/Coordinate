package com.unicorn.coordinate.profile;

import android.net.Uri;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.base.WebViewActivity;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.utils.ConfigUtils;


public class UserMatchDetailActivity extends WebViewActivity {


    @InjectExtra(Constant.K_MATCH_ID)
    String matchId;


    @Override
    protected String getUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/matchdetail?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchId);
        return builder.toString();
    }


}
