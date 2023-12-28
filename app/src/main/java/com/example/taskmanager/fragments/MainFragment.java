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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;
import com.example.taskmanager.core.entities.Category;
import com.example.taskmanager.fragments.adapters.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements CategoriesAdapter.OnCategoryClickListener {

    private RecyclerView recyclerCategories;
    private CategoriesAdapter categoriesAdapter;

    private DataBaseHelper databaseHelper;

    private List<Category> categoryList;

    private TextView textpendingtasks;
    private ProgressBar progressCompletedTasks;
    private TextView textTotalTasks;
    private TextView textCompletedTasks;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DataBaseHelper(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        textpendingtasks = view.findViewById(R.id.text_pending_tasks);
        progressCompletedTasks = view.findViewById(R.id.progress_completed_tasks);
        textTotalTasks = view.findViewById(R.id.text_total_tasks);
        textCompletedTasks = view.findViewById(R.id.text_completed_tasks);

        recyclerCategories = view.findViewById(R.id.recycler_categories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesAdapter = new CategoriesAdapter(getContext(), categoryList, this);
        recyclerCategories.setAdapter(categoriesAdapter);
        return view;
    }

    @Override
    public void onCategoryClick(Category category) {
        tasksbycategory fragment = tasksbycategory.newInstance(category.getCategoryID());
        // Perform fragment transaction to replace with tasksbycategory fragment
        // Consider passing the category ID to the newInstance method for the tasksbycategory fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadTaskCounts() {
        int totalTasks = databaseHelper.countTotalTasks();
        int completedTasks = databaseHelper.countCompletedTasks();
        int pendingTasks = databaseHelper.countPendingTasks(); // Assuming no other states for simplicity
        textpendingtasks.setText("Pending Tasks: " + pendingTasks);
        textTotalTasks.setText("Total Tasks: " + totalTasks);
        textCompletedTasks.setText("Completed Tasks: " + completedTasks + " / " + totalTasks);

        progressCompletedTasks.setMax(totalTasks);
        progressCompletedTasks.setProgress(completedTasks);


    }

    private void loadCategories() {
        categoryList = databaseHelper.getAllCategories();
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        categoriesAdapter.updateData(categoryList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCategories();
        loadTaskCounts(); // Update task counts and progress bars

    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
        loadTaskCounts(); // Assuming this is the method that updates tasks information
    }
}
