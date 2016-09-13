package com.unicorn.coordinate.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    @Override
    public void initViews() {
        initRecyclerView();
    }


    // ======================== recyclerView =========================

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    final private TaskAdapter adapter = new TaskAdapter();

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
