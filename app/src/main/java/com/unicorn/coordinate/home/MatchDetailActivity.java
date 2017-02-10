package com.unicorn.coordinate.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.unicorn.coordinate.home.event.ReadMessageEvent;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DialogUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;


public class MatchDetailActivity extends BaseActivity {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH)
    Match match;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
    }

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
        status.setText(MatchHelper.matchStatusText(matchInfo));
        date1.setText(matchInfo.getDate1());
        date2.setText(matchInfo.getDate2());
        date3.setText(matchInfo.getDate3());
        date4.setText(matchInfo.getDate4());
        content.setText(matchInfo.getContent());
        // 加载结束，显示内容，之前为不可见
        container.setVisibility(View.VISIBLE);
    }


    // ====================== getMyMatchStatus ======================

    private MyMatchStatus myMatchStatus;

    @OnClick(R.id.signUp)
    public void signUpOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(this)) {
            getMyMatchStatusIfNeed();
        }
    }

    private void getMyMatchStatusIfNeed() {
        // TODO 可以点击的只有1和3？
        if (MatchHelper.isNeedGetMyMatchStatus(matchInfo)) {
            getMyMatchStatus();
        } else {
            // TODO 不能点击时提示比赛状态
            ToastUtils.show(MatchHelper.matchStatusText(matchInfo));
        }
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
//        readMatchInstruction();
        signUp();
    }

    private void readMatchInstruction() {
        Intent intent = new Intent(this, MatchInstructionActivity.class);
        intent.putExtra(Constant.K_TITLE, "比赛须知");
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ====================== signUp ======================

    /*
        1、未报名参赛    => 进入队名设定
        2、已设定队名    => 进入线路选择
        3、已选择线路    => 进入预报名界面
        4、被邀请，未操作 => 弹出对话框，你有被邀请信息，请马上处理，进入读取信息页面
        5、预报名完成    => 正式报名界面 （队员没有预报名）
        6、正式报名完成
     */

    private void signUp() {
        switch (myMatchStatus.getStatus()) {
            case "1":
                readMatchInstruction();
                break;
            case "2":
                chooseLine();
                break;
            case "3":
                preSignUp();
                break;
            case "4":
                DialogUtils.showConfirm(this, "你有被邀请信息，请马上处理", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EventBus.getDefault().post(new ReadMessageEvent());
                        finish();
                    }
                });
                break;
            case "5":
            case "6":
                formalSignUp();
                break;
        }
    }

    private void setTeamName() {
        Intent intent = new Intent(this, TeamNameSetActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }

    private void chooseLine() {
        Intent intent = new Intent(this, LineChooseActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }

    private void preSignUp() {
        Intent intent = new Intent(this, PreSignUpActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }

    private void formalSignUp() {
        Intent intent = new Intent(this, FormalSignUpActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ======================== low level method ========================

    private String matchInfoUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmatchinfo?matchid=" + match.getMatch_id();
    }

    private String myMatchStatusUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmymatchstatus?matchid=" + match.getMatch_id()
                + "&userid=" + ConfigUtils.getUserId();
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


    // ====================== ignore ======================

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    void startAnim() {
        avi.show();
    }

    void stopAnim() {
        avi.hide();
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
