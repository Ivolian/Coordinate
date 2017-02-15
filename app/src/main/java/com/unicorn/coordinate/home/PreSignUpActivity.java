package com.unicorn.coordinate.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.DialogUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PreSignUpActivity extends EventBusActivity {


    // ====================== initViews ======================

    @Override
    public void initViews() {
        initRxPlayers();
        initSth();
        teamStatus.setText(MatchHelper.myMatchStatusText(myMatchStatus));
        addSwipeListener();
        getPlayers();
        getMyLine();
    }


    // ====================== player ======================

    List<Player> playerList;

    final private PlayerAdapter playerAdapter = new PlayerAdapter();

    private void initRxPlayers() {
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
        playerList = new Gson().fromJson(data.toString(), new TypeToken<List<Player>>() {
        }.getType());

        Player player = playerList.get(0);
        teamName.setText(AESUtils.decrypt2(player.getTeamname()));

        playerAdapter.setPlayerList(playerList);
        playerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void refreshPlayer(RefreshPlayerEvent refreshPlayerEvent) {
        getPlayers();
    }


    // ======================== getMyLine ========================

    MyLine myLine;

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

    String type;

    private void copeResponse2(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<MyLine> myLineList = new Gson().fromJson(data.toString(), new TypeToken<List<MyLine>>() {
        }.getType());

        myLine = myLineList.get(0);
        lineName.setText(myLine.getLinename());

        int myLineStatus = myLine.getStatus();
        if (myLineStatus == 1) {
            type = "2";
        } else if (myLineStatus == 2) {
            type = "1";
        } else {
            addExtra.setVisibility(View.INVISIBLE);
        }
    }


    // ======================== complete ========================

    @OnClick(R.id.complete)
    public void completeOnClick() {
        if (ClickHelper.isSafe()) {
            DialogUtils.showConfirm(this, "是否预报名完成", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    completeSign();
                }
            });
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
        formalSignUp();
        finish();
    }

    private void formalSignUp() {
        Intent intent = new Intent(this, FormalSignUpActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }

    // ======================== cancelTeam ========================

    @OnClick(R.id.cancelTeam)
    public void cancelTeamOnClick() {
        if (ClickHelper.isSafe()) {
            DialogUtils.showConfirm(this, "是否取消组队", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    cancelTeam();
                }
            });
        }
    }

    private void cancelTeam() {
        String url = cancelTeamUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse4(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse4(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("组队已取消");
        finish();
    }


    // ======================== addPlayer ========================

    @OnClick(R.id.addPlayer)
    public void addPlayerOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(this, AddPlayerActivity.class);
            intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
            intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
            startActivity(intent);
        }
    }


    // ======================== addExtra ========================

    @OnClick(R.id.addExtra)
    public void addExtraOnClick() {
        if (ClickHelper.isSafe()) {
            addExtra1();
        }
    }

    private void addExtra1() {
        Intent intent = new Intent(this, ExtraInfoActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        intent.putExtra("type", type);
        startActivity(intent);
    }


    private void addSwipeListener() {
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rvPlayers,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return playerList.get(position).getLeader() != 1;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final Player player = playerList.get(position);
                                    DialogUtils.showConfirm(PreSignUpActivity.this, "确认删除" +  AESUtils.decrypt2(player.getNickname()) + "?", new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            deletePlayer(player.getMobile());
                                            playerAdapter.notifyItemRemoved(position);
                                            playerList.remove(position);
                                            playerAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final Player player = playerList.get(position);
                                    DialogUtils.showConfirm(PreSignUpActivity.this, "确认删除" + AESUtils.decrypt2(player.getNickname()) + "?", new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            deletePlayer(player.getMobile());
                                            playerAdapter.notifyItemRemoved(position);
                                            playerList.remove(position);
                                            playerAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        },
                        new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            }
                        });
        rvPlayers.addOnItemTouchListener(swipeTouchListener);
    }

    private void deletePlayer(String mobile) {
        String url = deletePlayerUrl(mobile);
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

        ToastUtils.show("删除成功");
//        finish();
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

    @BindView(R.id.addExtra)
    TextView addExtra;

    @BindView(R.id.addPlayer)
    TextView addPlayer;

    @BindView(R.id.cancelTeam)
    TextView cancelTeam;


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

    private String completeSignUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/CompleteSign?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private String cancelTeamUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/cancelteam?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private String deletePlayerUrl(String mobile) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/delplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter("mobile", mobile);
        return builder.toString();
    }

    private void initSth() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(this, 3));
        addExtra.setBackgroundDrawable(gradientDrawable);
        addPlayer.setBackgroundDrawable(gradientDrawable);

        GradientDrawable g2 = new GradientDrawable();
        g2.setColor(ContextCompat.getColor(this, R.color.md_red_400));
        g2.setCornerRadius(DensityUtils.dip2px(this, 3));
        cancelTeam.setBackgroundDrawable(g2);
    }


    // ======================== ignore ========================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sign_up);
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
