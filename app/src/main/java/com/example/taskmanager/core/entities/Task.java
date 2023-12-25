package com.example.taskmanager.core.entities;

import java.sql.*;

public class Task {
    private int taskID;
    private int userID;
    private int categoryID;
    private String title;
    private String description;
    private TaskType taskType;
    private Date dueDate;
    private Time startTime;
    private Time endTime;
    private String dayOfWeek;
    private boolean completed;

    public Task() {}

    public Task(int taskID, int userID, int categoryID, String title, String description, TaskType taskType,
                Date dueDate, Time startTime, Time endTime, String dayOfWeek, boolean completed) {
        this.taskID = taskID;
        this.userID = userID;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.completed = completed;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
