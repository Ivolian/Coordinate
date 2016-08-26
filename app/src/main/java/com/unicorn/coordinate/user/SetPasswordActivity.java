package com.unicorn.coordinate.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.f2prateek.dart.InjectExtra;
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

/**
 * Created by ivolianer on 2016/8/26.
 */
public class SetPasswordActivity extends BaseActivity {


    @InjectExtra("userId")
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
    }

    @BindView(R.id.pwd)
    AppCompatEditText pwd;


    private boolean isPwdValid() {
        if (TextUtils.isEmpty(pwd.getText())) {
            ToastUtils.show("验证码不能为空");
            return false;
        }
        // TODO 密码本身有什么要求
        return true;
    }

    private String getSetPasswordUrl(String userId, String pwd) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/set_pwd?").buildUpon();
        // TODO 为啥 account 一会是手机号，一会是 userId..
        builder.appendQueryParameter("account", userId);
        builder.appendQueryParameter("pwd", pwd);
        return builder.toString();
    }

    @OnClick(R.id.register)
    public void registerOnClick() {
        if (!ClickHelper.isSafe()) {
            return;
        }

        if (!isPwdValid()) {
            return;
        }
        // TODO
        final KProgressHUD mask = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        Request stringRequest = new JsonObjectRequest(
                getSetPasswordUrl(userId, pwd.getText().toString()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mask.dismiss();
                        try {
                            copeResponse2(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener(mask)
        );
        SimpleVolley.addRequest(stringRequest);

    }


    private void copeResponse2(JSONObject response) throws Exception {
        String code = response.getString(Constant.K_CODE);
        if (code == null) {
            return;
        }

        if (code.equals(Constant.SUCCESS_CODE)) {
            String successMsg = response.getString(Constant.K_DATA);
            if (successMsg != null) {
                ToastUtils.show(successMsg);
                setResult(Constant.REGISTER_SUCCESS_CODE);
                finish();
            }
        } else {
            // TODO 抽象
            String errorMsg = response.getString(Constant.K_MSG);
            if (errorMsg != null) {
                ToastUtils.show(errorMsg);
            }
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
