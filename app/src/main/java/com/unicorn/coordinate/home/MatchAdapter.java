package com.unicorn.coordinate.home;

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
import com.unicorn.coordinate.helper.GlideRoundTransform;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Match> matchList = new ArrayList<>();

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture)
        ImageView picture;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.status)
        TextView status;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                Context context = picture.getContext();
                Match match = matchList.get(getAdapterPosition());
                startMatchDetailActivity(context, match);
            }
        }
    }

    private void startMatchDetailActivity(Context context, Match match) {
        Intent intent = new Intent(context, MatchDetailActivity.class);
        intent.putExtra(Constant.K_MATCH, match);
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Match match = matchList.get(position);
        String imgUrl = ConfigUtils.getImageBaseUrl() + match.getPic2();
        loadPicture(imgUrl, holder.picture);
        holder.name.setText(match.getMatch_name());
        holder.date.setText(match.getDate());
        holder.status.setText(match.getStatus());
    }

    private void loadPicture(String imgUrl, ImageView imageView) {
        Context context = SimpleApplication.getInstance();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .transform(new GlideRoundTransform(SimpleApplication.getInstance(), 8))
                .into(imageView);
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }


}
