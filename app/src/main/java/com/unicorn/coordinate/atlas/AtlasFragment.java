package com.unicorn.coordinate.atlas;

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
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;


public class AtlasFragment extends LazyLoadFragment {


    // ====================== getLayoutResId ======================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_atlas;
    }


    // ====================== onFirstUserVisible ======================

    @Override
    public void onFirstUserVisible() {
        initRecyclerView();
        fetchAtlasList();
    }


    // ====================== 图集列表 ======================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private AtlasAdapter adapter = new AtlasAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchAtlasList() {
        String url = ConfigUtils.getBaseUrl() + "/api/getpic";
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
        // model 用的是 match
        adapter.setMatchList(matchList);
        adapter.notifyDataSetChanged();
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
