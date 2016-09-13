package com.unicorn.coordinate.profile;

import android.os.Bundle;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;

import butterknife.OnClick;

public class UpdateProfileActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
    }

    @Override
    public void initViews() {
//        UserInfo userInfo = ConfigUtils.getUserInfo();
//        tvName.setText(userInfo.getName());
//        tvSexy.setText(userInfo.getSexy().equals("1") ? "男" : "女");
//        tvBirthday.setText(userInfo.getBirthday().substring(0, 10));
//        tvCardType.setText(userInfo.getCardtype().equals("1") ? "身份证" : "护照");
//        tvCardNo.setText(userInfo.getCardno());
    }


    // ======================== views =========================

//    @BindView(R.id.tvName)
//    TextView tvName;
//
//    @BindView(R.id.tvSexy)
//    TextView tvSexy;
//
//    @BindView(R.id.tvBirthday)
//    TextView tvBirthday;
//
//    @BindView(R.id.tvCardType)
//    TextView tvCardType;
//
//    @BindView(R.id.tvCardNo)
//    TextView tvCardNo;


    // ======================== onClick =========================

//    @OnClick(R.id.name)
//    public void nameOnClick(){
//
//    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
