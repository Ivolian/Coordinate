package com.unicorn.coordinate.message;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.unicorn.coordinate.message.model.Message;
import com.unicorn.coordinate.message.model.MessageDetailReadEvent;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageDetailActivity extends BaseActivity {


    // ======================== InjectExtra =========================

    @InjectExtra(Constant.K_MESSAGE)
    Message message;


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
    }


    // ======================== initViews =========================

    @Override
    public void initViews() {
        date.setText(message.getCreatetime());
        content.setText(message.getContext());

        String type = message.getType();
        if (type != null && (type.equals("3") || type.equals("4"))) {
            operationBarContainer.setVisibility(View.VISIBLE);
        }

        String field2 = message.getField2();
        if (field2 == null) {
            return;
        }
        if (field2.equals("0")) {
            operationBar.setVisibility(View.VISIBLE);
        }
        if (field2.equals("1")) {
            accepted.setVisibility(View.VISIBLE);
        }
        if (field2.equals("2")) {
            rejected.setVisibility(View.VISIBLE);
        }
    }


    // ======================== accept =========================

    @OnClick(R.id.accept)
    public void acceptOnClick() {
        if (ClickHelper.isSafe()) {
            accept();
        }
    }

    private void accept() {
        String url = acceptUrl();
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
        ToastUtils.show("已接受");
        EventBus.getDefault().post(new MessageDetailReadEvent());
        finish();
    }


    // ======================== reject =========================

    @OnClick(R.id.reject)
    public void rejectOnClick() {
        if (ClickHelper.isSafe()) {
            reject();
        }
    }

    private void reject() {
        String url = rejectUrl();
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
        ToastUtils.show("已拒绝");
        EventBus.getDefault().post(new MessageDetailReadEvent());
        finish();
    }


    // ======================== views =========================

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.operationBarContainer)
    PercentLinearLayout operationBarContainer;

    @BindView(R.id.operationBar)
    PercentLinearLayout operationBar;

    @BindView(R.id.accepted)
    TextView accepted;

    @BindView(R.id.rejected)
    TextView rejected;


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


    // ======================== 底层方法 ========================

    private String acceptUrl() {
        String part = message.getType().equals("4") ? "/api/acceptchange?" : "/api/accept?";
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + part).buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_INFO_ID, message.getInfoid());
        return builder.toString();
    }

    private String rejectUrl() {
        String part = message.getType().equals("4") ? "/api/rejectchange?" : "/api/reject?";
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + part).buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_INFO_ID, message.getInfoid());
        return builder.toString();
    }


}
