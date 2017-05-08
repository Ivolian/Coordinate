package com.unicorn.coordinate.profile.matchCert;

import android.os.Bundle;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.Task;
import com.unicorn.coordinate.task.model.TaskDao;
import com.unicorn.coordinate.utils.AESUtils;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.OnClick;


public class MatchFinishCertActivity extends BaseActivity {

    @InjectExtra(Constant.K_POINT)
    Point point;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_finish_cert);
        initViews();
    }

    @BindView(R.id.tvTeamName)
    TextView tvTeamName;

    @BindView(R.id.tvTeamNo)
    TextView tvTeamNo;

    @BindView(R.id.tvNickName)
    TextView tvNickName;

    @BindView(R.id.tvLineName)
    TextView tvLineName;

    public void initViews() {
        TaskDao taskDao = SimpleApplication.getInstance().getTaskDao();
        task = taskDao.queryBuilder().unique();
        tvTeamName.setText("队伍名称:  " + AESUtils.decrypt2(task.getTeamname()));
        tvTeamNo.setText("队号:  " + task.getTeamno());
        tvNickName.setText("姓名:  " + AESUtils.decrypt2(task.getNickname()));
        tvLineName.setText("线路名称:  " + AESUtils.decrypt2(task.getLinename()));

        DateTime dateTime = new DateTime(point.getPointtime());
        String date = dateTime.toString("yyyy年MM月dd日");
        String match = AESUtils.decrypt2(task.getMatch_name());
        String description = "于" + date + "顺利完成" + match + "。";
        tvDescription.setText(description);
    }

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
