package com.unicorn.coordinate.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;


public class MatchDetailActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
    }


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH)
    Match match;


    // ====================== initViews ======================

    @Override
    public void initViews() {
        fetchMatchInfo();
    }


    // ====================== fetchMatchInfo ======================

    private MatchInfo matchInfo;

    private void fetchMatchInfo() {
        String url = matchInfoUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        stopAnim();
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener(avi)
        );
        SimpleVolley.addRequest(request);
        startAnim();
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        matchInfo = new Gson().fromJson(data.toString(), MatchInfo.class);
        fillViews();
    }

    private void fillViews() {
        name.setText(matchInfo.getMatch_name());
        date.setText(matchInfo.getDate());
        status.setText(matchStatusText());
        date1.setText(matchInfo.getDate1());
        date2.setText(matchInfo.getDate2());
        date3.setText(matchInfo.getDate3());
        date4.setText(matchInfo.getDate4());
        content.setText(matchInfo.getContent());
        // 加载结束，显示内容
        container.setVisibility(View.VISIBLE);
    }

    private String matchStatusText() {
        switch (matchInfo.getStatus()) {
            case "0":
                return "报名未开始";
            case "1":
                return "立即预报名";
            case "2":
                return "预报名结束";
            case "3":
                return "报名付费";
            case "4":
            case "8":
            case "9":
                return "报名结束";
            case "5":
                return "比赛结束";
            default:
                return "";
        }
    }


    // ====================== getMyMatchStatus ======================

    private MyMatchStatus myMatchStatus;

    private void getMyMatchStatusIfNeed() {
        if (isNeedGetMyMatchStatus()) {
            getMyMatchStatus();
        } else {
            ToastUtils.show(matchStatusText());
        }
    }

    private boolean isNeedGetMyMatchStatus() {
        String matchStatus = matchInfo.getStatus();
        return Arrays.asList("1", "3", "4").contains(matchStatus);
    }

    private void getMyMatchStatus() {
        String url = myMatchStatusUrl();
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
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        myMatchStatus = new Gson().fromJson(data.toString(), MyMatchStatus.class);
        signUp();
    }

    @OnClick(R.id.signUpMatch)
    public void signUpMatchOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(this)){
            getMyMatchStatusIfNeed();
        }
    }


    // ====================== signUp ======================

    private void signUp() {
        switch (myMatchStatus.getStatus()) {
            case "1":
                setTeamName();
                break;
            case "2":
            case "3":
                chooseLine();
                break;
        }
    }

    private void setTeamName() {
        Intent intent = new Intent(this, SetTeamNameActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        startActivity(intent);
    }

    private void chooseLine(){
        Intent intent = new Intent(this, LineChooseActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ====================== views ======================

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.date1)
    TextView date1;

    @BindView(R.id.date2)
    TextView date2;

    @BindView(R.id.date3)
    TextView date3;

    @BindView(R.id.date4)
    TextView date4;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.container)
    ScrollView container;


    // ====================== avi ======================

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    void startAnim() {
        avi.show();
    }

    void stopAnim() {
        avi.hide();
    }


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


    // ======================== low level method ========================

    private String matchInfoUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmatchinfo?matchid=" + match.getMatch_id();
    }

    private String myMatchStatusUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmymatchstatus?matchid=" + match.getMatch_id()
                + "&userid=" + ConfigUtils.getUserId();
    }


}
