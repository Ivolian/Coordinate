package com.unicorn.coordinate.home;

import android.net.Uri;
import android.os.Bundle;
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
import com.unicorn.coordinate.home.model.Line;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.ui.LineView;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LineChooseActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_choose);
    }


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;


    // ====================== initViews ======================

    @Override
    public void initViews() {
        matchName.setText(matchInfo.getMatch_name());
        fetchLine();
    }


    // ====================== fetchLine ======================

    List<Line> lineList;

    List<LineView> lineViewList = new ArrayList<>();

    private void fetchLine() {
        String url = fetchLineUrl();
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
        lineList = new Gson().fromJson(data.toString(), new TypeToken<List<Line>>() {
        }.getType());
        renderLine();
    }

    private void renderLine() {
        for (final Line line : lineList) {
            final LineView lineView = new LineView(this);
            PercentLinearLayout.LayoutParams lp = new PercentLinearLayout.LayoutParams(PercentLinearLayout.LayoutParams.WRAP_CONTENT, PercentLinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(DensityUtils.dip2px(this, 16), 0, 0, 0);
            lineContainer.addView(lineView, lp);
            lineView.setText(line.getName());
            lineView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineOnChoose(line);
                }
            });
            lineViewList.add(lineView);
        }
    }


    private void lineOnChoose(Line line) {
        for (LineView lineView : lineViewList) {
            if (lineView.getText().equals(line.getName())) {
                lineView.select();
            } else {
                lineView.unselect();
            }
        }
        content.setText(line.getContent());
        teamPrice.setText(line.getTeamPrice() + "");
    }


    // ====================== views ======================

    @BindView(R.id.matchName)
    TextView matchName;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.area)
    TextView area;

    @BindView(R.id.lineContainer)
    PercentLinearLayout lineContainer;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.teamPrice)
    TextView teamPrice;


    // ======================== 底层方法 ========================

    private String fetchLineUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getline?").buildUpon();
        builder.appendQueryParameter(Constant.K_MATCH_ID, matchInfo.getMatch_id());
        return builder.toString();
    }


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
