package com.unicorn.coordinate.profile;

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
import com.unicorn.coordinate.message.MessageDetailActivity;
import com.unicorn.coordinate.message.model.Message;
import com.unicorn.coordinate.profile.model.UserMatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserMatchAdapter extends RecyclerView.Adapter<UserMatchAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<UserMatch> userMatchList = new ArrayList<>();

    public void setUserMatchList(List<UserMatch> userMatchList) {
        this.userMatchList = userMatchList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.matchName)
        TextView matchName;

        @BindView(R.id.teamName)
        TextView teamName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
//                Context context = content.getContext();
//                Message message = userMatchList.get(getAdapterPosition());
//                startMessageDetailActivity(context, message);
            }
        }
    }

    private void startMessageDetailActivity(Context context, Message message) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra(Constant.K_MESSAGE, message);
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UserMatch userMatch = userMatchList.get(position);
        holder.matchName.setText(userMatch.getMatch_name());
        holder.teamName.setText("队伍名称: " + userMatch.getTeamname());
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_match, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return userMatchList.size();
    }


}
