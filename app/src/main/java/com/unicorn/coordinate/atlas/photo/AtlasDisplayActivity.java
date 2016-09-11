package com.unicorn.coordinate.atlas.photo;

import android.os.Bundle;
import android.widget.ImageView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ImageHelper;

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
        initPhoto();
    }


    // ====================== InjectExtra ======================

    @InjectExtra(Constant.K_IMG_URL)
    String imgUrl;


    // ====================== views ======================

    @BindView(R.id.photo)
    ImageView photo;


    // ====================== back ======================

    private void initPhoto() {
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photo);
        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_START);
        ImageHelper.loadPicture(imgUrl, photo);
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
