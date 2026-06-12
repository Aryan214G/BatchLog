package com.log.service;

import com.log.model.BatchRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectContentService {


    public List<BatchRow> fetchProjectContents(
            Connection conn,
            String projectName
    ) throws SQLException {

        String sql = """
            SELECT
                bt.Test_ID,

                bt.Batch_CODE,
                bt.Product_CODE,

                b.Batch_ID,

                p.Product_name,
                
                p.Product_ID,

                bt.Test_date,
                bt.Test_site,
                bt.SOP,
                bt.Test_schedule

            FROM Batch_Test bt

            LEFT JOIN Batch b
                ON bt.Batch_CODE = b.Batch_CODE

            JOIN Product p
                ON bt.Product_CODE = p.Product_CODE

            JOIN Project pr
                ON p.Project_ID = pr.Project_ID

            WHERE pr.Project_name = ?

            ORDER BY bt.Test_date DESC
            """;

        List<BatchRow> rows = new ArrayList<>();

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, projectName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Integer batchCode =
                        (Integer) rs.getObject(
                                "Batch_CODE"
                        );

                rows.add(
                        new BatchRow(
                                batchCode,
                                rs.getString("Batch_ID"),
                                rs.getString("Product_name"),
                                rs.getString("Test_date"),
                                rs.getString("Test_site"),
                                rs.getInt("Test_ID"),
                                rs.getInt("Product_CODE"),
                                rs.getString("Product_ID"),
                                rs.getString("SOP"),
                                rs.getString("Test_schedule")
                        )
                );
            }
        }

        return rows;
    }
}