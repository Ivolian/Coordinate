package com.unicorn.coordinate.profile;

import android.content.Intent;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseFragment;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.message.MessageActivity;

import butterknife.OnClick;


public class ProfileFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile;
    }

    @OnClick(R.id.message)
    public void messageOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            startActivity(intent);
        }
    }

}
