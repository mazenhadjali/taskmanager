package com.example.taskmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;
import com.example.taskmanager.core.entities.Task;
import com.example.taskmanager.fragments.adapters.DaysAdapter;
import com.example.taskmanager.fragments.adapters.TaskAdapter;

import java.util.List;

public class CalendarFragment extends Fragment {

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    private RecyclerView recyclerViewDays;
    private RecyclerView recyclerViewTasks;

    private DaysAdapter daysAdapter;

    private DataBaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        databaseHelper = new DataBaseHelper(this.getActivity());

        recyclerViewDays = view.findViewById(R.id.recyclerViewDays);
        LinearLayoutManager layoutManagerDays = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDays.setLayoutManager(layoutManagerDays);

        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);
        LinearLayoutManager layoutManagerTasks = new LinearLayoutManager(getContext());
        recyclerViewTasks.setLayoutManager(layoutManagerTasks);

        List<String> days = getYourDaysList();
        if (days != null && !days.isEmpty()) {
            View.OnClickListener clickListener = v -> {
                int itemPosition = recyclerViewDays.getChildLayoutPosition(v);
                String selectedDay = days.get(itemPosition);
                List<Task> tasksForThatDay = getTasksByDay(selectedDay);
                Log.d("CalendarFragment", "Day clicked: " + selectedDay); // Debug log
                TaskAdapter taskAdapter = (TaskAdapter) recyclerViewTasks.getAdapter();

                Log.d("CalendarFragment", "tasksForThatDay size: " + tasksForThatDay.size()); // Debug log

                if (taskAdapter != null) {
                    taskAdapter.updateData(tasksForThatDay);
                    taskAdapter.notifyDataSetChanged();
                    Log.d("CalendarFragment", "Adapter updated"); // Debug log
                } else {
                    taskAdapter = new TaskAdapter(getContext(), tasksForThatDay);
                    recyclerViewTasks.setAdapter(taskAdapter);
                    Log.d("CalendarFragment", "New adapter set"); // Debug log
                }
                daysAdapter.setSelectedDay(itemPosition);
            };

            daysAdapter = new DaysAdapter(getContext(), days, clickListener);
            recyclerViewDays.setAdapter(daysAdapter);

            if (!days.isEmpty()) {
                List<Task> tasksForThatDay = getTasksByDay(days.get(0));
                TaskAdapter taskAdapter = new TaskAdapter(getContext(), tasksForThatDay);
                recyclerViewTasks.setAdapter(taskAdapter); // Set your Task adapter here initially
            }
        } else {
            // Handle the case when days list is empty
        }

        return view;
    }

    private List<String> getYourDaysList() {
        return databaseHelper.getDistinctDays();
    }

    public List<Task> getTasksByDay(String day) {
        List<Task> tasks = databaseHelper.getTasksByDay(day);
        if (tasks != null) {
            Log.i("CalendarFragment", "Tasks for day " + day + ": " + tasks.size());
        }
        return tasks;
    }

}
