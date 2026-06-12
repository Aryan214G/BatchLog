package com.log.database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final Path DB_PATH = Paths.get("database", "BatchLog_test.db");
    private static final String URL = "jdbc:sqlite:" + DB_PATH.toAbsolutePath();

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    public static Path getDatabasePath() {
        return DB_PATH;
    }

}