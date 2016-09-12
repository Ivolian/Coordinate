package com.unicorn.coordinate.message;

import android.os.Bundle;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.message.model.Message;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageDetailActivity extends BaseActivity {


    // ======================== InjectExtra =========================

    @InjectExtra(Constant.K_MESSAGE)
    Message message;


    // ======================== views =========================

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.content)
    TextView content;


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
    }

    @Override
    public void initViews() {
        date.setText(message.getCreatetime());
        content.setText(message.getContext());
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
