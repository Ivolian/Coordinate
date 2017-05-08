package com.unicorn.coordinate.profile.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.task.PointHelper;
import com.unicorn.coordinate.task.model.TaskDao;
import com.unicorn.coordinate.user.UpdatePasswordActivity;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    // ======================== onClick =========================

    @OnClick(R.id.updatePwd)
    public void updatePwd() {
        if (ClickHelper.isSafe()) {
            startActivity(UpdatePasswordActivity.class);
        }
    }

    @OnClick(R.id.logout)
    public void logout() {
        if (ClickHelper.isSafe()) {
            ConfigUtils.logout();
            finish();
        }
    }

    @OnClick(R.id.clearCache)
    public void clearCache() {
        if (ClickHelper.isSafe()) {
            new MaterialDialog.Builder(this)
                    .title("清除缓存")
                    .content("清除缓存可能导致当前比赛信息缺失，请勿在比赛前后及过程中清除缓存。您确定要清除缓存吗？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            TaskDao taskDao = SimpleApplication.getInstance().getTaskDao();
                            taskDao.deleteAll();
                            PointHelper.deleteAll();
                            ToastUtils.show("缓存已清除");
                        }
                    })
                    .show();
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
