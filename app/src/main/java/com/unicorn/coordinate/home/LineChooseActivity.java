package com.unicorn.coordinate.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.Line;
import com.unicorn.coordinate.home.model.LineType;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.ui.LineTypeView;
import com.unicorn.coordinate.home.ui.LineView;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.DialogUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LineChooseActivity extends BaseActivity {


    // ====================== initViews ======================

    @Override
    public void initViews() {
        teamName.setText(AESUtils.decrypt2(myMatchStatus.getTeamname()));
        date.setText(matchInfo.getDate4());
        area.setText(matchInfo.getArea2());
        getLineType();
        getMyMatchStatusIfNeed();
    }

    // ====================== getLineType ======================

    List<LineType> lineTypeList;

    List<LineTypeView> lineTypeViewList = new ArrayList<>();

    private void getLineType() {
        String url = lineTypeUrl();
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
        lineTypeList = new Gson().fromJson(data.toString(), new TypeToken<List<LineType>>() {
        }.getType());
        renderLineType();
    }

    private void renderLineType() {
        for (final LineType lineType : lineTypeList) {
            final LineTypeView lineTypeView = new LineTypeView(this);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, DensityUtils.dip2px(this, 8), DensityUtils.dip2px(this, 8), 0);
            lineTypeContainer.addView(lineTypeView, lp);
            lineTypeView.setText(lineType.getName());
            lineTypeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineTypeOnChoose(lineType);
                }
            });
            lineTypeViewList.add(lineTypeView);
        }
        lineTypeOnChoose(lineTypeList.get(0));
    }

    private void lineTypeOnChoose(LineType lineType) {
        for (LineTypeView lineTypeView : lineTypeViewList) {
            if (lineTypeView.getText().equals(lineType.getName())) {
                lineTypeView.select();
            } else {
                lineTypeView.unselect();
            }
        }
        getLine(lineType);
    }


    // ====================== getLine ======================

    Line lineChosen;

    List<Line> lineList;

    List<LineView> lineViewList = new ArrayList<>();

    private void getLine(LineType lineType) {
        String url = lineUrl(lineType);
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
        lineList = new Gson().fromJson(data.toString(), new TypeToken<List<Line>>() {
        }.getType());
        renderLine();
    }

    private void renderLine() {
        lineContainer.removeAllViews();
        for (final Line line : lineList) {
            final LineView lineView = new LineView(this);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, DensityUtils.dip2px(this, 8), DensityUtils.dip2px(this, 8), 0);
            lineContainer.addView(lineView, lp);
            lineView.setText(line.getLinename());
            lineView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineOnChoose(line);
                }
            });
            lineViewList.add(lineView);
        }
        lineOnChoose(lineList.get(0));
    }

    private void lineOnChoose(Line line) {
        for (LineView lineView : lineViewList) {
            if (lineView.getText().equals(line.getLinename())) {
                lineView.select();
            } else {
                lineView.unselect();
            }
        }
        content.setText(line.getContent());
        notice.setText(line.getNotice());
        teamPrice.setText("￥" + line.getPrice());
        lineChosen = line;
    }


    // ====================== setLine ======================

    @OnClick(R.id.nextStep)
    public void nextStepOnClick() {
        if (ClickHelper.isSafe()) {
            DialogUtils.showConfirm(this, "是否选择" + lineChosen.getLinename() + "，如需更改需解散队伍，重新组队选择线路", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    setLine();
                }
            });
        }
    }

    private void setLine() {
        String url = setLineUrl();
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
        ToastUtils.show("选择线路成功");
        myMatchStatus.setStatus("3");
        preSignUp();
        finish();
    }

    private void preSignUp() {
        Intent intent = new Intent(this, PreSignUpActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ====================== views ======================

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.area)
    TextView area;

    @BindView(R.id.lineTypeContainer)
    FlowLayout lineTypeContainer;

    @BindView(R.id.lineContainer)
    FlowLayout lineContainer;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.notice)
    TextView notice;

    @BindView(R.id.teamPrice)
    TextView teamPrice;


    // ======================== 底层方法 ========================

    private String lineTypeUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getlinetype?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        return builder.toString();
    }

    private String lineUrl(LineType lineType) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getlinebytype?").buildUpon();
        builder.appendQueryParameter("typeid", lineType.getLineid());
        return builder.toString();
    }

    private String setLineUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/SelLine?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter("lineid", lineChosen.getLinesid());
        return builder.toString();
    }


    // ====================== ignore ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_choose);
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


    //

    private void getMyMatchStatusIfNeed() {
        final String teamId = myMatchStatus.getTeamid();
        if (teamId == null || teamId.equals("")) {
            getMyMatchStatus();
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
        myMatchStatus = new Gson().fromJson(data.toString(), MyMatchStatus.class);
    }

    private String myMatchStatusUrl() {
        return ConfigUtils.getBaseUrl() + "/api/getmymatchstatus?matchid=" + matchInfo.getMatch_id()
                + "&userid=" + ConfigUtils.getUserId();
    }


}
