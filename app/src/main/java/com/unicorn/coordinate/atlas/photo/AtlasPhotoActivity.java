package com.unicorn.coordinate.atlas.photo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.atlas.photo.model.AtlasPhoto;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.helper.SpaceItemDecoration;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class AtlasPhotoActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlas_photo);
    }

    @Override
    public void initViews() {
        initRecyclerView();
        fetchAtlasDetail();
    }

    // ====================== InjectExtra ======================

    @InjectExtra(Constant.K_MATCH_ID)
    String matchId;


    // ====================== 照片 ======================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private AtlasPhotoAdapter adapter = new AtlasPhotoAdapter();

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
    }

    private void fetchAtlasDetail() {
        String url = getUrl(matchId);
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

    private String getUrl(String matchId) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getpicdetail?").buildUpon();
        builder.appendQueryParameter("match_id", matchId);
        return builder.toString();
    }


    // ====================== copeResponse ======================

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<AtlasPhoto> atlasPhotoList = new Gson().fromJson(data.toString(), new TypeToken<List<AtlasPhoto>>() {
        }.getType());
        adapter.setAtlasPhotoList(atlasPhotoList);
        adapter.notifyDataSetChanged();
    }


    // ====================== back ======================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
