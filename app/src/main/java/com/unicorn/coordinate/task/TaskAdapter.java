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
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.utils.AESUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Point> pointList = PointHelper.getDisplayPointList();

    public void refreshTask() {
        pointList = PointHelper.getDisplayPointList();
        notifyDataSetChanged();
    }

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
                Context context = name.getContext();
                Point point = pointList.get(getAdapterPosition());
                String content = AESUtils.decrypt(point.getContent());
                startTaskDetailActivity(context, content);
            }
        }
    }

    private void startTaskDetailActivity(Context context, final String content) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(Constant.K_CONTENT, content);
        context.startActivity(intent);
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Point point = pointList.get(position);
        String name = AESUtils.decrypt(point.getPointname());
        holder.name.setText(name);
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
