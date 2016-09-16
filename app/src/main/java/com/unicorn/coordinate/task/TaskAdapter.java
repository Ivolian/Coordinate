package com.unicorn.coordinate.task;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;
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


    // ================================== refreshTask ==================================

    public void refreshTask() {
        pointList = PointHelper.getDisplayPointList();
        notifyDataSetChanged();
    }


    // ================================== ViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.arrow)
        IconicsImageView arrow;

        @BindView(R.id.small)
        ImageView small;

        @BindView(R.id.big)
        ImageView big;

        @BindView(R.id.bottom)
        ImageView bottom;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelper.isSafe()) {
                if (getAdapterPosition() <= PointHelper.getCurrentPosition()) {
                    return;
                }
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

        Context context = holder.name.getContext();
        int colorRes = (position <= PointHelper.getCurrentPosition() ? R.color.md_black : R.color.md_grey_400);
        int color = ContextCompat.getColor(context, colorRes);
        holder.name.setTextColor(color);
//        holder.arrow.setColor(color);

        if (point.getPointtype() == PointType.LAST_POINT) {
            holder.small.setVisibility(View.VISIBLE);
            holder.big.setVisibility(View.INVISIBLE);
            holder.bottom.setVisibility(View.INVISIBLE);
            holder.small.setImageResource(position == PointHelper.getCurrentPosition() ? R.drawable.star : R.drawable.blue_p);
        } else {
            holder.small.setVisibility(View.INVISIBLE);
            holder.big.setVisibility(View.VISIBLE);
            holder.bottom.setVisibility(View.VISIBLE);
        }

        if (position <= PointHelper.getCurrentPosition()) {
            holder.arrow.setVisibility(View.INVISIBLE);
        } else {
            holder.arrow.setVisibility(View.VISIBLE);
        }
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
