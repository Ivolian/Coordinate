package com.unicorn.coordinate.atlas;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoDisplayActivity extends BaseActivity {


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        initViews();
    }

    public void initViews() {
        initPhoto();
        addPhotoView();
    }


    // ====================== InjectExtra ======================

    @InjectExtra(Constant.K_IMG_URL)
    String imgUrl;


    // ====================== views ======================

    @BindView(R.id.photo)
    ImageView photo;


    private void initPhoto() {
        loadImage(this, imgUrl, photo);
    }

    private void addPhotoView() {
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photo);
        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_START);
        photoViewAttacher.update();
    }

    private void loadImage(Context context, String imgUrl, ImageView ivPicture) {
        Glide.with(context).load(imgUrl).placeholder(R.drawable.placeholder).into(ivPicture);
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
