package com.unicorn.coordinate.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MatchInstructionActivity extends BaseActivity {

    @InjectExtra(Constant.K_MATCH_INFO)
    public MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    public MyMatchStatus myMatchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_instruction);
    }

    @Override
    public void initViews() {
        tvTitle.setText(title);
        initWebView();
        fetchHtml();
    }

    private void fetchHtml() {
        String url = htmlUrl();
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
        JSONObject response = new JSONObject(responseString);
        String data = response.getString(Constant.K_DATA);
//        webView.loadData(data, "text/html", "UTF-8");
       webView.loadData(data, "text/html; charset=UTF-8", null);
        startTiming();
    }

    protected String htmlUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmatchnotice?matchid=" + matchInfo.getMatch_id();
    }


    // ======================== views =========================

    @BindView(R.id.nextStep)
    TextView nextStep;

    @OnClick(R.id.nextStep)
    public void nextStepOnClick() {
        if (ClickHelper.isSafe()) {
            if (time == 0) {
                setTeamName();
                finish();
            }
        }
    }

    private void setTeamName() {
        Intent intent = new Intent(this, TeamNameSetActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ======================== timing =========================

    private final Handler handler = new Handler();

    private int time = 0;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            nextStep.setText(time == 0 ? "我已阅读并同意，马上预报名" : "请阅读比赛须知（" + time + "S）");
            if (time == 0) {
                return;
            }
            time--;
            handler.postDelayed(this, 1000);
        }
    };

    private void startTiming() {
        time = 10;
        handler.post(runnable);
    }


    // ======================== InjectExtra =========================

    @InjectExtra(Constant.K_TITLE)
    String title;


    // ======================== InjectExtra =========================

    @BindView(R.id.tvTitle)
    TextView tvTitle;


    // ======================== initWebView =========================

    @BindView(R.id.webView)
    WebView webView;

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
//        webSettings.setDefaultTextEncodingName("UTF-8");
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }
}
