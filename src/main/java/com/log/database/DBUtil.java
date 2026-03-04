package com.log.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:database/BatchLog.db";

    public static Connection getConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(URL);
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON");

        System.out.println("Foreign keys enabled");

        stmt.close();
        return conn;

    }
}