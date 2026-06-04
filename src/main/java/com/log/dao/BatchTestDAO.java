package com.log.dao;

import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.*;

public class BatchTestDAO {

    public int insertBatchTest(Connection conn, BatchTest batchTest) {
        String sql = "INSERT INTO Batch_Test (Batch_CODE, Test_date, Test_site, SOP) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, batchTest.getBatchCode());
            stmt.setString(2, batchTest.getTestDate());
            stmt.setString(3, batchTest.getTestSite());
            stmt.setString(4,batchTest.getSOP());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBatchTestId(Connection conn, BatchTest batchTest){
        String sql = "SELECT Test_ID FROM Batch_Test WHERE Batch_CODE=? AND Test_date=? AND Test_site=? AND SOP=? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batchTest.getBatchCode());
            stmt.setString(2, batchTest.getTestDate());
            stmt.setString(3, batchTest.getTestSite());
            stmt.setString(4,batchTest.getSOP());

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt("Test_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;


    }

    public String getBatchIdByTestId(Connection conn, int testId) {

    String sql = """
            SELECT b.Batch_ID
            FROM Batch_Test bt
            JOIN Batch b
                ON bt.Batch_CODE = b.Batch_CODE
            WHERE bt.Test_ID = ?
            """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, testId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("Batch_ID");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}

    public BatchTest getBatchTestById(Connection conn, int testId) {

    String sql = """
            SELECT *
            FROM Batch_Test
            WHERE Test_ID = ?
            """;

    try (PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setInt(1, testId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            BatchTest batchTest = new BatchTest(
                   rs.getInt("Test_ID"),
                    rs.getInt("Batch_CODE"),
                    rs.getString("Test_date"),
                    rs.getString("Test_site"),
                    rs.getString("SOP")
            );

            return batchTest;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}
}
