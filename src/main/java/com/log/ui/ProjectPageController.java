package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.BatchTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectPageController {

    @FXML private Label projectTitleLabel;
    @FXML private GridPane batchesGrid;

    // ── Row model ─────────────────────────────────────────────────────────────

    private static class BatchRow {
        int batchCode;
        String batchId, productName, testDate, testSite;

        BatchRow(int batchCode, String batchId, String productName,
                 String testDate, String testSite) {
            this.batchCode   = batchCode;
            this.batchId     = batchId;
            this.productName = productName;
            this.testDate    = testDate;
            this.testSite    = testSite;
        }
    }

    // ── Entry point called by HomePageController ──────────────────────────────

    public void loadProject(String projectName) {
        projectTitleLabel.setText(projectName);

        try (Connection conn = DBUtil.getConnection()) {
            List<BatchRow> batches = fetchBatches(conn, projectName);
            populateGrid(batches);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    private List<BatchRow> fetchBatches(Connection conn, String projectName) throws SQLException {
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
                        rs.getString("Test_site")
                ));
            }
        }

        return rows;
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid(List<BatchRow> rows) {
        batchesGrid.getChildren().clear();
        batchesGrid.getColumnConstraints().clear();

        String[] headers = {"Batch ID", "Product", "Test Date", "Test Site"};

        for (int i = 0; i < headers.length; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / headers.length);
            cc.setHgrow(Priority.ALWAYS);
            batchesGrid.getColumnConstraints().add(cc);
        }

        // Header row
        for (int i = 0; i < headers.length; i++) {
            batchesGrid.add(makeHeader(headers[i]), i, 0);
        }

        // Data rows
        int row = 1;
        for (BatchRow b : rows) {
            boolean isAlt = (row % 2 == 0);

            Label batchIdCell     = makeCell(b.batchId,     isAlt);
            Label productCell     = makeCell(b.productName, isAlt);
            Label testDateCell    = makeCell(b.testDate,    isAlt);
            Label testSiteCell    = makeCell(b.testSite,    isAlt);

            // Make every cell in the row clickable
            for (Label cell : List.of(batchIdCell, productCell, testDateCell, testSiteCell)) {
                cell.getStyleClass().add("clickable-row");
                cell.setOnMouseClicked(e -> openBatchResults(b));
            }

            batchesGrid.add(batchIdCell,  0, row);
            batchesGrid.add(productCell,  1, row);
            batchesGrid.add(testDateCell, 2, row);
            batchesGrid.add(testSiteCell, 3, row);
            row++;
        }

        if (rows.isEmpty()) {
            Label empty = new Label("No batches found for this project.");
            empty.getStyleClass().add("empty-label");
            batchesGrid.add(empty, 0, 1, headers.length, 1);
        }
    }

// ── Open batch results ────────────────────────────────────────────────────────

    private void openBatchResults(BatchRow b) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            // Build a BatchTest from the BatchRow data to pass to RetrievalResultsController
            BatchTest batch = new BatchTest(b.batchCode, b.testDate, b.testSite);

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(batch);

            controller.loadBatch(batch);
            controller.setBackDestination("/com/log/ui/views/ProjectPage.fxml"); // go back to projectchrr

            Stage stage = (Stage) batchesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Label makeHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("grid-header");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private Label makeCell(String text, boolean isAlt) {
        Label label = new Label(text != null ? text : "—");
        label.getStyleClass().add("grid-cell");
        if (isAlt) label.getStyleClass().add("grid-cell-alt");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    // ── Back button ───────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/HomePage.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) batchesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}