package com.unicorn.coordinate.profile;

import android.content.Intent;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseFragment;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.message.MessageActivity;
import com.unicorn.coordinate.utils.ConfigUtils;

import butterknife.OnClick;


public class ProfileFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile;
    }

    @OnClick(R.id.message)
    public void messageOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.setting)
    public void settingOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            // todo 目前是登出
            ConfigUtils.logout();
        }
    }

}
