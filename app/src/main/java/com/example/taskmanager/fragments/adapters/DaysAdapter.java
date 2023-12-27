package com.example.taskmanager.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private final List<String> days;
    private final LayoutInflater inflater;
    private final View.OnClickListener clickListener;
    private int selectedDayPosition = -1;

    public DaysAdapter(Context context, List<String> days, View.OnClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.days = days;
        this.clickListener = clickListener;
    }

    public void setSelectedDay(int position) {
        int previousSelectedPosition = selectedDayPosition;
        selectedDayPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedDayPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = days.get(position);

        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = originalFormat.parse(day);

            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            String dayOfWeek = dayOfWeekFormat.format(date);

            SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            String dayOfMonth = dayOfMonthFormat.format(date);

            holder.textViewDayOfWeek.setText(dayOfWeek);
            holder.textViewDate.setText(dayOfMonth);

            holder.itemView.setSelected(position == selectedDayPosition);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewDayOfWeek;
        TextView textViewDate;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            setSelectedDay(getAdapterPosition());
            clickListener.onClick(view);
        }
    }
}
