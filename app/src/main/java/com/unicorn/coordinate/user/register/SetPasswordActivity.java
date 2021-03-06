package com.unicorn.coordinate.user.register;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
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


public class SetPasswordActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
    }


    // ======================== InjectExtra =========================

    @InjectExtra(Constant.K_USER_ID)
    String userId;


    // ======================== views =========================

    @BindView(R.id.pwd)
    AppCompatEditText pwd;

    @BindView(R.id.confirmPwd)
    AppCompatEditText confirmPwd;


    // ======================== views =========================

    @OnClick(R.id.register)
    public void registerOnClick() {
        if (ClickHelper.isSafe() && isInputValid()) {
            register();
        }
    }

    private void register() {
        String url = getSetPasswordUrl(userId, getPwd());
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            if (ResponseHelper.isRight(responseString)) {
                                ToastUtils.show("用户注册成功");
                                finishWithResult();
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

    private void finishWithResult(){
        setResult(Constant.RC_REGISTER_SUCCESS);
        finish();
    }


    // ======================== 底层方法 =========================

    private boolean isInputValid() {
        final int pwdMinLength = 6;
        String pwd = getPwd();
        if (pwd.equals("")) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        if (pwd.length() < pwdMinLength) {
            ToastUtils.show("密码至少6位");
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
        if (!pwd.equals(confirmPwd)) {
            ToastUtils.show("两次密码不一致");
            return false;
        }
        return true;
    }

    private String getSetPasswordUrl(String userId, String pwd) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/set_pwd?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, userId);
        builder.appendQueryParameter("pwd", pwd);
        return builder.toString();
    }

    private String getPwd() {
        return pwd.getText().toString().trim();
    }


    private String getConfirmPwd() {
        return confirmPwd.getText().toString().trim();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
