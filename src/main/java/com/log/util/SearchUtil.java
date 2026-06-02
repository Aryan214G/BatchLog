package com.log.util;

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
        SELECT DISTINCT b.Batch_CODE, b.Batch_ID, bt.Test_ID, bt.Test_Date, bt.Test_Site
        FROM Batch b
        JOIN Product pd     ON b.Product_CODE  = pd.Product_code
        JOIN Project pr     ON pd.Project_ID   = pr.Project_ID
        LEFT JOIN Batch_Test bt ON bt.Batch_CODE = b.Batch_CODE
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
