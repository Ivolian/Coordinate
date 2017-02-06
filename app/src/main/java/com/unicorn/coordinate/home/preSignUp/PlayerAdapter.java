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
//
//        @BindView(R.id.date)
//        TextView date;
//
//        @BindView(R.id.status)
//        TextView status;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Match match = playerList.get(position);
//        String imgUrl = ConfigUtils.getImageBaseUrl() + match.getPic1();
//        loadPicture(imgUrl, holder.picture);
//        holder.name.setText(match.getMatch_name());
//        holder.date.setText(match.getDate());
//        holder.status.setText(match.getStatus());
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }


}
