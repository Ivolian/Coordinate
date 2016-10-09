package com.unicorn.coordinate.profile.matchCert;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.profile.model.UserMatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MatchCertAdapter extends RecyclerView.Adapter<MatchCertAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<UserMatch> userMatchList = new ArrayList<>();

    public void setUserMatchList(List<UserMatch> userMatchList) {
        this.userMatchList = userMatchList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.matchName)
        TextView matchName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                Context context = matchName.getContext();
                UserMatch userMatch = userMatchList.get(getAdapterPosition());
                startMatchCertDetailActivity(context, userMatch);
            }
        }
    }

    private void startMatchCertDetailActivity(Context context, UserMatch userMatch) {
        Intent intent = new Intent(context, MatchCertDetailActivity.class);
        intent.putExtra(Constant.K_TITLE, "赛事证书");
        intent.putExtra(Constant.K_MATCH_ID, userMatch.getMatch_id());
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UserMatch userMatch = userMatchList.get(position);
        holder.matchName.setText(userMatch.getMatch_name());
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match_cert, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return userMatchList.size();
    }


}
