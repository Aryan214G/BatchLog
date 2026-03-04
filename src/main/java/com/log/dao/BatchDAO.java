package com.log.dao;
import com.log.database.DBUtil;
import com.log.model.Batch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchDAO {
    public void insertBatch(Batch batch) {

        String sql = "INSERT INTO Batch VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batch.getBatchCode());
            stmt.setInt(2, batch.getBatchId());
            stmt.setString(3, batch.getTestDate());
            stmt.setString(4, batch.getTestSite());
            stmt.setInt(5, batch.getProjectId());
            stmt.setInt(6, batch.getProductCode());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        rs.getInt("Batch_ID"),
                        rs.getString("Test_date"),
                        rs.getString("Test_site"),
                        rs.getInt("Project_ID"),
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
                        rs.getInt("Batch_ID"),
                        rs.getString("Test_date"),
                        rs.getString("Test_site"),
                        rs.getInt("Project_ID"),
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

}
