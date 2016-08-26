package com.unicorn.coordinate.user;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONObject;

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

    // ======================== 登录 =========================

    @BindView(R.id.account)
    AppCompatEditText account;

    @BindView(R.id.pwd)
    AppCompatEditText pwd;



    private String getLoginUrl(String account, String pwd) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/userlogin?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, account);
        builder.appendQueryParameter(Constant.K_PWD, pwd);
        return builder.toString();
    }

    private boolean isInputValid() {
        if (TextUtils.isEmpty(account.getText())) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(pwd.getText())) {
            ToastUtils.show("密码不能为空");
            return false;
        }

        return true;
    }

    @OnClick(R.id.login)
    public void login() {
        if (!ClickHelper.isSafe()) {
            return;
        }

        if (!isInputValid()) {
            return;
        }
        final KProgressHUD mask = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        Request stringRequest = new JsonObjectRequest(
                getLoginUrl(getAccount(), pwd.getText().toString()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mask.dismiss();
                        try {
//                            copeResponse2(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener(mask)
        );
        SimpleVolley.addRequest(stringRequest);

    }

    private String getAccount(){
        return  account.getText().toString().replace(" ","");
    }


    private void copeResponse2(JSONObject response) throws Exception {
        String code = response.getString(Constant.K_CODE);
        if (code == null) {
            return;
        }

        if (code.equals(Constant.SUCCESS_CODE)) {
            String userId = response.getString(Constant.K_DATA);
            if (userId != null) {
//                TinyDB tinyDB = new TinyDB(SimpleApplication.getInstance());
//                tinyDB.putString(Constant.K_USER_ID, userId);
            }
        } else {
            // TODO 抽象
            String errorMsg = response.getString(Constant.K_MSG);
            if (errorMsg != null) {
                ToastUtils.show(errorMsg);
            }
        }

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
