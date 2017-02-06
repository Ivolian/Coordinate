package com.unicorn.coordinate.home.preSignUp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.EventBusActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.RefreshPlayerEvent;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyLine;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PreSignUpBusActivity extends EventBusActivity {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sign_up);
    }

    @Override
    public void initViews() {
        initRecyclerView();
        initFillInfoAndInvite();
        matchName.setText(matchInfo.getMatch_name());
        teamStatus.setText(statusText());
        getPlayer();
        getMyLine();
    }


    // ====================== getPlayer ======================

    private void getPlayer() {
        String url = playerUrl();
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
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Player> playerList = new Gson().fromJson(data.toString(), new TypeToken<List<Player>>() {
        }.getType());
        Player leader = playerList.get(0);
        teamName.setText(leader.getTeamname());
        playerAdapter.setPlayerList(playerList);
        playerAdapter.notifyDataSetChanged();
    }


    // ======================== getMyLine ========================

    private void getMyLine() {
        String url = myLineUrl();
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
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        MyLine myLine = new Gson().fromJson(data.toString(), MyLine.class);
        lineName.setText(myLine.getName());

        int myLineStatus = myLine.getStatus();
        if (myLineStatus == 1) {
            fillInfo.setText("填写汽车线路");
            fillInfo.setVisibility(View.VISIBLE);
        }
        if (myLineStatus == 2) {
            fillInfo.setText("填写宝宝线路");
            fillInfo.setVisibility(View.VISIBLE);
        }
    }


    // ======================== signUp ========================

    @OnClick(R.id.signUp)
    public void signUpOnClick() {
        if (ClickHelper.isSafe()) {
            completeSign();
        }
    }

    private void completeSign() {
        String url = completeSignUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse3(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse3(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("预报名成功");
        finish();
    }

    // ======================== playerAdapter ========================

    final private PlayerAdapter playerAdapter = new PlayerAdapter();

    private void initRecyclerView() {
        rvTeamInfo.setLayoutManager(new LinearLayoutManager(this));
        rvTeamInfo.setAdapter(playerAdapter);
    }


    // ======================== addPlayer ========================

    @OnClick(R.id.invite)
    public void inviteOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(this, AddPlayerActivity.class);
            intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
            intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
            startActivity(intent);
        }
    }

    //

    @Subscribe
    public void refreshPlayer(RefreshPlayerEvent refreshPlayerEvent) {
        getPlayer();
    }


    // ======================== views ========================

    @BindView(R.id.matchName)
    TextView matchName;

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.teamStatus)
    TextView teamStatus;

    @BindView(R.id.lineName)
    TextView lineName;

    @BindView(R.id.rvTeamInfo)
    RecyclerView rvTeamInfo;

    @BindView(R.id.fillInfo)
    TextView fillInfo;

    @BindView(R.id.invite)
    TextView invite;


    // ======================== 底层方法 ========================

    private String playerUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String myLineUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getmyline?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String completeSignUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/CompleteSign?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private String statusText() {
        switch (myMatchStatus.getStatus()) {
            case "1":
                return "未报名参赛";
            case "2":
                return "已设定队名";
            case "3":
                return "已选择线路";
            case "4":
                return "被邀请，未操作";
            case "5":
                return "预报名完成";
            case "6":
                return "正式报名完成";
            default:
                return "";
        }
    }

    private void initFillInfoAndInvite() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(this, 3));
        fillInfo.setBackgroundDrawable(gradientDrawable);
        invite.setBackgroundDrawable(gradientDrawable);
    }


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
