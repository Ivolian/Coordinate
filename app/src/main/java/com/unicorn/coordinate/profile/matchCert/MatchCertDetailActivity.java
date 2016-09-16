package com.unicorn.coordinate.profile.matchCert;

import android.net.Uri;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.base.WebViewActivity;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.utils.ConfigUtils;


public class MatchCertDetailActivity extends WebViewActivity {


    @InjectExtra(Constant.K_MATCH_ID)
    String matchId;


    @Override
    protected String getUrl() {
        Uri.Builder builder = Uri.parse("http://www.chengshidingxiang.com" + "/api/matchcert?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchId);
        return builder.toString();
    }


}
