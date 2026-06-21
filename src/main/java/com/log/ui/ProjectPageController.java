package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.BatchRow;
import com.log.model.BatchTest;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.ProjectContentService;
import com.log.service.export.ProjectExportService;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectPageController {

    @FXML private Label projectTitleLabel;
    @FXML private GridPane batchesGrid;
    @FXML private Button exportButton;

    private String currentProjectName;
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private final AppState appState = AppState.getInstance();
    private final ProjectService projectService = new ProjectService();
    private ProjectExportService projectExportService = new ProjectExportService();
    private BatchService batchService = new BatchService();
    //private BatchTestService batchTestService = new BatchTestService();


    // ── Entry point ───────────────────────────────────────────────────────────

     public void initialize()
     {
         exportButton.setVisible(false);
     }

    public void loadProject(String projectName) {
        this.currentProjectName = projectName;
        projectTitleLabel.setText(projectName);

        // Ensure state is always current when this page loads
        bpropState.setProjectName(projectName);
        try (Connection conn = DBUtil.getConnection()) {
            int projectId = projectService.getProjectId(conn, projectName);
            bpropState.setProjectId(projectId);

            List<BatchRow> batches = projectContentService.fetchProjectContents(conn, projectName);

            populateGrid(batches);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    private final ProjectContentService projectContentService = new ProjectContentService();
    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid(List<BatchRow> rows) {
        batchesGrid.getChildren().clear();
        batchesGrid.getColumnConstraints().clear();

        String[] headers = {"Batch ID","Component ID", "Product", "Test Date", "Test Site","SOP"};

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

            ContextMenu contextMenu = createRowContextMenu(b);  // add this

            Label batchIdCell     = makeCell(b.getBatchId(),     isAlt);
            Label componentIdCell = makeCell(b.getComponentId(), isAlt);
            Label productCell     = makeCell(b.getProductName(), isAlt);
            Label testDateCell    = makeCell(b.getTestDate(),    isAlt);
            Label testSiteCell    = makeCell(b.getTestSite(),    isAlt);
            Label sopCell         = makeCell(b.getSop(),         isAlt);

            // Left click opens results, right click shows context menu
            for (Label cell : List.of(batchIdCell, componentIdCell, productCell, testDateCell, testSiteCell, sopCell)) {
                cell.getStyleClass().add("clickable-row");
                cell.setOnMouseClicked(e -> {
                    if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        openBatchResults(b);
                    }
                });
                cell.setContextMenu(contextMenu);
            }

            batchesGrid.add(batchIdCell,     0, row);
            batchesGrid.add(componentIdCell, 1, row);
            batchesGrid.add(productCell,     2, row);
            batchesGrid.add(testDateCell,    3, row);
            batchesGrid.add(testSiteCell,    4, row);
            batchesGrid.add(sopCell,         5, row);
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
        bpropState.setBatchNo(b.getBatchId());
        bpropState.setProductName(b.getProductName());
        bpropState.setProductID(b.getComponentId());
        bpropState.setTestDate(LocalDate.parse(b.getTestDate()));
        bpropState.setPlaceOfTesting((b.getTestSite()));
        bpropState.setTestId(b.getTestId());
        bpropState.setSop(b.getSop());
        bpropState.setTestSchedule(b.getTestSchedule());
        bpropState.setProductCode(b.getProductCode());
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            BatchTest batch = new BatchTest(
                    b.getTestId(),
                    b.getBatchCode(),
                    b.getTestDate(),
                    b.getTestSite(),
                    b.getProductCode(),
                    b.getSop(),
                    b.getTestSchedule()
            );

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

//    private void refreshBatches() {
//        batchesGrid.getChildren().clear();
//        try (Connection conn = DBUtil.getConnection()) {
//            List<BatchRow> batches = fetchBatches(conn, currentProjectName);
//            populateGrid(batches);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void handleExport() throws SQLException {

        Stage stage = (Stage) exportButton
                .getScene()
                .getWindow();

        List<BatchRow> batches = batchService.getBatchesInProject(
                DBUtil.getConnection(), currentProjectName
        );

        projectExportService.export(stage, batches, currentProjectName);

    }



    private ContextMenu createRowContextMenu(BatchRow b) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openResultsItem = new MenuItem("View Results");
        openResultsItem.setOnAction(e -> openBatchResults(b));

        MenuItem openCategoriesItem = new MenuItem("Add new entries");
        openCategoriesItem.setOnAction(e -> openInCategories(b));

        contextMenu.getItems().addAll(openResultsItem, openCategoriesItem);
        return contextMenu;
    }

    private void openInCategories(BatchRow b) {
        bpropState.setBatchNo(b.getBatchId());
        bpropState.setProductName(b.getProductName());
        bpropState.setProductID(b.getComponentId());
        bpropState.setTestDate(LocalDate.parse(b.getTestDate()));
        bpropState.setPlaceOfTesting(b.getTestSite());
        bpropState.setTestId(b.getTestId());
        bpropState.setSop(b.getSop());
        bpropState.setBatchCode(b.getBatchCode());
        bpropState.setProductCode(b.getProductCode());

        appState.setProjectCreated(true);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/CategoriesPage.fxml")
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