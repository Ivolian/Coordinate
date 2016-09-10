package com.unicorn.coordinate.home;

import android.os.Bundle;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.home.model.Match;

import butterknife.BindView;
import butterknife.OnClick;


public class MatchDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
    }

    @Override
    public void initViews() {
        name.setText(match.getMatch_name());
        date.setText(match.getDate());
        status.setText(match.getStatus());
        date1.setText(match.getDate1());
        date2.setText(match.getDate2());
        date3.setText(match.getDate3());
        date4.setText(match.getDate4());
        content.setText(match.getContent());
    }

    @InjectExtra(Constant.K_MATCH)
    Match match;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.date1)
    TextView date1;

    @BindView(R.id.date2)
    TextView date2;

    @BindView(R.id.date3)
    TextView date3;

    @BindView(R.id.date4)
    TextView date4;

    @BindView(R.id.content)
    TextView content;

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
