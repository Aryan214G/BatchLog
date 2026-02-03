package com.log.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL);
    }
}