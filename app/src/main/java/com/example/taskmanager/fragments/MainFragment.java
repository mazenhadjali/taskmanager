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
import com.example.taskmanager.core.entities.Category;
import com.example.taskmanager.fragments.adapters.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recyclerCategories;
    private CategoriesAdapter categoriesAdapter;

    private DataBaseHelper databaseHelper;

    private List<Category> categoryList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DataBaseHelper(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerCategories = view.findViewById(R.id.recycler_categories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesAdapter = new CategoriesAdapter(getContext(), categoryList);
        recyclerCategories.setAdapter(categoriesAdapter);
        return view;
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
    }
}
