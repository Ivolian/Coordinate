package com.unicorn.coordinate.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.f2prateek.dart.InjectExtra;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.task.event.StopRefreshingEvent;
import com.unicorn.coordinate.task.event.RefreshTaskEvent;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {


    // ======================== InjectExtra =========================

    @Nullable
    @InjectExtra(Constant.K_SCAN_RESULT)
    String scanResult;


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    @Override
    public void initViews() {
        initSwipeRefreshLayout();
        initRecyclerView();
        if (scanResult != null) {
            copeScanResult(scanResult);
        }
    }

    // ======================== swipeRefreshLayout =========================

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PointHelper.syncTeam();
            }
        });
    }

    @Subscribe
    public void stopRefresh(StopRefreshingEvent stopRefreshingEvent) {
        swipeRefreshLayout.setRefreshing(false);
    }


    // ======================== recyclerView =========================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private TaskAdapter adapter = new TaskAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    // ====================== 扫描任务码 ======================

    @OnClick(R.id.scan)
    public void scanOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(this)) {
            new IntentIntegrator(this)
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
        try {
            PointHelper.copeScanResult(scanResult);
        } catch (Exception e) {
            ToastUtils.show("请扫描任务二维码");
        }
    }


    // ======================== refreshTask =========================

    @Subscribe
    public void refreshTask(RefreshTaskEvent refreshTaskEvent) {
        adapter.refreshTask();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
