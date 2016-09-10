package com.unicorn.coordinate.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.home.model.Match;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.GlideUtils;

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
        RoundedImageView picture;

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
                //  TODO
            }
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Match match = matchList.get(position);
        String imgUrl = ConfigUtils.getImageBaseUrl() + match.getPic1();
        GlideUtils.loadPicture(imgUrl, holder.picture);
        holder.name.setText(match.getMatch_name());
        String date = match.getDate4().substring(0, 10);
        holder.date.setText(date);
        holder.status.setText(match.getStatus());
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
