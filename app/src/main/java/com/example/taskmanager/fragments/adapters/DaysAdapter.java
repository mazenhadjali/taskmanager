package com.example.taskmanager.fragments.adapters;

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
    // Define a listener
    private final View.OnClickListener clickListener;

    public DaysAdapter(Context context, List<String> days, View.OnClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.days = days;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_day, parent, false);
        view.setOnClickListener(clickListener); // Set the listener to the view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = days.get(position);

        try {
            // Assuming your day string is in a "YYYY-MM-DD" format, e.g., "2020-01-01"
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = originalFormat.parse(day);

            // Format the date to get the day of the week e.g. "Mon"
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            String dayOfWeek = dayOfWeekFormat.format(date);

            // Format the date to get the date in "01 Jan" format
            SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            String dayOfMonth = dayOfMonthFormat.format(date);

            holder.textViewDayOfWeek.setText(dayOfWeek);
            holder.textViewDate.setText(dayOfMonth);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDayOfWeek;
        TextView textViewDate;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
