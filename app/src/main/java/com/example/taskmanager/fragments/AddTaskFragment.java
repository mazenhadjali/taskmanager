package com.example.taskmanager.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    private DataBaseHelper databaseHelper;
    private RadioGroup radioGroup;
    private LinearLayout taskDetailsLayout;
    private LinearLayout categoryDetailsLayout;
    private EditText startDateEditText, endDateEditText;
    private Spinner spinnerTaskCategory;
    private Button submitButton;

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DataBaseHelper(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        spinnerTaskCategory = view.findViewById(R.id.spinnercategoryselect);
        populateSpinnerWithCategories();
        submitButton = view.findViewById(R.id.btn_submit_task);
        radioGroup = view.findViewById(R.id.rg_add_options);
        taskDetailsLayout = view.findViewById(R.id.ll_task_details);
        categoryDetailsLayout = view.findViewById(R.id.ll_category_details);

        startDateEditText = view.findViewById(R.id.et_start_date_time);
        endDateEditText = view.findViewById(R.id.et_end_date_time);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String startDateString = dateFormat.format(calendar.getTime());
        startDateEditText.setText(startDateString);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String endDateString = dateFormat.format(calendar.getTime());
        endDateEditText.setText(endDateString);

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(v);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(v);
            }
        });

        if (radioGroup.getCheckedRadioButtonId() == R.id.rb_insert_task) {
            taskDetailsLayout.setVisibility(View.VISIBLE);
            categoryDetailsLayout.setVisibility(View.GONE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_insert_category) {
            taskDetailsLayout.setVisibility(View.GONE);
            categoryDetailsLayout.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_insert_task) {
                    populateSpinnerWithCategories();
                    taskDetailsLayout.setVisibility(View.VISIBLE);
                    categoryDetailsLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_insert_category) {
                    taskDetailsLayout.setVisibility(View.GONE);
                    categoryDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });

        return view;
    }

    private void populateSpinnerWithCategories() {
        Cursor cursor = databaseHelper.getCategories();
        ArrayList<String> categories = new ArrayList<>();
        int columnNameIndex = cursor.getColumnIndex("CategoryName");
        if (columnNameIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    categories.add(cursor.getString(columnNameIndex));
                } while (cursor.moveToNext());
            }
        } else {
            Toast.makeText(getActivity(), "The 'CategoryName' column was not found.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskCategory.setAdapter(dataAdapter);
    }

    public void showDateTimePicker(final View v) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getActivity(), (timeView, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                String dateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date.getTime());
                ((EditText) v).setText(dateTimeString);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void handleFormSubmission() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.rb_insert_category) {
            String categoryName = ((EditText) getView().findViewById(R.id.et_category_name)).getText().toString().trim();
            if (!categoryName.isEmpty()) {
                categoryName = Character.toUpperCase(categoryName.charAt(0)) + categoryName.substring(1);
                databaseHelper.addCategory(categoryName);
                Toast.makeText(this.getActivity(), "Category inserted successfully", Toast.LENGTH_SHORT).show();
                ((EditText) getView().findViewById(R.id.et_category_name)).setText("");
            } else {
                Toast.makeText(getActivity(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            String title = ((EditText) getView().findViewById(R.id.et_task_title)).getText().toString().trim();
            String description = ((EditText) getView().findViewById(R.id.et_task_description)).getText().toString().trim();
            int categoryId = databaseHelper.getCategoryIDByName(spinnerTaskCategory.getSelectedItem().toString());
            String startTime = startDateEditText.getText().toString().trim();
            String endTime = endDateEditText.getText().toString().trim();


            if (!title.isEmpty() && !description.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
                databaseHelper.addTask(1, categoryId, title, description, startTime, endTime);
                ((EditText) getView().findViewById(R.id.et_task_title)).setText("");
                ((EditText) getView().findViewById(R.id.et_task_description)).setText("");
                startDateEditText.setText("");
                endDateEditText.setText("");
                Toast.makeText(this.getActivity(), "Task inserted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "All fields must be filled for Task", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
