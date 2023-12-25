package com.example.taskmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;
import com.example.taskmanager.fragments.adapters.DaysAdapter;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    private RecyclerView recyclerViewDays;
    private DaysAdapter daysAdapter;

    private DataBaseHelper databaseHelper;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DataBaseHelper(this.getActivity());

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize the RecyclerView and set its adapter
        recyclerViewDays = view.findViewById(R.id.recyclerViewDays);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDays.setLayoutManager(layoutManager);

        // This is a placeholder for your list of day strings. You need to fill it with actual data.
        List<String> days = getYourDaysList(); // Replace with actual data retrieval method

        // In case you want to handle click events
        View.OnClickListener clickListener = v -> {
            int itemPosition = recyclerViewDays.getChildLayoutPosition(v);
            // Do something with the clicked item
        };

        daysAdapter = new DaysAdapter(getContext(), days, clickListener);
        recyclerViewDays.setAdapter(daysAdapter);

        return view;
    }

    // Replace the placeholder method with an actual data retrieval method
    private List<String> getYourDaysList() {
        // This should return a list of day strings obtained from the database
        return databaseHelper.getDistinctDays();
    }


}
