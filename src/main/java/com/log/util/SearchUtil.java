package com.log.util;

import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchUtil {
    //TODO: add sop to query from batchtest
    public List<Batch> searchBatches(Connection conn,
                                     String projectName,
                                     String productName,
                                     String batchId,
                                     String testDate,
                                     String testSite,
                                     String sop) throws SQLException {

        StringBuilder sql = new StringBuilder("""
            SELECT
                            b.Batch_CODE,
                            b.Batch_ID,
                            bt.Test_ID,
                            bt.Test_Date,
                            bt.Test_Site,
                            bt.Product_CODE,
                            bt.SOP
            FROM Batch b
            JOIN Product pd         ON b.Product_CODE  = pd.Product_code
            JOIN Project pr         ON pd.Project_ID   = pr.Project_ID
            LEFT JOIN Batch_Test bt ON bt.Batch_CODE   = b.Batch_CODE
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (projectName != null && !projectName.isBlank()) {
            sql.append(" AND LOWER(pr.Project_name) LIKE ?");
            params.add("%" + projectName.toLowerCase() + "%");
        }
        if (productName != null && !productName.isBlank()) {
            sql.append(" AND LOWER(pd.Product_name) LIKE ?");
            params.add("%" + productName.toLowerCase() + "%");
        }
        if (batchId != null && !batchId.isBlank()) {
            sql.append(" AND LOWER(b.Batch_ID) LIKE ?");
            params.add("%" + batchId.toLowerCase() + "%");
        }
        if (testDate != null && !testDate.isBlank()) {
            sql.append(" AND bt.Test_Date = ?");
            params.add(testDate);
        }
        if (testSite != null && !testSite.isBlank()) {
            sql.append(" AND LOWER(bt.Test_Site) LIKE ?");
            params.add("%" + testSite.toLowerCase() + "%");
        }
        if (sop != null && !sop.isBlank()) {
            sql.append(" AND LOWER(bt.SOP) LIKE ?");
            params.add("%" + sop.toLowerCase() + "%");
        }
        sql.append(" ORDER BY b.Batch_CODE, bt.Test_ID");

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();

        // Group tests under their batch using LinkedHashMap to preserve order
        Map<Integer, Batch> batchMap = new LinkedHashMap<>();

        while (rs.next()) {
            int batchCode = rs.getInt("Batch_CODE");

            Batch batch = batchMap.computeIfAbsent(batchCode, code ->
                    {
                        try {
                            return new Batch(code, rs.getString("Batch_ID"), 0);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            // Add test if it exists (LEFT JOIN may return null Test_ID)
            int testId = rs.getInt("Test_ID");
            if (!rs.wasNull()) {
                batch.getTests().add(new BatchTest(
                        testId,
                        batchCode,
                        rs.getString("Test_Date"),
                        rs.getString("Test_Site"),
                        rs.getInt("Product_CODE"),
                        rs.getString("SOP"),
                        rs.getString("Test_schedule")
                ));
            }
        }

        return new ArrayList<>(batchMap.values());
    }

    public List<BatchTest> searchProductTests(
            Connection conn,
            String projectName,
            String productName,
            String testDate,
            String testSite,
            String sop
    ) throws SQLException {

        StringBuilder sql = new StringBuilder("""
        SELECT
        bt.Test_ID,
        bt.Test_Date,
        bt.Test_Site,
        bt.Product_CODE,
        bt.SOP,
        pd.Product_name
    FROM Batch_Test bt
    JOIN Product pd
        ON bt.Product_CODE = pd.Product_CODE
    JOIN Project pr
        ON pd.Project_ID = pr.Project_ID
    WHERE bt.Batch_CODE IS NULL
""");

        List<Object> params = new ArrayList<>();

        if (projectName != null && !projectName.isBlank()) {
            sql.append(" AND LOWER(pr.Project_name) LIKE ?");
            params.add("%" + projectName.toLowerCase() + "%");
        }

        if (productName != null && !productName.isBlank()) {
            sql.append(" AND LOWER(pd.Product_name) LIKE ?");
            params.add("%" + productName.toLowerCase() + "%");
        }

        if (testDate != null && !testDate.isBlank()) {
            sql.append(" AND bt.Test_Date = ?");
            params.add(testDate);
        }

        if (testSite != null && !testSite.isBlank()) {
            sql.append(" AND LOWER(bt.Test_Site) LIKE ?");
            params.add("%" + testSite.toLowerCase() + "%");
        }

        if (sop != null && !sop.isBlank()) {
            sql.append(" AND LOWER(bt.SOP) LIKE ?");
            params.add("%" + sop.toLowerCase() + "%");
        }

        PreparedStatement stmt =
                conn.prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();

        List<BatchTest> tests = new ArrayList<>();

        while (rs.next()) {

            tests.add(
                    new BatchTest(
                            rs.getInt("Test_ID"),
                            null,
                            rs.getString("Test_Date"),
                            rs.getString("Test_Site"),
                            rs.getInt("Product_CODE"),
                            rs.getString("SOP"),
                            rs.getString("Test_schedule")
                    )
            );
        }

        return tests;
    }
}