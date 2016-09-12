package com.unicorn.coordinate.message;

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
import com.unicorn.coordinate.message.model.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Message> messageList = new ArrayList<>();

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.date)
        TextView date;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                Context context = content.getContext();
                Message message = messageList.get(getAdapterPosition());
                startMessageDetailActivity(context, message);
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
        Message message = messageList.get(position);
        holder.content.setText(message.getContext());
        holder.date.setText(message.getCreatetime());
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


}
