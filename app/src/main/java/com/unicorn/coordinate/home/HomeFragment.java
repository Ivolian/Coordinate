package com.unicorn.coordinate.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.LazyLoadFragment;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;


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


    // ====================== copeResponse ======================

    private void copeResponse(String responseString) throws Exception {
        if (!isResponseSuccessful(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Match> matchList = new Gson().fromJson(data.toString(), new TypeToken<List<Match>>() {
        }.getType());
        matchAdapter.setMatchList(matchList);
        matchAdapter.notifyDataSetChanged();
    }

    private boolean isResponseSuccessful(String responseString) throws Exception {
        JSONObject response = new JSONObject(responseString);
        String code = response.getString(Constant.K_CODE);
        boolean success = (code != null && code.equals(Constant.RESPONSE_SUCCESS_CODE));
        if (!success) {
            showMsg(responseString);
        }
        return success;
    }

    private void showMsg(String responseString) throws Exception {
        JSONObject response = new JSONObject(responseString);
        String msg = response.getString(Constant.K_MSG);
        if (msg != null) {
            ToastUtils.show(msg);
        }
    }


}
