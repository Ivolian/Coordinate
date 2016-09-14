package com.unicorn.coordinate.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.LazyLoadFragment;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.task.TaskActivity;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends LazyLoadFragment {


    // ====================== getLayoutResId ======================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }


    // ====================== onFirstUserVisible ======================

    @Override
    public void onFirstUserVisible() {
        initRecyclerView();
        fetchMatchList();
    }


    // ====================== 赛事列表 ======================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private MatchAdapter matchAdapter = new MatchAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(matchAdapter);
    }

    private void fetchMatchList() {
        String url = ConfigUtils.getBaseUrl() + "/api/getmatchlist";
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


    // ====================== copeResponse ======================

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Match> matchList = new Gson().fromJson(data.toString(), new TypeToken<List<Match>>() {
        }.getType());
        matchAdapter.setMatchList(matchList);
        matchAdapter.notifyDataSetChanged();
    }


    // ====================== 扫描任务码 ======================

    @OnClick(R.id.scan)
    public void scanOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            IntentIntegrator.forSupportFragment(this)
                    .setPrompt("请扫描任务二维码")
                    .initiateScan();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                copeScanResult(result.getContents());
            }
        }
    }

    private void copeScanResult(String scanResult) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.putExtra(Constant.K_SCAN_RESULT, scanResult);
        startActivity(intent);
    }


    // ====================== 任务书 ======================

    @OnClick(R.id.task)
    public void taskOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            startActivity(intent);
        }
    }


    // ====================== avi ======================

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    void startAnim() {
        avi.show();
    }

    void stopAnim() {
        avi.hide();
    }


}
