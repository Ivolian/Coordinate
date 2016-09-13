package com.unicorn.coordinate.user.forgetPwd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
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

public class ForgetPwdActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
    }


    // ======================== timing =========================

    private final Handler handler = new Handler();

    private int time = 0;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            requestPwd.setText(time == 0 ? "获取动态密码" : time + "秒后可重新获得");
            if (time == 0) {
                return;
            }
            time--;
            handler.postDelayed(this, 1000);
        }
    };


    // ======================== views =========================

    @BindView(R.id.tel)
    AppCompatEditText tel;

    @BindView(R.id.pwd)
    AppCompatEditText pwd;

    @BindView(R.id.requestPwd)
    AppCompatButton requestPwd;


    // ======================== 获取验证码 =========================

    @OnClick(R.id.requestPwd)
    public void requestOnClick() {
        if (ClickHelper.isSafe() && !isTiming() && isTelValid()) {
            fetchPwd(getTel());
        }
    }

    private void fetchPwd(String tel) {
        String url = getRequestPwdUrl(tel);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (ResponseHelper.isRight(response)) {
                                ToastUtils.show("验证码已发送");
                                startTiming();
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

    private void startTiming() {
        time = 60;
        handler.post(runnable);
    }

    private boolean isTiming() {
        return time != 0;
    }


    // ======================== 检查验证码 =========================

    @OnClick(R.id.nextStep)
    public void nextStepOnClick() {
        if (ClickHelper.isSafe() && isVercodeValid()) {
            checkVercode();
        }
    }

    private void checkVercode() {
        String url = getCheckVercodeUrl(getTel(), gerVercode());
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);

    }


    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
//        JSONObject response = new JSONObject(responseString);
        startResetPasswordActivity();
    }

    private void startResetPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(Constant.K_TEL, getTel());
        startActivityForResult(intent, 233);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RC_RESET_SUCCESS) {
            finish();
        }
    }


    // ======================== 底层方法 =========================

    private String getRequestPwdUrl(String tel) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/forget_pwd?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, tel);
        return builder.toString();
    }

    private String getCheckVercodeUrl(String tel, String vercode) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/forget_check?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, tel);
        builder.appendQueryParameter("vercode", vercode);
        return builder.toString();
    }

    private boolean isTelValid() {
        String tel = getTel();
        if (tel.equals("")) {
            ToastUtils.show("手机号不能为空");
            return false;
        }
        if (tel.length() != 11) {
            ToastUtils.show("手机号位数不正确");
            return false;
        }
        return true;
    }

    private boolean isVercodeValid() {
        String vercode = gerVercode();
        if (vercode.equals("")) {
            ToastUtils.show("验证码不能为空");
            return false;
        }
        return true;
    }

    private String getTel() {
        return tel.getText().toString().trim();
    }

    private String gerVercode() {
        return pwd.getText().toString().trim();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
