package com.unicorn.coordinate.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.profile.model.UserMatch;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMatchActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_match);
    }

    @Override
    public void initViews() {
        initRecyclerView();
        fetchMyMatchList();
    }


    // ======================== recyclerView =========================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private UserMatchAdapter adapter = new UserMatchAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchMyMatchList() {
        String url = getUrl(ConfigUtils.getUserId());
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
        List<UserMatch> userMatchList = new Gson().fromJson(data.toString(), new TypeToken<List<UserMatch>>() {
        }.getType());
        adapter.setUserMatchList(userMatchList);
        adapter.notifyDataSetChanged();
    }

    private String getUrl(final String userId) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getusermatch?").buildUpon();
        builder.appendQueryParameter("userid", userId);
        return builder.toString();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
