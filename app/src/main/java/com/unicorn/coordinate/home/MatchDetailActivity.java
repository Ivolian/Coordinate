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
        if (!ConfigUtils.notLogin()) {
            getMyMatchStatusFirst();
        }
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
        tvSignUp.setText(MatchHelper.matchStatusText(matchInfo));
        // 加载结束，显示内容，之前为不可见
        container.setVisibility(View.VISIBLE);
    }


    // ====================== getMyMatchStatusFirst ======================

    private MyMatchStatus myMatchStatus;

    /*
    0 已创建
    显示“报名未开始” 不可点击

    1 预报名开始可以开始预报名
    显示：立即预报名，后续根据status操作，现在是对的

    2 预报名结束不能预报名
    显示：预报名已结束
    status=6的可以点击进去，到预报名完成界面，付费按钮不显示

    3 正式报名开始可以支付
    status=6 显示 ”立即支付“ 按钮可以点击进入
    status=7 显示 “已完成报名”，可以点击进入现在的完成页面(付费页面不显示按钮)，下方付费按钮不显示

    9 正式报名结束只有此状态下可以替换队员更换队长
    status=7的可以点击进去现在完成页面(付费页面不显示按钮)，下面的立即支付按钮不显示

    4 比赛开始不能预报名不能报名
    显示比赛中，不判断status

    5 比赛结束不能预报名不能报名
    显示比赛结束，不判断status

    8 比赛开始前准备(锁定信息，不能更换队员)
    显示比赛中，不判断status
     */


    private void getMyMatchStatusFirst() {
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
        tvSignUp.setText(MatchHelper.signUpText(myMatchStatus));
    }

    private void getMyMatchStatus() {
        String url = myMatchStatusUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse22(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse22(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        myMatchStatus = new Gson().fromJson(data.toString(), MyMatchStatus.class);
        tvSignUp.setText(MatchHelper.signUpText(myMatchStatus));
        signUp();
    }


    @OnClick(R.id.signUp)
    public void signUpOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(this)) {
            getMyMatchStatus();
        }
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
        String matchStatus = myMatchStatus.getMacthStatus();
        String status = myMatchStatus.getStatus();
        switch (matchStatus) {
            case "0":
            case "4":
            case "5":
            case "8":
                break;
            case "1":
                preSignUpZ();
                break;
            case "2":
                if (status.equals("6")) {
                    formalSignUp();
                }
                break;
            case "3":
                if (status.equals("6") || status.equals("7")) {
                    formalSignUp();
                }
                break;
            case "9":
                if (status.equals("7")) {
                    formalSignUp();
                }
                break;
        }


//        switch (myMatchStatus.getStatus()) {
//            case "1":
//                readMatchInstruction();
//                break;
//            case "2":
//                chooseLine();
//                break;
//            case "3":
//                preSignUp();
//                break;
//            case "4":
//                DialogUtils.showConfirm(this, "你有被邀请信息，请马上处理", new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        EventBus.getDefault().post(new ReadMessageEvent());
//                        finish();
//                    }
//                });
//                break;
//            case "5":
//            case "6":
//            case "7":
//                formalSignUp();
//                break;
//        }
    }

    private void preSignUpZ() {
        switch (myMatchStatus.getStatus()) {
            case "1":
                readMatchInstruction();
                break;
            case "2":
                chooseLine();
                break;
            case "3":
            case "5":
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
        }
    }

    private void readMatchInstruction() {
        Intent intent = new Intent(this, MatchInstructionActivity.class);
        intent.putExtra(Constant.K_TITLE, "比赛须知");
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

    @BindView(R.id.signUp)
    TextView tvSignUp;

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
