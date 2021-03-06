package com.unicorn.coordinate.home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.unicorn.coordinate.home.event.RefreshPlayerEvent;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddPlayerActivity extends BaseActivity {


    // ====================== initViews ======================

    @Override
    public void initViews() {
        initSth();
        initRvPlayers();
        getPlayers();
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
        List<Player> playerList = new Gson().fromJson(data.toString(), new TypeToken<List<Player>>() {
        }.getType());

        // 显示除了队长之外的队员
        List<Player> playerListBesideLeader = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getLeader() != 1) {
                playerListBesideLeader.add(player);
            }
        }
        playerAdapter.setPlayerList(playerListBesideLeader);
        playerAdapter.notifyDataSetChanged();
    }


    // ====================== addPlayer ======================

    @OnClick(R.id.addPlayer)
    public void addPlayerOnClick() {
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
        getPlayers();
    }


    // ====================== views ======================

    @BindView(R.id.mobile)
    TextView mobile;

    @BindView(R.id.rvPlayers)
    RecyclerView rvPlayers;

    @BindView(R.id.addPlayer)
    TextView addPlayer;


    // ====================== low level methods ======================

    private String playersUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String addPlayerUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/addplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter("mobile", mobile.getText().toString().trim());
        return builder.toString();
    }

    private void initSth() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(this, 3));
        addPlayer.setBackgroundDrawable(gradientDrawable);
    }


    // ======================== ignore ========================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
    }

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

    @Override
    public void onBackPressed() {
        if (ClickHelper.isSafe()) {
            EventBus.getDefault().post(new RefreshPlayerEvent());
            finish();
        }
    }


}
