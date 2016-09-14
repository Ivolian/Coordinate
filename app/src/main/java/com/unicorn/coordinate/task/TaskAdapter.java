package com.unicorn.coordinate.task;

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
import com.unicorn.coordinate.task.model.Point;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Point> pointList = PointHelper.getPointList();


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
//
//        @BindView(R.id.date)
//        TextView date;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
//                Context context = content.getContext();
//                Message message = pointList.get(getAdapterPosition());
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
        Point point = pointList.get(position);
        holder.name.setText(point.getPointname());
    }


    // ================================== 基本无视 ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }


}
