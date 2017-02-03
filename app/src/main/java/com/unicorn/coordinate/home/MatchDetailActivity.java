package com.unicorn.coordinate.home;

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
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

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
        fetchMatchDetail();
    }

    private void fetchMatchDetail() {
        String url = getMatchDetailUrl(match.getMatch_id());
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

    private String getMatchDetailUrl(final String matchId) {
        return ConfigUtils.getBaseUrl() + "/api/getmatchinfo?matchid=" + matchId;
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        MatchInfo matchInfo = new Gson().fromJson(data.toString(), MatchInfo.class);
        fillViews(matchInfo);
    }

    private void fillViews(MatchInfo matchInfo) {
        name.setText(matchInfo.getMatch_name());
        date.setText(matchInfo.getDate());
        status.setText(getStatusText(matchInfo));
        date1.setText(matchInfo.getDate1());
        date2.setText(matchInfo.getDate2());
        date3.setText(matchInfo.getDate3());
        date4.setText(matchInfo.getDate4());
        content.setText(matchInfo.getContent());
        container.setVisibility(View.VISIBLE);
    }

    private String getStatusText(MatchInfo matchInfo) {
        switch (matchInfo.getStatus()) {
            case "1":
                return "报名未开始";
            case "2":
                return "立即预报名";
            case "3":
                return "预报名结束";
            case "4":
                return "报名付费";
            case "5":
                return "报名结束";
            case "6":
                return "比赛结束";
            default:
                return "";
        }
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


}
