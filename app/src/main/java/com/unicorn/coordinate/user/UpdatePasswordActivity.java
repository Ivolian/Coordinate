package com.unicorn.coordinate.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdatePasswordActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
    }


    // ======================== views =========================

    @BindView(R.id.oldPwd)
    AppCompatEditText oldPwd;

    @BindView(R.id.newPwd)
    AppCompatEditText newPwd;

    @BindView(R.id.confirmPwd)
    AppCompatEditText confirmPwd;


    // ======================== views =========================

    @OnClick(R.id.updatePwd)
    public void registerOnClick() {
        if (ClickHelper.isSafe() && isInputValid()) {
            updatePassword();
        }
    }

    private void updatePassword() {
        String url = getUrl(ConfigUtils.getUserId(), getOldPwd(), getNewPwd());
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            if (ResponseHelper.isRight(responseString)) {
                                ToastUtils.show("修改密码成功");
                                finish();
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }


    // ======================== 底层方法 =========================

    private boolean isInputValid() {
        final int pwdMinLength = 6;
        String oldPwd = getOldPwd();
        if (oldPwd.equals("")) {
            ToastUtils.show("原密码不能为空");
            return false;
        }
        if (oldPwd.length() < pwdMinLength) {
            ToastUtils.show("原密码至少6位");
            return false;
        }
        String newPwd = getNewPwd();
        if (newPwd.equals("")) {
            ToastUtils.show("新密码不能为空");
            return false;
        }
        if (newPwd.length() < pwdMinLength) {
            ToastUtils.show("新密码至少6位");
            return false;
        }
        String confirmPwd = getConfirmPwd();
        if (confirmPwd.equals("")) {
            ToastUtils.show("确认密码不能为空");
            return false;
        }
        if (confirmPwd.length() < pwdMinLength) {
            ToastUtils.show("确认密码至少6位");
            return false;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.show("两次密码不一致");
            return false;
        }
        return true;
    }

    private String getUrl(String userId, String oldPwd, String newPwd) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/update_pwd?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, userId);
        builder.appendQueryParameter("oldpwd", oldPwd);
        builder.appendQueryParameter("newpwd", newPwd);
        return builder.toString();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


    // ======================== 基本无视 =========================

    private String getOldPwd() {
        return oldPwd.getText().toString().trim();
    }

    private String getNewPwd() {
        return newPwd.getText().toString().trim();
    }

    private String getConfirmPwd() {
        return confirmPwd.getText().toString().trim();
    }


}
