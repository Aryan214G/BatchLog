package com.log.dao;

import com.log.model.BatchTest;

import java.sql.*;

public class BatchTestDAO {

    public int insertBatchTest(Connection conn, BatchTest batchTest){
        String sql = "INSERT INTO Batch_Test (Batch_CODE, Test_date, Test_site) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, batchTest.getBatchCode());
            stmt.setString(2, batchTest.getTestDate());
            stmt.setString(3, batchTest.getTestSite());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
