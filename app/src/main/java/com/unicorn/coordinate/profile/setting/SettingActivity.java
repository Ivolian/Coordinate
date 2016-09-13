package com.unicorn.coordinate.profile.setting;

import android.os.Bundle;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.user.UpdatePasswordActivity;
import com.unicorn.coordinate.utils.ConfigUtils;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    // ======================== onClick =========================

    @OnClick(R.id.updatePwd)
    public void updatePwd() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdatePasswordActivity.class);
        }
    }

    @OnClick(R.id.logout)
    public void logout() {
        if (ClickHelper.isSafe()) {
            ConfigUtils.logout();
            finish();
        }
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
