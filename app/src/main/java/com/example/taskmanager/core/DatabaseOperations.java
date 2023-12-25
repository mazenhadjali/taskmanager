package com.example.taskmanager.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.core.entities.Category;
import com.example.taskmanager.core.entities.Task;
import com.example.taskmanager.core.entities.User;

public interface DatabaseOperations {

    long addUser(SQLiteDatabase db, User user);

    boolean isUserExists(String username, String password);

    long addCategory(String categoryName);

    long addTask(int userId, int categoryId, String title, String description, String startTime, String endTime);

    Cursor getCategories();
}
