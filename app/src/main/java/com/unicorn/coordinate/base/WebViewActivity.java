package com.unicorn.coordinate.base;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class WebViewActivity extends BaseActivity {


    // ======================== abstract =========================

    protected abstract String getUrl();


    // ======================== InjectExtra =========================

    @InjectExtra(Constant.K_TITLE)
    String title;


    // ======================== InjectExtra =========================

    @BindView(R.id.tvTitle)
    TextView tvTitle;


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
    }

    @Override
    public void initViews() {
        tvTitle.setText(title);
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
        webView.loadUrl(getUrl());
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
