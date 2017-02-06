package com.unicorn.coordinate.home.preSignUp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.coordinate.R;
import com.unicorn.coordinate.home.model.Player;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Player> playerList = new ArrayList<>();

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.isLeader)
        TextView isLeader;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.status)
        TextView status;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Player player = playerList.get(position);
        holder.name.setText(player.getNickname());
        holder.isLeader.setText(player.getLeader() == 1 ? "领队" : "队员");
        holder.status.setText(playerStatusText(player));
    }


    private String playerStatusText(Player player) {
        switch (player.getStatus()) {
            case "0":
                return "已邀请";
            case "1":
                return "已确认";
            case "2":
                return "已拒绝";
            default:
                return "";
        }
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_player, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }


}
