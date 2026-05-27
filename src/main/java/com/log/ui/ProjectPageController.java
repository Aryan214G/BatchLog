package com.log.ui;

import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.BatchRow;
import com.log.model.BatchTest;
import com.log.service.BatchService;
import com.log.service.ProjectExportService;
import com.log.service.ProjectService;
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
    @FXML private Button exportButton;

    private String currentProjectName;
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private final ProjectService projectService = new ProjectService();
    private ProjectExportService projectExportService = new ProjectExportService();
    private BatchService batchService = new BatchService();



    // ── Entry point ───────────────────────────────────────────────────────────

    public void loadProject(String projectName) {
        this.currentProjectName = projectName;
        projectTitleLabel.setText(projectName);

        // Ensure state is always current when this page loads
        bpropState.setProjectName(projectName);
        try (Connection conn = DBUtil.getConnection()) {
            int projectId = projectService.getProjectId(conn, projectName);
            bpropState.setProjectId(projectId);

            List<BatchRow> batches = fetchBatches(conn, projectName);
            populateGrid(batches);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    // TODO: This method was moved to BatchDAO. Remove this from this class.
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

        for (int i = 0; i < headers.length; i++) {
            batchesGrid.add(makeHeader(headers[i]), i, 0);
        }

        int row = 1;
        for (BatchRow b : rows) {
            boolean isAlt = (row % 2 == 0);

            Label batchIdCell  = makeCell(b.getBatchId(),     isAlt);
            Label productCell  = makeCell(b.getProductName(), isAlt);
            Label testDateCell = makeCell(b.getTestDate(),    isAlt);
            Label testSiteCell = makeCell(b.getTestSite(),    isAlt);

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

    // ── Open batch results ────────────────────────────────────────────────────

    private void openBatchResults(BatchRow b) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            BatchTest batch = new BatchTest(b.getBatchCode(), b.getTestDate(), b.getTestSite());

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(batch);
            controller.setBackDestination("/com/log/ui/views/ProjectPage.fxml");

            Stage stage = (Stage) batchesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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

    // ── Navigation ────────────────────────────────────────────────────────────

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

    @FXML
    private void handleNewBatch() {
        // State is already set in loadProject() — just navigate
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/NewBatch.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) batchesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshBatches() {
        batchesGrid.getChildren().clear();
        try (Connection conn = DBUtil.getConnection()) {
            List<BatchRow> batches = fetchBatches(conn, currentProjectName);
            populateGrid(batches);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleExport() throws SQLException {

        Stage stage = (Stage) exportButton
                .getScene()
                .getWindow();

        List<BatchRow> batches = batchService.getBatchesInProject(
                DBUtil.getConnection(), currentProjectName
        );

        projectExportService.export(stage, batches);

    }
}