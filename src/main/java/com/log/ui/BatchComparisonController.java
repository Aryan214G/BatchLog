package com.log.ui;

import com.log.dao.PropertyDAO;
import com.log.database.DBUtil;
import com.log.dto.ComparisonReportData;
import com.log.model.BatchTest;
import com.log.service.export.ComparisonReportService;
import com.log.service.export.ReportGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BatchComparisonController {

    @FXML private GridPane comparisonGrid;
    @FXML private Label titleLabel;

    private Map<Integer, Map<String, Double>> data = new LinkedHashMap<>();
    private List<String> allProperties = new ArrayList<>();
    private List<BatchTest> batches;
    private PropertyDAO propertyDAO = new PropertyDAO();

    private ReportGenerator<ComparisonReportData> reportService = new ComparisonReportService();

    // ── Filter state ──────────────────────────────────────────────────────────
    // Column = each batch row, Property = each property column
    private Map<String, Boolean> batchStates   = new LinkedHashMap<>();
    private Map<String, Boolean> propertyStates = new LinkedHashMap<>();

    public void loadComparison(List<BatchTest> batches) {
        this.batches = batches;
        titleLabel.setText("Batch Comparison — " + batches.size() + " batches selected");

        try (Connection conn = DBUtil.getConnection()) {
            fetchData(conn);

            // Default all batches and properties visible
            batchStates.clear();
            for (BatchTest b : batches) {
                batchStates.put("Batch " + b.getBatchCode(), true);
            }

            propertyStates.clear();
            for (String p : allProperties) {
                propertyStates.put(p, true);
            }

            populateGrid();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── Filter button ─────────────────────────────────────────────────────────

    @FXML
    private void handleFilter() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/FilterPopup.fxml")
            );
            Parent root = loader.load();

            FilterPopupController controller = loader.getController();
            // Batches act as "columns" in the filter popup
            controller.setColumnStates(batchStates);
            controller.setPropertyStates(propertyStates);
            controller.loadCheckboxes();
            controller.setOnApply(result -> {
                batchStates.clear();
                batchStates.putAll(result.columnStates);
                propertyStates.clear();
                propertyStates.putAll(result.propertyStates);
                populateGrid();
            });

            Stage popupStage = new Stage();
            popupStage.setTitle("Filter");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.initOwner(comparisonGrid.getScene().getWindow());
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    private void fetchData(Connection conn) throws SQLException {
        Set<String> propertySet = new LinkedHashSet<>();

        for (BatchTest test : batches) {

            Map<String, Double> averages;

            if (test.getBatchCode() != null) {

                averages =
                        propertyDAO.getPropertyAveragesByBatch(
                                conn,
                                test.getBatchCode()
                        );

                data.put(
                        test.getBatchCode(),
                        averages
                );

            } else {

                averages =
                        propertyDAO.getPropertyAveragesByTest(
                                conn,
                                test.getTestId()
                        );

                data.put(
                        -test.getTestId(),   // unique key
                        averages
                );
            }

            propertySet.addAll(
                    averages.keySet()
            );
        }

        allProperties = new ArrayList<>(propertySet);
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid() {
        comparisonGrid.getChildren().clear();
        comparisonGrid.getColumnConstraints().clear();

        // Filter visible properties and batches
        List<String> visibleProperties = allProperties.stream()
                .filter(p -> propertyStates.getOrDefault(p, true))
                .toList();

        List<BatchTest> visibleBatches = batches.stream()
                .filter(b -> batchStates.getOrDefault("Batch " + b.getBatchCode(), true))
                .toList();

        if (visibleBatches.isEmpty() || visibleProperties.isEmpty()) {
            Label empty = new Label("No data to display.");
            empty.getStyleClass().add("empty-label");
            comparisonGrid.add(empty, 0, 0);
            return;
        }

        // Columns: "Property" + one per batch
        int totalCols = 1 + visibleBatches.size();

        for (int i = 0; i < totalCols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(100);
            cc.setPrefWidth(140);
            cc.setHgrow(Priority.ALWAYS);
            comparisonGrid.getColumnConstraints().add(cc);
        }

        // Header row — "Property" + batch identifiers
        comparisonGrid.add(makeHeader("Property"), 0, 0);
        for (int i = 0; i < visibleBatches.size(); i++) {
            BatchTest test = visibleBatches.get(i);
            String title;
            if (test.getBatchCode() != null) {
                title = "Batch " + test.getBatchCode();
            } else {title = "Test " + test.getTestId();}
            comparisonGrid.add(makeHeader(title), i + 1, 0);
        }

        // Data rows — one per property
        int row = 1;
        for (String propName : visibleProperties) {
            boolean isAlt = (row % 2 == 0);

            // First cell — property name
            comparisonGrid.add(makeCell(propName, isAlt), 0, row);

            // Batch cells — average value for this property in each batch
            for (int i = 0; i < visibleBatches.size(); i++) {
                BatchTest test = visibleBatches.get(i);

                Integer key =
                        (test.getBatchCode() != null)
                                ? test.getBatchCode()
                                : -test.getTestId();

                Map<String, Double> props =
                        data.getOrDefault(
                                key,
                                Collections.emptyMap()
                        );
                String val = props.containsKey(propName)
                        ? formatDouble(props.get(propName))
                        : "—";
                comparisonGrid.add(makeCell(val, isAlt), i + 1, row);
            }

            row++;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatDouble(double value) {
        if (value == Math.floor(value)) return String.valueOf((int) value);
        return String.format("%.2f", value);
    }

    private Label makeHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("grid-header");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
        return label;
    }

    private Label makeCell(String text, boolean isAlt) {
        Label label = new Label(text != null ? text : "—");
        label.getStyleClass().add("grid-cell");
        if (isAlt) label.getStyleClass().add("grid-cell-alt");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
        return label;
    }

    // ── Back ──────────────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalPage.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) comparisonGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //TODO: feeding example data currently, replace that with actual implememtation
    @FXML
    private void handlePrint(){
        Map<String, List<String>> rows =
            new LinkedHashMap<>();

    rows.put(
            "Density",
            List.of(
                    "1.23",
                    "1.45",
                    "1.31"
            )
    );

    rows.put(
            "Open Porosity",
            List.of(
                    "2.10",
                    "2.30",
                    "2.25"
            )
    );

    rows.put(
            "Flexural Strength",
            List.of(
                    "45.2",
                    "47.8",
                    "44.9"
            )
    );

    ComparisonReportData data =
            new ComparisonReportData(
                    List.of(
                            "Batch-101",
                            "Batch-102",
                            "Batch-103"
                    ),
                    rows
            );

    try {
        new ComparisonReportService()
                .generateReport(data);

    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}