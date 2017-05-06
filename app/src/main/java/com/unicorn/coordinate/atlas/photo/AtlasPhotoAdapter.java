package com.unicorn.coordinate.atlas.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.atlas.photo.model.AtlasPhoto;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AtlasPhotoAdapter extends RecyclerView.Adapter<AtlasPhotoAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<AtlasPhoto> atlasPhotoList = new ArrayList<>();

    public void setAtlasPhotoList(List<AtlasPhoto> atlasPhotoList) {
        this.atlasPhotoList = atlasPhotoList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                Context context = photo.getContext();
                AtlasPhoto atlasPhoto = atlasPhotoList.get(getAdapterPosition());
                startPhotoDisplayActivity(context, getImgUrl(atlasPhoto));
            }
        }
    }

    private void startPhotoDisplayActivity(Context context, String imgUrl) {
        Intent intent = new Intent(context, AtlasDisplayActivity.class);
        intent.putExtra(Constant.K_TITLE, "照片详情");
        intent.putExtra(Constant.K_IMG_URL, imgUrl);
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AtlasPhoto atlasPhoto = atlasPhotoList.get(position);
        loadPicture(getImgUrl(atlasPhoto), holder.photo);
    }

    private void loadPicture(String imgUrl, ImageView imageView) {
        Context context = SimpleApplication.getInstance();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

    private String getImgUrl(AtlasPhoto atlasPhoto) {
        return ConfigUtils.getImageBaseUrl() + atlasPhoto.getPicture();
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_atlas_photo, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return atlasPhotoList.size();
    }


}
