package com.unicorn.coordinate.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.LazyLoadFragment;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.task.TaskActivity;
import com.unicorn.coordinate.task.scan.ScanActivity;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.uuzuche.lib_zxing.activity.CodeUtils;
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


    // ====================== TODO 扫描二维码 ======================

    private final int SCAN_REQUEST_CODE = 233;

    @OnClick(R.id.scan)
    public void scanOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(getActivity(), ScanActivity.class);
            startActivityForResult(intent, SCAN_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    copeScanResult(result);
                }
            }
        }
    }


    // ====================== task ======================

    @OnClick(R.id.task)
    public void taskOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            startActivity(intent);
        }
    }

    private void copeScanResult(String scanResult) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.putExtra(Constant.K_SCAN_RESULT, scanResult);
        startActivity(intent);
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
