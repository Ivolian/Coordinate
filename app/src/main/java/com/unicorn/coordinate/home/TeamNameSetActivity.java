package com.unicorn.coordinate.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.DialogUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamNameSetActivity extends BaseActivity {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name_set);
    }

    @Override
    public void initViews() {
        initCheckTeamName();
    }


    // ====================== 检查队名 ======================

    @OnClick(R.id.checkTeamName)
    public void checkTeamNameOnClick() {
        if (ClickHelper.isSafe()) {
            if (!TextUtils.isEmpty(etTeamName.getText())) {
                checkTeamName();
            } else {
                ToastUtils.show("队名不能为空");
            }
        }
    }

    private void checkTeamName() {
        String url = checkTeamNameUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
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
        ToastUtils.show("该队名可用");
    }


    // ====================== 下一步 ======================

    @OnClick(R.id.nextStep)
    public void nextStepOnClick() {
        if (ClickHelper.isSafe()) {
            if (!TextUtils.isEmpty(etTeamName.getText())) {
                DialogUtils.showConfirm(this, "是否填写" + etTeamName.getText() +
                        "作为队名，提交之后不能更换", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        regTeamName();
                    }
                });
            } else {
                ToastUtils.show("队名不能为空");
            }
        }
    }

    private void regTeamName() {
        String url = regTeamNameUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse2(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse2(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("队名设定成功");
        chooseLine();

        finish();
    }

    private void chooseLine() {
        Intent intent = new Intent(this, LineChooseActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ======================== low level methods ========================

    private String checkTeamNameUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/CheckTname?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        builder.appendQueryParameter(Constant.K_TEAM_NAME, etTeamName.getText().toString().trim());
        return builder.toString();
    }

    private String regTeamNameUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/RegTeamName?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter("tname", etTeamName.getText().toString().trim());
        builder.appendQueryParameter("tcom", etCompanyName.getText().toString().trim());
        return builder.toString();
    }

    private void initCheckTeamName() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(this, 5));
        checkTeamName.setBackgroundDrawable(gradientDrawable);
    }


    // ====================== views ======================

    @BindView(R.id.etTeamName)
    EditText etTeamName;

    @BindView(R.id.checkTeamName)
    TextView checkTeamName;

    @BindView(R.id.etCompanyName)
    TextView etCompanyName;


    // ======================== ignore ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
