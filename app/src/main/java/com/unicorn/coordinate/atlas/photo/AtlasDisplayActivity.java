package com.unicorn.coordinate.atlas.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ImageHelper;
import com.unicorn.coordinate.utils.GlideUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


public class AtlasDisplayActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlas_display);
        initViews();
    }

    public void initViews() {
        tvTitle.setText(title);
        initPhoto();
    }


    // ====================== InjectExtra ======================

    @Nullable
    @InjectExtra(Constant.K_IMG_URL)
    String imgUrl;

    @Nullable
    @InjectExtra(Constant.K_SKETCMAP)
    File sketcmap;

    @InjectExtra(Constant.K_TITLE)
    String title;

    // ====================== views ======================

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    // ====================== back ======================

    private void initPhoto() {
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photo);
        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_START);
        if (imgUrl != null) {
            ImageHelper.loadPicture(imgUrl, photo);
        } else {
            GlideUtils.loadPicture(sketcmap, photo);
        }
        photoViewAttacher.update();
    }


    // ====================== back ======================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
