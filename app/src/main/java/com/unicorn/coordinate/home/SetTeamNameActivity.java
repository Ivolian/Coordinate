package com.unicorn.coordinate.home;

import android.os.Bundle;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.home.model.Match;

public class SetTeamNameActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_team_name);
    }


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH)
    Match match;


    // ====================== initViews ======================

    @Override
    public void initViews() {
    }


}
