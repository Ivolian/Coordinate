package com.unicorn.coordinate.user;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.user.model.UserInfo;
import com.unicorn.coordinate.user.register.RegisterActivity;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DialogUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        account.setText(ConfigUtils.getAccount());
        addUnderlines();
    }


    // ======================== views =========================

    @BindView(R.id.account)
    AppCompatEditText account;

    @BindView(R.id.pwd)
    AppCompatEditText pwd;


    // ======================== loginOnClick =========================

    @OnClick(R.id.login)
    public void loginOnClick() {
        if (ClickHelper.isSafe() && isInputValid()) {
            login(getAccount(), getPwd());
        }
    }

    private void login(String account, String pwd) {
        final MaterialDialog mask = DialogUtils.showIndeterminateDialog(this, "登录中", "请稍后...");
        String url = getLoginUrl(account, pwd);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mask.dismiss();
                            copeResponse(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener(mask)
        );
        SimpleVolley.addRequest(request);
    }


    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
        ConfigUtils.saveUserInfo(userInfo);
        ConfigUtils.saveAccount(getAccount());
        ToastUtils.show("登录成功");
        finish();
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


    // ======================== 底层方法 =========================

    private String getLoginUrl(final String account, final String pwd) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/userlogin?").buildUpon();
        builder.appendQueryParameter(Constant.K_ACCOUNT, account);
        builder.appendQueryParameter("passwd", pwd);
        builder.appendQueryParameter("typ", "02");
        return builder.toString();
    }

    private boolean isInputValid() {
        String account = getAccount();
        if (account.equals("")) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (account.length() != 11) {
            ToastUtils.show("手机号位数不正确");
            return false;
        }
        String pwd = getPwd();
        if (pwd.equals("")) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    private String getAccount() {
        return account.getText().toString().trim();
    }

    private String getPwd() {
        return pwd.getText().toString().trim();
    }


}
