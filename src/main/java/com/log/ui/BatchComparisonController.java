package com.log.ui;

import com.log.dao.PropertyDAO;
import com.log.database.DBUtil;
import com.log.model.BatchTest;
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

        for (BatchTest batch : batches) {
            Map<String, Double> averages = propertyDAO.getPropertyAveragesByBatch(conn, batch.getBatchCode());
            data.put(batch.getBatchCode(), averages);
            propertySet.addAll(averages.keySet());
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
            comparisonGrid.add(makeHeader("Batch " + visibleBatches.get(i).getBatchCode()), i + 1, 0);
        }

        // Data rows — one per property
        int row = 1;
        for (String propName : visibleProperties) {
            boolean isAlt = (row % 2 == 0);

            // First cell — property name
            comparisonGrid.add(makeCell(propName, isAlt), 0, row);

            // Batch cells — average value for this property in each batch
            for (int i = 0; i < visibleBatches.size(); i++) {
                Map<String, Double> props = data.getOrDefault(
                        visibleBatches.get(i).getBatchCode(), Collections.emptyMap()
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
}