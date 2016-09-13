package com.unicorn.coordinate.user.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initViews() {
        addRegProtocolLink();
    }


    // ======================== regProtocol =========================

    @BindView(R.id.regProtocol)
    AppCompatTextView regProtocol;

    private void addRegProtocolLink() {
        LinkBuilder.on(regProtocol)
                .addLink(getRegProtocolLink())
                .build();
    }

    private Link getRegProtocolLink() {
        return new Link("XXXXX")
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setUnderlined(false)
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        startRegProtocolActivity();
                    }
                });
    }

    private void startRegProtocolActivity() {
        Intent intent = new Intent(this, RegProtocolActivity.class);
        intent.putExtra(Constant.K_TITLE, "注册协议");
        startActivity(intent);
    }

    // ======================== timing =========================

    private final Handler handler = new Handler();

    private int time = 0;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            requestVercode.setText(time == 0 ? "获取验证码" : time + "秒后可重新获得");
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

    @BindView(R.id.vercode)
    AppCompatEditText vercode;

    @BindView(R.id.requestVercode)
    AppCompatButton requestVercode;


    // ======================== 获取验证码 =========================

    @OnClick(R.id.requestVercode)
    public void requestOnClick() {
        if (ClickHelper.isSafe() && !isTiming() && isTelValid()) {
            fetchVercode(getTel());
        }
    }

    private void fetchVercode(String tel) {
        String url = getRequestVercodeUrl(tel);
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
        JSONObject response = new JSONObject(responseString);
        String userId = response.getString(Constant.K_DATA);
        startSetPasswordActivity(userId);
    }

    private void startSetPasswordActivity(final String userId) {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra(Constant.K_USER_ID, userId);
        startActivityForResult(intent, 233);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RC_REGISTER_SUCCESS) {
            finish();
        }
    }


    // ======================== 底层方法 =========================

    private String getRequestVercodeUrl(String tel) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/reg_step1?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, tel);
        return builder.toString();
    }

    private String getCheckVercodeUrl(String tel, String vercode) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/reg_check?").buildUpon();
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
        return vercode.getText().toString().trim();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
