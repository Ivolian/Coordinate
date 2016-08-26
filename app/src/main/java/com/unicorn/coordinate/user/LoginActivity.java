package com.unicorn.coordinate.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        addUnderlines();
    }


    // ======================== 注册 & 忘记密码 =========================

    @BindView(R.id.register)
    AppCompatTextView register;

    @BindView(R.id.forgetPwd)
    AppCompatTextView forgetPwd;

    private void addUnderlines() {
        drawUnderline(register);
        drawUnderline(forgetPwd);
    }

    private void drawUnderline(AppCompatTextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView.getPaint().setAntiAlias(true);
    }

    @OnClick(R.id.register)
    public void registerOnClick() {
        if (ClickHelper.isSafe()) {
            startActivity(RegisterActivity.class);
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
