package com.log.ui.util;

import com.log.model.Batch;
import com.log.model.Property;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchUtil {
    public List<Batch> searchBatches(Connection conn,
                                     String projectName,
                                     String productName,
                                     Integer batchId,
                                     LocalDate testDate,
                                     String testSite) throws SQLException {

        StringBuilder sql = new StringBuilder("""
        SELECT DISTINCT b.*
        FROM Batch b
        JOIN Project pr ON b.Project_ID = pr.Project_ID
        JOIN Product pd ON b.Product_ID = pd.Product_ID
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

        if (batchId != null) {
            sql.append(" AND b.Batch_CODE = ?");
            params.add(batchId);
        }

        if (testDate != null) {
            sql.append(" AND b.Test_Date = ?");
            params.add(Date.valueOf(testDate));
        }

        if (testSite != null && !testSite.isBlank()) {
            sql.append(" AND LOWER(b.Test_Site) LIKE ?");
            params.add("%" + testSite.toLowerCase() + "%");
        }

        PreparedStatement stmt = conn.prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();

        List<Batch> results = new ArrayList<>();

        while (rs.next()) {
            results.add(mapToBatch(rs)); // you implement this
        }

        return results;
    }


    private Batch mapToBatch(ResultSet rs) throws SQLException {
        return new Batch(
                rs.getInt("Batch_CODE"),
                rs.getInt("Batch_ID"),
                rs.getString("Test_Date"),
                rs.getString("Test_Site"),
                rs.getInt("Project_ID"),
                rs.getInt("Product_ID")
        );
    }
}
