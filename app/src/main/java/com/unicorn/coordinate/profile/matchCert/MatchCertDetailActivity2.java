package com.unicorn.coordinate.profile.matchCert;

import android.net.Uri;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.base.WebViewActivity;
import com.unicorn.coordinate.helper.Constant;


public class MatchCertDetailActivity2 extends WebViewActivity {

    @InjectExtra(Constant.K_MATCH_USER_ID)
    String matchUserId;


    @Override
    protected String getUrl() {
        Uri.Builder builder = Uri.parse("http://www.chengshidingxiang.com" + "/api/matchcert?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_USER_ID, matchUserId);
        return builder.toString();
    }

}
