package com.log.dao;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchDAO {

    public void insertBatchByCODE(Batch batch) {

        //TODO: recheck this query. Use WHERE batchCode keyword
        String sql = "INSERT INTO Batch VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batch.getBatchCode());
            stmt.setString(2, batch.getBatchId());
            stmt.setInt(3, batch.getProductCode());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertBatchByID(Connection conn, Batch batch) {
        String sql = "INSERT INTO Batch (Batch_ID, Product_CODE) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, batch.getBatchId());
            stmt.setInt(2, batch.getProductCode());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Batch getBatch(int batchCode) {

        String sql = "SELECT * FROM Batch WHERE Batch_CODE = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batchCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Batch(
                        rs.getInt("Batch_CODE"),
                        rs.getString("Batch_ID"),
                        rs.getInt("Product_CODE")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Batch> getAllBatches() {

        List<Batch> batches = new ArrayList<>();

        String sql = "SELECT * FROM Batch";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Batch batch = new Batch(
                        rs.getInt("Batch_CODE"),
                        rs.getString("Batch_ID"),
                        rs.getInt("Product_CODE")
                );

                batches.add(batch);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return batches;
    }

    public void deleteBatch(int batchCode) {

        String sql = "DELETE FROM Batch WHERE Batch_CODE = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batchCode);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public List<BatchRow> getBatchesInProject(Connection conn, String projectName) throws SQLException {
        String sql = """
            SELECT
                b.Batch_CODE,
                b.Batch_ID,
                p.Product_name,
                bt.Test_date,
                bt.Test_site
            FROM Batch b
            JOIN Product   p  ON b.Product_CODE  = p.Product_code
            JOIN Project   pr ON p.Project_ID    = pr.Project_ID
            LEFT JOIN Batch_Test bt ON bt.Batch_CODE = b.Batch_CODE
            WHERE pr.Project_name = ?
        """;

        List<BatchRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(new BatchRow(
                        rs.getInt("Batch_CODE"),
                        rs.getString("Batch_ID"),
                        rs.getString("Product_name"),
                        rs.getString("Test_date"),
                        rs.getString("Test_site")));
            }
        }

        return rows;
    }

    public int getBatchCode(Connection conn, Batch batch){
        String sql = "SELECT Batch_CODE FROM Batch WHERE Batch_ID=? AND Product_CODE=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batch.getBatchId());
            stmt.setInt(2, batch.getProductCode());

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt("Batch_CODE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;


    }

}
