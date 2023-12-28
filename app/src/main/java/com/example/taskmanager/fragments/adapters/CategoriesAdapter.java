package com.example.taskmanager.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.core.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> categoryList;
    private LayoutInflater mInflater;

    public CategoriesAdapter(Context context, List<Category> data, OnCategoryClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.categoryList = (data != null) ? new ArrayList<>(data) : new ArrayList<>();
        this.onCategoryClickListener = listener;
    }
    // Define an interface for callback
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private OnCategoryClickListener onCategoryClickListener;


    public void updateData(List<Category> newData) {
        categoryList.clear();
        categoryList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onCategoryClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                Category clickedCategory = categoryList.get(getAdapterPosition());
                onCategoryClickListener.onCategoryClick(clickedCategory);
            }
        }
    }
}
