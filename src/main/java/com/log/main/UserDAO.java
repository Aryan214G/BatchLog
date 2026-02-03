package com.log.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT UNIQUE
                );
                """;

        try(Connection conn = DBUtil.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
