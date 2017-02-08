package com.unicorn.coordinate.home;

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
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyLine;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FormalSignUpActivity extends BaseActivity {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formal_sign_up);
    }

    @Override
    public void initViews() {
        initRecyclerView();
        teamStatus.setText(statusText());
        getPlayer();
        getMyLine();

        if (myMatchStatus.getMacthStatus().equals("3") && myMatchStatus.getIsLeader().equals("1")){
            pay.setVisibility(View.VISIBLE);
        }
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
        teamName.setText(AESUtils.decrypt2(leader.getTeamname()));

            // TODO 过滤一下 只展示接受邀请的人员。
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
        List<MyLine> myLineList = new Gson().fromJson(data.toString(), new TypeToken<List<MyLine>>() {
        }.getType());
        MyLine myLine = myLineList.get(0);
        lineName.setText(myLine.getLinename());
    }


    // ======================== getOrder ========================

    @OnClick(R.id.pay)
    public void payOnClick() {
        if (ClickHelper.isSafe()) {
            getOrder();
        }
    }

    private void getOrder() {
        String url = orderUrl();
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
        ToastUtils.show("正式报名成功");
        finish();
    }

    // ======================== playerAdapter ========================

    final private PlayerAdapter playerAdapter = new PlayerAdapter();

    private void initRecyclerView() {
        rvTeamInfo.setLayoutManager(new LinearLayoutManager(this));
        rvTeamInfo.setAdapter(playerAdapter);
    }





    // ======================== views ========================

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.teamStatus)
    TextView teamStatus;

    @BindView(R.id.lineName)
    TextView lineName;

    @BindView(R.id.rvTeamInfo)
    RecyclerView rvTeamInfo;

    @BindView(R.id.payPrice)
    TextView payPrice;

    @BindView(R.id.payStatus)
    TextView payStatus;

    @BindView(R.id.pay)
    TextView pay;


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

    private String orderUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getmyorder?").buildUpon();
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


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
