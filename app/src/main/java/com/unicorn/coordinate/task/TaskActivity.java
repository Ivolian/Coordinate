package com.unicorn.coordinate.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.utils.MD5Util;
import com.unicorn.coordinate.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {


    // ======================== InjectExtra =========================

    // 通过任务书进来是 null，通过扫码进来是有值的
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
        initRecyclerView();
        if (scanResult != null) {
            copeScanResult(scanResult);
        }
    }


    // ======================== recyclerView =========================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private TaskAdapter adapter = new TaskAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    // ======================== 主体部分 =========================

    private void copeScanResult(String scanResult) {
        if (isLineid(scanResult)) {
            copeLine(scanResult);
        } else {
            copePoint(scanResult);
        }
        ToastUtils.show(scanResult);
    }

    private boolean isLineid(String scanResult) {
        return scanResult.startsWith("{");
    }

    private void copeLine(String scanResult) {

    }

    //    md5("csdxsuccessdx")
    private void copePoint(String scanResult) {
        for (Point point : PointHelper.getPointList()) {
            String pointIdDx = point.getPointid() + "dx";
            String MD5 = MD5Util.getMD5String(pointIdDx);
            if (scanResult.equals(MD5)) {
                ToastUtils.show("ok");
            }
        }
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
