package com.log.dao;

import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.*;

public class BatchTestDAO {

    public int insertBatchTest(Connection conn, BatchTest batchTest) {
        String sql = "INSERT INTO Batch_Test (Batch_CODE, Test_date, Test_site) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, batchTest.getBatchCode());
            stmt.setString(2, batchTest.getTestDate());
            stmt.setString(3, batchTest.getTestSite());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBatchTestId(Connection conn, BatchTest batchTest){
        String sql = "SELECT Test_ID FROM Batch_Test WHERE Batch_CODE=? AND Test_date=? AND Test_site=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batchTest.getBatchCode());
            stmt.setString(2, batchTest.getTestDate());
            stmt.setString(3, batchTest.getTestSite());

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt("Test_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;


    }
}
