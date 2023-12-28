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
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;
import com.example.taskmanager.core.entities.Task;
import com.example.taskmanager.fragments.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class tasksbycategory extends Fragment {

    private DataBaseHelper dataBaseHelper;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TextView tvCategoryTitle; // Add this TextView variable

    public static tasksbycategory newInstance(int categoryId) {
        tasksbycategory fragment = new tasksbycategory();
        Bundle args = new Bundle();
        args.putInt("CATEGORY_ID", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBaseHelper = new DataBaseHelper(getActivity());
        // We'll initialize the TextView in onViewCreated
        return inflater.inflate(R.layout.fragment_tasksbycategory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvTasksByCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);

        int categoryId = -1;
        if (getArguments() != null) {
            categoryId = getArguments().getInt("CATEGORY_ID");
        }

        if (categoryId != -1) {
            List<Task> tasks = dataBaseHelper.getTasksByCategoryId(categoryId);
            adapter.updateData(tasks);

            String categoryName = dataBaseHelper.getCategoryNameById(categoryId);
            tvCategoryTitle.setText(categoryName);
        } else {
            tvCategoryTitle.setText("error getting category");
        }
    }


}
