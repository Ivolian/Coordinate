package com.unicorn.coordinate.home;

import android.content.Intent;
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
import com.unicorn.coordinate.helper.PayStatusHelper;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.PaySuccessEvent;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyLine;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.MyOrder;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FormalSignUpActivity extends EventBusActivity {


    // ====================== initViews ======================

    @Override
    public void initViews() {
        initRvPlayers();
        teamStatus.setText(MatchHelper.myMatchStatusText(myMatchStatus));

        getPlayers();
        getMyLine();
        checkPay();
    }


    // ====================== players ======================

    final private PlayerAdapter playerAdapter = new PlayerAdapter();

    private void initRvPlayers() {
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        rvPlayers.setAdapter(playerAdapter);
    }

    private void getPlayers() {
        String url = playersUrl();
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

        // 展示接受邀请的人员。
        List<Player> playerListAccepted = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getStatus().equals("1")) {
                playerListAccepted.add(player);
            }
        }
        playerAdapter.setPlayerList(playerListAccepted);
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
        payPrice.setText("￥" + myLine.getPrice());
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
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<MyOrder> myOrderList = new Gson().fromJson(data.toString(), new TypeToken<List<MyOrder>>() {
        }.getType());
        pay(myOrderList.get(0));
    }

    private void pay(MyOrder myOrder) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra(Constant.K_MY_ORDER, myOrder);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }

    @Subscribe
    public void onPaySuccess(PaySuccessEvent paySuccessEvent) {
        finish();
    }


    // ======================== 获取支付状态 ========================

    private void checkPay() {
        String url = checkPayUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponseZ(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponseZ(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        String status = data.getString("status");
        payStatus.setText(PayStatusHelper.payStatusText(status));

        // 报名付费 & 领队
        if (myMatchStatus.getMacthStatus().equals("3") && myMatchStatus.getIsLeader().equals("1")
//                && status.equals("0")
                ) {
            pay.setVisibility(View.VISIBLE);
        }
    }


    // ======================== 底层方法 ========================

    private String playersUrl() {
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
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String checkPayUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/checkpay?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }


    // ======================== views ========================

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.teamStatus)
    TextView teamStatus;

    @BindView(R.id.lineName)
    TextView lineName;

    @BindView(R.id.rvPlayers)
    RecyclerView rvPlayers;

    @BindView(R.id.payPrice)
    TextView payPrice;

    @BindView(R.id.payStatus)
    TextView payStatus;

    @BindView(R.id.pay)
    TextView pay;


    // ======================== ignore ========================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formal_sign_up);
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
