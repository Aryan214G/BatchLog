package com.log.dao;

import com.log.model.BatchTest;

import java.sql.*;

public class BatchTestDAO {

    public int insertBatchTest(
            Connection conn,
            BatchTest batchTest
    ) {

        String sql = """
            INSERT INTO Batch_Test
            (
                Batch_CODE,
                Test_date,
                Test_site,
                Product_CODE,
                SOP,
                Test_schedule
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            if (batchTest.getBatchCode() == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(
                        1,
                        batchTest.getBatchCode()
                );
            }

            stmt.setString(
                    2,
                    batchTest.getTestDate()
            );

            stmt.setString(
                    3,
                    batchTest.getTestSite()
            );

            stmt.setInt(
                    4,
                    batchTest.getProductCode()
            );

            stmt.setString(
                    5,
                    batchTest.getSOP()
            );

            stmt.setString(
                    6,
                    batchTest.getTestSchedule()
            );

            stmt.executeUpdate();

            ResultSet rs =
                    stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int getBatchTestId(
            Connection conn,
            BatchTest batchTest
    ) {

        String sql;

        if (batchTest.getBatchCode() == null) {

            sql = """
                SELECT Test_ID
                FROM Batch_Test
                WHERE Product_CODE = ?
                  AND Batch_CODE IS NULL
                  AND Test_date = ?
                  AND Test_site = ?
                  AND (
                        SOP = ?
                        OR (SOP IS NULL AND ? IS NULL)
                      )
                  AND (
                        Test_schedule = ?
                        OR (Test_schedule IS NULL AND ? IS NULL)
                      )
                """;

        } else {

            sql = """
                SELECT Test_ID
                FROM Batch_Test
                WHERE Product_CODE = ?
                  AND Batch_CODE = ?
                  AND Test_date = ?
                  AND Test_site = ?
                  AND (
                        SOP = ?
                        OR (SOP IS NULL AND ? IS NULL)
                      )
                  AND (
                        Test_schedule = ?
                        OR (Test_schedule IS NULL AND ? IS NULL)
                      )
                """;
        }

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            String sop =
                    batchTest.getSOP();

            String schedule =
                    batchTest.getTestSchedule();

            if (batchTest.getBatchCode() == null) {

                stmt.setInt(
                        1,
                        batchTest.getProductCode()
                );

                stmt.setString(
                        2,
                        batchTest.getTestDate()
                );

                stmt.setString(
                        3,
                        batchTest.getTestSite()
                );

                stmt.setString(4, sop);
                stmt.setString(5, sop);

                stmt.setString(6, schedule);
                stmt.setString(7, schedule);

            } else {

                stmt.setInt(
                        1,
                        batchTest.getProductCode()
                );

                stmt.setInt(
                        2,
                        batchTest.getBatchCode()
                );

                stmt.setString(
                        3,
                        batchTest.getTestDate()
                );

                stmt.setString(
                        4,
                        batchTest.getTestSite()
                );

                stmt.setString(5, sop);
                stmt.setString(6, sop);

                stmt.setString(7, schedule);
                stmt.setString(8, schedule);
            }

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(
                        "Test_ID"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getBatchIdByTestId(
            Connection conn,
            int testId
    ) {

        String sql = """
                SELECT b.Batch_ID
                FROM Batch_Test bt
                JOIN Batch b
                    ON bt.Batch_CODE = b.Batch_CODE
                WHERE bt.Test_ID = ?
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, testId);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getString(
                        "Batch_ID"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BatchTest getBatchTestById(
            Connection conn,
            int testId
    ) {

        String sql = """
                SELECT *
                FROM Batch_Test
                WHERE Test_ID = ?
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, testId);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                Integer batchCode =
                        (Integer) rs.getObject(
                                "Batch_CODE"
                        );

                BatchTest batchTest =
                        new BatchTest(
                                rs.getInt("Test_ID"),
                                batchCode,
                                rs.getString("Test_date"),
                                rs.getString("Test_site"),
                                rs.getInt("Product_CODE"),
                                rs.getString("SOP"),
                                rs.getString("Test_schedule")
                        );

                return batchTest;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateTestDate(
            Connection conn,
            int testId,
            String newTestDate
    ) {

        String sql =
                "UPDATE Batch_Test SET Test_date = ? WHERE Test_ID = ?";

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(
                    1,
                    newTestDate
            );

            stmt.setInt(
                    2,
                    testId
            );

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTestSite(
            Connection conn,
            int testId,
            String newTestSite
    ) {

        String sql =
                "UPDATE Batch_Test SET Test_site = ? WHERE Test_ID = ?";

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(
                    1,
                    newTestSite
            );

            stmt.setInt(
                    2,
                    testId
            );

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}