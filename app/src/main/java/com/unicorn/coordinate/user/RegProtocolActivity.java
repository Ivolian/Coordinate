package com.unicorn.coordinate.user;

import com.unicorn.coordinate.base.WebViewActivity;
import com.unicorn.coordinate.utils.ConfigUtils;


public class RegProtocolActivity extends WebViewActivity {

    @Override
    protected String getUrl() {
        return ConfigUtils.getBaseUrl() + "/api/regprotocol";
    }

}
