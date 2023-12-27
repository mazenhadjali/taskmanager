package com.example.taskmanager.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;
import com.example.taskmanager.core.entities.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private LayoutInflater mInflater;

    DataBaseHelper dataBaseHelper;

    public TaskAdapter(Context context, List<Task> data) {
        this.mInflater = LayoutInflater.from(context);
        this.taskList = data;
        dataBaseHelper = new DataBaseHelper(context);
    }

    public void updateData(List<Task> newData) {
        taskList.clear();
        taskList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.btnCheck.setChecked(task.getState().equals("Completed"));
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());

        holder.btnCheck.setOnClickListener(v -> {
            if (task.getState().equals("Completed")) {
                dataBaseHelper.setTaskPending(task.getTaskID());
                task.setState("Pending");
            } else {
                dataBaseHelper.setTaskCompleted(task.getTaskID());
                task.setState("Completed");
            }
            notifyItemChanged(position);
        });

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        if (task.getStartTime() != null) {
            String startTimeStr = timeFormat.format(task.getStartTime());
            holder.tvStartTime.setText(startTimeStr);
        } else {
            holder.tvStartTime.setText("");
        }
        if (task.getEndTime() != null) {
            String endTimeStr = timeFormat.format(task.getEndTime());
            holder.tvEndTime.setText(endTimeStr);
        } else {
            holder.tvEndTime.setText("");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskDescription;
        TextView tvStartTime;
        TextView tvEndTime;
        CheckBox btnCheck;

        ViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            btnCheck = itemView.findViewById(R.id.btnCheck);
        }
    }
}
