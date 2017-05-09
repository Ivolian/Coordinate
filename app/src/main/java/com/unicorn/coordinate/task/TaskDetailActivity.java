package com.unicorn.coordinate.task;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.LocalHelper;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.atlas.photo.AtlasDisplayActivity;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.GlideUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskDetailActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }

    @Override
    public void initViews() {
        GlideUtils.loadPicture(new LocalHelper().getLocalTaskLogo(), ivTaskLogo);
        tvPointName.setText(AESUtils.decrypt(point.getPointname()));
        tvPointAddress.setText(point.getPointaddress());
        tvPointTask.setText(Html.fromHtml(AESUtils.decrypt(point.getPointtask())));
        tvPointOut.setText(AESUtils.decrypt(point.getPointout()));
        initSketc();
    }


    // ======================== initSketc =========================

    private void initSketc() {
        makeStyle(tvLinkNo);
        makeStyle(tvSketcmap);
        makeStyle(tvSketvoice);

        String linkNo = point.getLinkno();
        boolean hide = linkNo == null || linkNo.equals("") || linkNo.equals("0");
        tvLinkNo.setVisibility(hide ? View.GONE : View.VISIBLE);

        String sketcmap = point.getSketchmap();
        hide = sketcmap == null || sketcmap.equals("");
        tvSketcmap.setVisibility(hide ? View.GONE : View.VISIBLE);

        String sketcvoice = point.getSketchvoice();
        hide = sketcvoice == null || sketcvoice.equals("");
        tvSketvoice.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void makeStyle(TextView textView) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(10);
        gradientDrawable.setColor(Color.parseColor("#66A3F6"));
        textView.setBackgroundDrawable(gradientDrawable);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(24, 16, 24, 16);
    }


    // ======================== tvLinkNo =========================

    @OnClick(R.id.tvLinkNo)
    public void linkNoOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(this, LinkNoActivity.class);
            intent.putExtra(Constant.K_TITLE, "回答问题");
            intent.putExtra(Constant.K_MATCH_USER_ID, point.getMatchuserid());
            intent.putExtra(Constant.K_LINK_NO, point.getLinkno());
            startActivity(intent);
        }
    }


    // ======================== tvSketcmap =========================

    @OnClick(R.id.tvSketcmap)
    public void sketmapOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(this, AtlasDisplayActivity.class);
            intent.putExtra(Constant.K_TITLE, "示意图");
            intent.putExtra(Constant.K_IMG_URL, point.getSketchmap());
            startActivity(intent);
        }
    }


    // ======================== tvSketvoice =========================

    MediaPlayer mediaPlayer;

    @OnClick(R.id.tvSketvoice)
    public void sketvoiceOnClick() {
        if (ClickHelper.isSafe()) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(point.getSketchvoice());
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    tvSketvoice.setText("播放语音提示");
                } else {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    tvSketvoice.setText("停止语音提示");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // ======================== EXTRA AND VIEWS =========================

    @InjectExtra(Constant.K_POINT)
    Point point;

    @BindView(R.id.ivTaskLogo)
    ImageView ivTaskLogo;

    @BindView(R.id.tvPointName)
    TextView tvPointName;

    @BindView(R.id.tvPointAddress)
    TextView tvPointAddress;

    @BindView(R.id.tvPointTask)
    TextView tvPointTask;

    @BindView(R.id.tvLinkNo)
    TextView tvLinkNo;

    @BindView(R.id.tvSketcmap)
    TextView tvSketcmap;

    @BindView(R.id.tvSketvoice)
    TextView tvSketvoice;

    @BindView(R.id.tvPointOut)
    TextView tvPointOut;


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
