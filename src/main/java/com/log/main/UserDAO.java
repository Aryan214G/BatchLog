package com.log.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    public static void createTable() {
        String sql = """
                
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
