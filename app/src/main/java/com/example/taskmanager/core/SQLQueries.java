package com.example.taskmanager.core;

public class SQLQueries {

    public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS Users ("
            + "UserID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "FirstName TEXT,"
            + "LastName TEXT,"
            + "Email TEXT,"
            + "Username TEXT UNIQUE,"
            + "Password TEXT"
            + ");";

    public static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS Categories ("
            + "CategoryID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "CategoryName TEXT UNIQUE"
            + ");";

    public static final String CREATE_TABLE_TASKS = "CREATE TABLE IF NOT EXISTS Tasks ("
            + "TaskID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "UserID INTEGER,"
            + "CategoryID INTEGER,"
            + "Title TEXT,"
            + "Description TEXT,"
            + "StartTime TIME,"
            + "EndTime TIME,"
            + "State TEXT DEFAULT 'Pending' CHECK( State IN ('Pending','InProgress','Completed','Archived') ),"
            + "FOREIGN KEY (UserID) REFERENCES Users(UserID),"
            + "FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)"
            + ");";

    public static final String GET_DISTINCT_DAYS = "SELECT DISTINCT DATE(StartTime) AS Day FROM Tasks " +
            "UNION " +
            "SELECT DISTINCT DATE(EndTime) FROM Tasks;";

}
