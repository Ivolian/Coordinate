package com.unicorn.coordinate.profile;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseFragment;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.ReadMessageEvent;
import com.unicorn.coordinate.profile.matchCert.MatchCertActivity;
import com.unicorn.coordinate.profile.setting.SettingActivity;
import com.unicorn.coordinate.profile.userMatch.UserMatchActivity;
import com.unicorn.coordinate.user.model.UserInfo;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile;
    }

    @OnClick(R.id.profile)
    public void profile() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.match)
    public void match() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), UserMatchActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.matchCert)
    public void matchCert() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), MatchCertActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.message)
    public void messageOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            EventBus.getDefault().post(new ReadMessageEvent());
        }
    }

    @OnClick(R.id.setting)
    public void settingOnClick() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
    }


    // ======================== 培训签到扫码 =========================

    @OnClick(R.id.signIn)
    public void signIn() {
        if (ClickHelper.isSafe() && ConfigUtils.checkLogin(getActivity())) {
            IntentIntegrator.forSupportFragment(this)
                    .setPrompt("请扫描比赛二维码")
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
        String url = getUrl(scanResult);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (ResponseHelper.isWrong(response)) {
                                return;
                            }
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
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/signin?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter(Constant.K_MATCH_ID,matchId);
        return builder.toString();
    }


    // ======================== profile =========================

    @BindView(R.id.tvProfile)
    TextView tvProfile;

    private void refreshProfile() {
        UserInfo userInfo = ConfigUtils.getUserInfo();
        if (userInfo == null) {
            tvProfile.setText("点击登录");
            return;
        }
        String text = userInfo.getName() != null ? userInfo.getName() : userInfo.getMobile();
        tvProfile.setText(text);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfile();
    }


}
