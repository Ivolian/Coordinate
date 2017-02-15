package com.unicorn.coordinate.message;

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
import com.unicorn.coordinate.base.LazyLoadFragment;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.message.model.Message;
import com.unicorn.coordinate.message.model.MessageDetailReadEvent;
import com.unicorn.coordinate.message.model.MessageReadEvent;
import com.unicorn.coordinate.message.model.UserChangeEvent;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;


public class MessageFragment extends LazyLoadFragment {


    // ====================== getLayoutResId ======================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_message;
    }


    // ====================== onFirstUserVisible ======================

    @Override
    public void onFirstUserVisible() {
        initRecyclerView();
        fetchMessageList();
    }

    @Override
    public void onUserVisible() {
        EventBus.getDefault().post(new MessageReadEvent());

    }
    // ====================== 图集列表 ======================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private MessageAdapter adapter = new MessageAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchMessageList() {
        String url = getUrl(ConfigUtils.getUserId());
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

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Message> messageList = new Gson().fromJson(data.toString(), new TypeToken<List<Message>>() {
        }.getType());
        adapter.setMessageList(messageList);
        adapter.notifyDataSetChanged();
    }

    private String getUrl(final String userId) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getmessage?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, userId);
        return builder.toString();
    }

    // ====================== 设置全部未读消息为已读 ======================




    // ====================== avi ======================

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    void startAnim() {
        avi.show();
    }

    void stopAnim() {
        avi.hide();
    }


    // ====================== eventBus ======================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onMessageReadEvent(MessageDetailReadEvent MessageReadEvent) {
        fetchMessageList();
    }


    @Subscribe
    public void onUserChangeEvent(UserChangeEvent userChangeEvent) {
        fetchMessageList();
    }


}
