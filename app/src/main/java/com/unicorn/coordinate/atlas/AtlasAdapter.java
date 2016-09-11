package com.unicorn.coordinate.atlas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AtlasAdapter extends RecyclerView.Adapter<AtlasAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Match> matchList = new ArrayList<>();

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logopic)
        ImageView logopic;

        @BindView(R.id.name)
        TextView name;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                Context context = logopic.getContext();
                Match match = matchList.get(getAdapterPosition());
                startAtlasDetailActivity(context, match);
            }
        }
    }

    private void startAtlasDetailActivity(Context context, Match match) {
        Intent intent = new Intent(context, AtlasPhotoActivity.class);
        intent.putExtra(Constant.K_MATCH_ID, match.getMatch_id());
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Match match = matchList.get(position);
        String imgUrl = ConfigUtils.getImageBaseUrl() + match.getLogopic();
        loadPicture(imgUrl, holder.logopic);
        holder.name.setText(match.getMatch_name());
    }

    private void loadPicture(String imgUrl, ImageView imageView) {
        Context context = SimpleApplication.getInstance();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_atlas, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }


}
