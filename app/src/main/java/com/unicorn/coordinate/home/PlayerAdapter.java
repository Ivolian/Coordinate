package com.unicorn.coordinate.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.RefreshPlayerEvent;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    public boolean couldOperate = false;

    // ================================== data ==================================

    private List<Player> playerList = new ArrayList<>();

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.isLeader)
        TextView isLeader;

        @BindView(R.id.mobile)
        TextView mobile;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.operation)
        TextView operation;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            initOperation(operation);
        }

        private String mobileStr;

        @OnClick(R.id.operation)
        public void operationOnClick(View view) {
            if (ClickHelper.isSafe()) {
                Player player = playerList.get(getAdapterPosition());
                showListDialog(view.getContext(), player);
            }
        }
    }

    private void showListDialog(final Context context, final Player player) {
        new MaterialDialog.Builder(context)
                .items(Arrays.asList("设为队长", "替换队员"))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            changeLeader(player);
                        }
                        if (which == 1) {
                            showPlayerDialog(context, player);
                        }
                    }
                })
                .show();
    }

    private void changeLeader(Player player) {
        String url = changeLeaderUrl(player);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (ResponseHelper.isWrong(response)) {
                                return;
                            }
                            ToastUtils.show("替换队长成功");
                            EventBus.getDefault().post(new RefreshPlayerEvent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private String changeLeaderUrl(Player player) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/releader?").buildUpon();
        builder.appendQueryParameter("userid", ConfigUtils.getUserId());
        builder.appendQueryParameter("matchuserid", player.getMatchuserid());
        return builder.toString();
    }


    private String mobileStr;

    private void showPlayerDialog(Context context, final Player player) {
        new MaterialDialog.Builder(context)
                .title("替换队员")
                .inputType(InputType.TYPE_CLASS_PHONE)
                .inputRange(11, 11)
                .input("输入手机号码", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        mobileStr = input.toString();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        changePlayer(player, mobileStr);
                    }
                })
                .show();
    }

    private void changePlayer(Player player, String mobile) {
        String url = changePlayerUrl(player, mobile);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (ResponseHelper.isWrong(response)) {
                                return;
                            }
                            ToastUtils.show("替换队员成功");
                            EventBus.getDefault().post(new RefreshPlayerEvent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private String changePlayerUrl(Player player, String mobile) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/changeplayer?").buildUpon();
        builder.appendQueryParameter("userid", ConfigUtils.getUserId());
        builder.appendQueryParameter("mobile", mobile);
        builder.appendQueryParameter("matchuserid", player.getMatchuserid());
        return builder.toString();
    }

    private void initOperation(TextView change) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(change.getContext(), 3));
        change.setBackgroundDrawable(gradientDrawable);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Player player = playerList.get(position);
        holder.name.setText(AESUtils.decrypt2(player.getNickname()));
        holder.mobile.setText(player.getMobile());
        holder.isLeader.setText(player.getLeader() == 1 ? "队长" : "队员");
        holder.status.setText(playerStatusText(player));

        boolean operationVisible = couldOperate && player.getLeader() != 1;
        holder.operation.setVisibility(operationVisible ? View.VISIBLE : View.GONE);
    }


    private String playerStatusText(Player player) {
        switch (player.getStatus()) {
            case "9":
                return "已拒绝";
            case "1":
                return "已确认";
            case "2":
                return "已邀请";
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
