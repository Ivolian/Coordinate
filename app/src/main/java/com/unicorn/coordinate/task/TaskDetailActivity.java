package com.unicorn.coordinate.task;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskDetailActivity extends BaseActivity {


    // ======================== content =========================

    @InjectExtra(Constant.K_CONTENT)
    String content;


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }

    @Override
    public void initViews() {
        initWebView();
    }


    // ======================== initWebView =========================

    @BindView(R.id.webView)
    WebView webView;

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
