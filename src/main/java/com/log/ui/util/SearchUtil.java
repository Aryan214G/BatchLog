package com.log.ui.util;

import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.model.Property;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchUtil {
    public List<BatchTest> searchBatches(Connection conn,
                                         String projectName,
                                         String productName,
                                         String batchId,
                                         String testDate,
                                         String testSite) throws SQLException {

        StringBuilder sql = new StringBuilder("""
        SELECT DISTINCT bt.*
        FROM Batch_Test bt
        JOIN Batch b ON bt.Batch_CODE = b.Batch_CODE
        JOIN Product pd ON b.Product_CODE = pd.Product_CODE
        JOIN Project pr ON pd.Project_ID = pr.Project_ID
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (projectName != null && !projectName.isBlank()) {
            sql.append(" AND LOWER(pr.Project_Name) LIKE ?");
            params.add("%" + projectName.toLowerCase() + "%");
        }

        if (productName != null && !productName.isBlank()) {
            sql.append(" AND LOWER(pd.Product_Name) LIKE ?");
            params.add("%" + productName.toLowerCase() + "%");
        }

        if (batchId != null && !batchId.isBlank()) {
            sql.append(" AND b.Batch_ID = ?");
            params.add(batchId);
        }

        if (testDate != null && !testDate.isBlank()) {
            sql.append(" AND bt.Test_Date = ?");
            params.add(testDate);
        }

        if (testSite != null && !testSite.isBlank()) {
            sql.append(" AND LOWER(bt.Test_Site) LIKE ?");
            params.add("%" + testSite.toLowerCase() + "%");
        }

        PreparedStatement stmt = conn.prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();

        List<BatchTest> results = new ArrayList<>();

        while (rs.next()) {
            results.add(mapToBatchTest(rs));
        }

        return results;
    }


    private BatchTest mapToBatchTest(ResultSet rs) throws SQLException {
        return new BatchTest(
                rs.getInt("Test_ID"),
                rs.getInt("Batch_CODE"),
                rs.getString("Test_Date"),
                rs.getString("Test_Site")
        );
    }


}
