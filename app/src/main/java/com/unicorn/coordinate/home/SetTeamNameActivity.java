package com.unicorn.coordinate.home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTeamNameActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_team_name);
    }


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;


    // ====================== initViews ======================

    @Override
    public void initViews() {
        matchName.setText(matchInfo.getMatch_name());
        initCheckTeamName();
    }

    private void initCheckTeamName() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(10);
        checkTeamName.setBackgroundDrawable(gradientDrawable);
    }

    @OnClick(R.id.checkTeamName)
    public void checkTeamNameOnClick() {
        if (ClickHelper.isSafe()) {
            if (!TextUtils.isEmpty(teamName.getText())) {
                checkTeamName();
            } else {
                ToastUtils.show("队名不能为空");
            }
        }
    }

    @OnClick(R.id.signUp)
    public void signUpOnClick() {
        if (ClickHelper.isSafe()) {
            if (!TextUtils.isEmpty(teamName.getText())) {
                signUp();
            } else {
                ToastUtils.show("队名不能为空");
            }
        }
    }


    // ====================== 检查队名 ======================

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

    private String checkTeamNameUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/CheckTname?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        builder.appendQueryParameter(Constant.K_TEAM_NAME, teamName.getText().toString().trim());
        return builder.toString();
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("该队名可用");
    }


    // ====================== 报名 ======================

    private void signUp() {
        String url = signUpUrl();
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

    private String signUpUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/RegTeamName?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        // 参数名一直在变...
        builder.appendQueryParameter("tname", teamName.getText().toString().trim());
        builder.appendQueryParameter("tcom", companyName.getText().toString().trim());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private void copeResponse2(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("报名成功");
    }


    // ====================== views ======================

    @BindView(R.id.matchName)
    TextView matchName;

    @BindView(R.id.teamName)
    EditText teamName;

    @BindView(R.id.checkTeamName)
    TextView checkTeamName;

    @BindView(R.id.companyName)
    TextView companyName;


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
