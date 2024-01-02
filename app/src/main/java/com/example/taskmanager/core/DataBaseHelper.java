package com.example.taskmanager.core;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.taskmanager.core.entities.Category;
import com.example.taskmanager.core.entities.Task;
import com.example.taskmanager.core.entities.User;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DataBaseHelper extends SQLiteOpenHelper implements DatabaseOperations {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TODO_DATABASE4";
    private static final String TABLE_NAME = "TODO_TABLE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DataBaseHelper", "Started executing queries to create tables");
        try {
            db.beginTransaction(); // Start the transaction
            db.execSQL(SQLQueries.CREATE_TABLE_USERS);
            db.execSQL(SQLQueries.CREATE_TABLE_CATEGORIES);
            db.execSQL(SQLQueries.CREATE_TABLE_TASKS);

            addDefaultUser(db);
            addDefaultCategories(db);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void addDefaultCategories(SQLiteDatabase db) {
        String[] defaultCategories = {"Study", "Home", "Work"};
        for (String category : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put("CategoryName", category);
            db.insert("Categories", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void addDefaultUser(SQLiteDatabase db) {
        User user = new User("Mazen", "Hadj Ali", "hadjalimazen@gmail.com", "hadjalimazen", "azerty");
        long id = addUser(db, user);
        if (id != -1)
            user.setUserID((int) id);
        Log.i("result", id + "");
    }


    @Override
    public long addUser(SQLiteDatabase db, User user) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        long result = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("FirstName", user.getFirstName());
            values.put("LastName", user.getLastName());
            values.put("Email", user.getEmail());
            values.put("Username", user.getUsername());
            values.put("Password", user.getPassword());

            result = db.insert("Users", null, values);

            Log.i("result", result + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changeUserPassword(String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        try {
            ContentValues values = new ContentValues();
            values.put("Password", newPassword);

            int rowsAffected = db.update("Users", values, "Username = 'hadjalimazen'", null);

            if (rowsAffected > 0) {
                result = true;
                Log.i("Update Result", "Password updated successfully for user: " + " hadjalimazen");
            } else {
                Log.i("Update Result", "No user records were updated. User may not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean isUserExists(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        Log.i("username", username);
        Log.i("password", password);

        try {
            String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
            cursor = db.rawQuery(query, new String[]{username, password});
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public long addCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CategoryName", categoryName);
        // Insert category data into the "Categories" table
        long result = db.insert("Categories", null, values);
        Log.i("Category Data Insertion", String.valueOf(result));
        return result;
    }

    @Override
    public long addTask(int userId, int categoryId, String title, String description, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userId);
        values.put("CategoryID", categoryId);
        values.put("Title", title);
        values.put("Description", description);
        values.put("StartTime", startTime);
        values.put("EndTime", endTime);
        long result = db.insert("Tasks", null, values);
        Log.i("Task Data Insertion", String.valueOf(result));
        return result;
    }

    @Override
    public Cursor getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("Categories", new String[]{"CategoryID", "CategoryName"}, null, null, null, null, null);
    }

    public int getCategoryIDByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Categories", new String[]{"CategoryID"}, "CategoryName = ?", new String[]{categoryName}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("CategoryID"));
            cursor.close();
            return id;
        } else {
            cursor.close();
            return -1;
        }
    }

    public List<String> getDistinctDays() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> distinctDays = new ArrayList();
        String query = SQLQueries.GET_DISTINCT_DAYS;

        Cursor cursor = db.rawQuery(query, new String[]{});

        int dayColumnIndex = cursor.getColumnIndex("Day");

        if (dayColumnIndex != -1 && cursor.moveToFirst()) {
            do {
                distinctDays.add(cursor.getString(dayColumnIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return distinctDays;
    }

    @SuppressLint("Range")
    public List<Task> getTasksByDay(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> tasksByDay = new ArrayList<>();
        String query = SQLQueries.GET_TASKS_BY_DAY;

        Cursor cursor = db.rawQuery(query, new String[]{day, day});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskID(cursor.getInt(cursor.getColumnIndex("TaskID")));
                task.setUserID(cursor.getInt(cursor.getColumnIndex("UserID")));
                task.setCategoryID(cursor.getInt(cursor.getColumnIndex("CategoryID")));
                task.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                task.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                String startTimeStr = cursor.getString(cursor.getColumnIndex("StartTime"));
                String endTimeStr = cursor.getString(cursor.getColumnIndex("EndTime"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                if (startTimeStr != null && !startTimeStr.trim().isEmpty()) {
                    try {
                        java.util.Date startTime = sdf.parse(startTimeStr);
                        task.setStartTime(new Time(startTime.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (endTimeStr != null && !endTimeStr.trim().isEmpty()) {
                    try {
                        java.util.Date endTime = sdf.parse(endTimeStr);
                        task.setEndTime(new Time(endTime.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                task.setState(cursor.getString(cursor.getColumnIndex("State")));
                tasksByDay.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasksByDay;
    }

    public boolean setTaskCompleted(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("State", "Completed");
        int numberOfRowsAffected = db.update("Tasks", values, "TaskID = ?", new String[]{String.valueOf(taskId)});
        Log.i("update database", "taskId" + taskId + " set completed");
        return numberOfRowsAffected > 0;
    }

    public boolean setTaskPending(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("State", "Pending");
        int numberOfRowsAffected = db.update("Tasks", values, "TaskID = ?", new String[]{String.valueOf(taskId)});
        Log.i("update database", "taskId" + taskId + " set pending");
        return numberOfRowsAffected > 0;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("Categories", null, null, null, null, null, "CategoryID ASC");

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("CategoryID"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("CategoryName"));
                    categories.add(new Category(id, name)); // Assuming a Category constructor that takes an id and name
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return categories;
    }

    @SuppressLint("Range")
    public int countTotalTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int totalCount = 0;
        try {
            cursor = db.rawQuery(SQLQueries.COUNT_TOTAL_TASKS, null);
            if (cursor != null && cursor.moveToFirst()) {
                totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalCount;
    }

    // Method to count the total number of completed tasks
    @SuppressLint("Range")
    public int countCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int completedCount = 0;
        try {
            cursor = db.rawQuery(SQLQueries.COUNT_COMPLETED_TASKS, null);
            if (cursor != null && cursor.moveToFirst()) {
                completedCount = cursor.getInt(cursor.getColumnIndex("completed_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return completedCount;
    }

    // Method to count the total number of pending tasks
    @SuppressLint("Range")
    public int countPendingTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int pendingCount = 0;
        try {
            cursor = db.rawQuery(SQLQueries.COUNT_PENDING_TASKS, null);
            if (cursor != null && cursor.moveToFirst()) {
                pendingCount = cursor.getInt(cursor.getColumnIndex("pending_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return pendingCount;
    }

    @SuppressLint("Range")
    public List<Task> getTasksByCategoryId(int categoryId) {
        List<Task> tasksByCategoryId = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] projection = {
                    "TaskID",
                    "UserID",
                    "CategoryID",
                    "Title",
                    "Description",
                    "StartTime",
                    "EndTime",
                    "State"
            };
            String selection = "CategoryID = ?";
            String[] selectionArgs = {String.valueOf(categoryId)};

            cursor = db.query(
                    "Tasks",   // The table to query
                    projection,            // The columns to return
                    selection,             // The columns for the WHERE clause
                    selectionArgs,         // The values for the WHERE clause
                    null,         // don't group the rows
                    null,          // don't filter by row groups
                    null           // The sort order
            );

            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setTaskID(cursor.getInt(cursor.getColumnIndex("TaskID")));
                    task.setUserID(cursor.getInt(cursor.getColumnIndex("UserID")));
                    task.setCategoryID(cursor.getInt(cursor.getColumnIndex("CategoryID")));
                    task.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    task.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                    String startTimeStr = cursor.getString(cursor.getColumnIndex("StartTime"));
                    String endTimeStr = cursor.getString(cursor.getColumnIndex("EndTime"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                    if (startTimeStr != null && !startTimeStr.trim().isEmpty()) {
                        try {
                            java.util.Date startTime = sdf.parse(startTimeStr);
                            task.setStartTime(new Time(startTime.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (endTimeStr != null && !endTimeStr.trim().isEmpty()) {
                        try {
                            java.util.Date endTime = sdf.parse(endTimeStr);
                            task.setEndTime(new Time(endTime.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    task.setState(cursor.getString(cursor.getColumnIndex("State")));
                    tasksByCategoryId.add(task);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("DataBaseHelper", "Error fetching tasks by category", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return tasksByCategoryId;
    }

    public String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String categoryName = null;

        try {
            String[] projection = new String[]{"CategoryName"};
            String selection = "CategoryID = ?";
            String[] selectionArgs = new String[]{String.valueOf(categoryId)};

            cursor = db.query(
                    "Categories",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("CategoryName");
                if (nameIndex != -1) {
                    categoryName = cursor.getString(nameIndex);
                }
            }
        } catch (SQLException e) {
            Log.e("DataBaseHelper", "Error fetching category name", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return categoryName;
    }
}
