package com.unicorn.coordinate.profile;

import android.os.Bundle;
import android.widget.TextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.user.model.UserInfo;
import com.unicorn.coordinate.utils.ConfigUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void initViews() {
        UserInfo userInfo = ConfigUtils.getUserInfo();
        tvName.setText(userInfo.getName());
        tvSexy.setText(userInfo.getSexy().equals("1") ? "男" : "女");
        tvBirthday.setText(userInfo.getBirthday().substring(0, 10));
        tvCardType.setText(userInfo.getCardtype().equals("1") ? "身份证" : "护照");
        tvCardNo.setText(userInfo.getCardno());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }


    // ======================== views =========================

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvSexy)
    TextView tvSexy;

    @BindView(R.id.tvBirthday)
    TextView tvBirthday;

    @BindView(R.id.tvCardType)
    TextView tvCardType;

    @BindView(R.id.tvCardNo)
    TextView tvCardNo;


    // ======================== onClick =========================

    @OnClick(R.id.name)
    public void nameOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdateProfileActivity.class);
        }
    }

    @OnClick(R.id.sexy)
    public void sexyOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdateProfileActivity.class);
        }
    }

    @OnClick(R.id.birthday)
    public void birthdayOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdateProfileActivity.class);
        }
    }

    @OnClick(R.id.cardType)
    public void cardTypeOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdateProfileActivity.class);
        }
    }

    @OnClick(R.id.cardNo)
    public void cardNoOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdateProfileActivity.class);
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
