package com.unicorn.coordinate.home.preSignUp;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.unicorn.coordinate.home.event.RefreshPlayerEvent;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class AddPlayerActivity extends BaseActivity {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
    }

    @Override
    public void initViews() {
        matchName.setText(matchInfo.getMatch_name());
    }

    // ====================== addPlayer ======================

    @OnClick(R.id.invite)
    public void inviteOnClick() {
        if (ClickHelper.isSafe()) {
            if (TextUtils.isEmpty(mobile.getText())) {
                ToastUtils.show("手机号码不能为空");
            } else {
                addPlayer();
            }
        }
    }

    private void addPlayer() {
        String url = addPlayerUrl();
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
        ToastUtils.show("邀请已发送");
    }


    // ====================== views ======================

    @BindView(R.id.matchName)
    TextView matchName;

    @BindView(R.id.mobile)
    TextView mobile;


    // ====================== low level methods ======================

    private String addPlayerUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/addplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter("mobile", mobile.getText().toString().trim());
        return builder.toString();
    }


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            EventBus.getDefault().post(new RefreshPlayerEvent());
            finish();
        }
    }

    @OnClick(R.id.finish)
    public void finishOnClick() {
        if (ClickHelper.isSafe()) {
            EventBus.getDefault().post(new RefreshPlayerEvent());
            finish();
        }
    }


}
