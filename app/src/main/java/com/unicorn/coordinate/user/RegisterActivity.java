package com.unicorn.coordinate.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initViews() {

    }


    @BindView(R.id.tel)
    AppCompatEditText tel;

    // vercode 代表验证码。。
    @OnClick(R.id.requestVercode)
    public void requestVercode() {
        if (!ClickHelper.isSafe()) {
            return;
        }

        if (!isTelValid()) {
            return;
        }



        Request stringRequest = new JsonObjectRequest(
                getRequestVercodeUrl(getTel()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(stringRequest);

    }

    private String getTel(){
        return  tel.getText().toString().replace(" ","");
    }


    private void copeResponse(JSONObject response) throws Exception {
        String code = response.getString(Constant.K_CODE);
        if (code != null && !code.equals(Constant.RESPONSE_SUCCESS_CODE)) {
            String errorMsg = response.getString(Constant.K_MSG);
            if (errorMsg != null) {
                ToastUtils.show(errorMsg);
            }
        }
    }

    private boolean isTelValid() {
        if (TextUtils.isEmpty(tel.getText())) {
            ToastUtils.show("手机号不能为空");
            return false;
        }
        // TODO 验证手机号合法性，或交给后台处理
        return true;
    }


    private String getRequestVercodeUrl(String tel) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/reg_step1?").buildUpon();
        builder.appendQueryParameter("account", tel);
        return builder.toString();
    }

    // ======================== 注册 =========================

    @BindView(R.id.vercode)
    AppCompatEditText vercode;


    private String getCheckVercodeUrl(String tel, String vercode) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/reg_check?").buildUpon();
        builder.appendQueryParameter("account", tel);
        builder.appendQueryParameter("vercode", vercode);
        return builder.toString();
    }

    private boolean isVercodeValid() {
        if (TextUtils.isEmpty(vercode.getText())) {
            ToastUtils.show("验证码不能为空");
            return false;
        }
        return true;
    }

    @OnClick(R.id.nextStep)
    public void checkVercode() {
        if (!ClickHelper.isSafe()) {
            return;
        }

        if (!isVercodeValid()) {
            return;
        }



        Request stringRequest = new JsonObjectRequest(
                getCheckVercodeUrl(getTel(), vercode.getText().toString()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            copeResponse2(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(stringRequest);

    }


    private void copeResponse2(JSONObject response) throws Exception {
        String code = response.getString(Constant.K_CODE);
        if (code == null) {
            return;
        }

        if (code.equals(Constant.RESPONSE_SUCCESS_CODE)) {
            String userId = response.getString(Constant.K_DATA);
            if (userId != null) {
//                TinyDB tinyDB = new TinyDB(SimpleApplication.getInstance());
//                tinyDB.putString(Constant.K_USER_ID, userId);
                gotoSetPasswordActivity(userId);
            }
        } else {
            // TODO 抽象
            String errorMsg = response.getString(Constant.K_MSG);
            if (errorMsg != null) {
                ToastUtils.show(errorMsg);
            }
        }

    }

    private void gotoSetPasswordActivity(final String userId) {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra(Constant.K_USER_ID, userId);
        startActivityForResult(intent,2333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.REGISTER_SUCCESS_CODE){
            finish();
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
