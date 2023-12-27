package com.example.taskmanager.core.entities;

import java.sql.*;

public class Task {
    private int taskID;
    private int userID;
    private int categoryID;
    private String title;
    private String description;
    private Time startTime;
    private Time endTime;
    private String State;

    public Task() {
    }

    public Task(int taskID, int userID, int categoryID, String title, String description, Time startTime, Time endTime, String state) {
        this.taskID = taskID;
        this.userID = userID;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        State = state;
    }

    public Task(int userID, int categoryID, String title, String description, Time startTime, Time endTime, String state) {
        this.userID = userID;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        State = state;
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

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
