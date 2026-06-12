package com.log.ui;

import com.log.core.AppState;
import com.log.dao.BatchTestDAO;
import com.log.dao.ProductDAO;
import com.log.dao.PropertyDAO;
import com.log.database.DBUtil;
import com.log.dto.ComparisonReportData;
import com.log.model.BatchTest;
import com.log.model.DefaultProperty;
import com.log.service.CategoryService;
import com.log.service.export.ComparisonReportService;
import com.log.service.export.ReportGenerator;
import javafx.collections.ObservableList;
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
    private BatchTestDAO batchTestDAO = new BatchTestDAO();
    private ProductDAO productDAO = new ProductDAO();


    private AppState appState = AppState.getInstance();
    private CategoryService categoryService = new CategoryService();

    private Map<Integer, Map<String, Map<String, Double>>> unitData = new LinkedHashMap<>();

    // Add this field to store category-ordered properties
    private List<String> allPropertiesOrdered = new ArrayList<>();

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
            for (String p : allPropertiesOrdered) {
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
                try {
                    populateGrid();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
            Map<String, Map<String, Double>> unitAverages;

            if (test.getBatchCode() != null) {
                averages     = propertyDAO.getPropertyAveragesByBatch(conn, test.getBatchCode());
                unitAverages = propertyDAO.getPropertyAveragesByBatchWithUnits(conn, test.getBatchCode());
                data.put(test.getBatchCode(), averages);
                unitData.put(test.getBatchCode(), unitAverages);
            } else {
                averages     = propertyDAO.getPropertyAveragesByTest(conn, test.getTestId());
                unitAverages = propertyDAO.getPropertyAveragesByTestWithUnits(conn, test.getTestId());
                data.put(-test.getTestId(), averages);
                unitData.put(-test.getTestId(), unitAverages);
            }

            propertySet.addAll(averages.keySet());
        }

        allPropertiesOrdered = buildCategoryOrderedProperties(conn, propertySet);
        allProperties = new ArrayList<>(propertySet);
    }


    private List<String> buildCategoryOrderedProperties(
            Connection conn,
            Set<String> propertyNames
    ) throws SQLException {

        categoryService.refreshCategoriesState();

        List<String> categoryOrder =
                appState.getCategories();

        List<String> ordered =
                new ArrayList<>();

        for (String category : categoryOrder) {

            ObservableList<DefaultProperty> props =
                    appState.getCategoriesMap().get(category);

            if (props == null) {
                continue;
            }

            for (DefaultProperty dp : props) {

                for (String key : propertyNames) {

                    String propertyName =
                            parseKey(key)[0];

                    if (propertyName.equals(
                            dp.getPropertyName()
                    )) {
                        ordered.add(key);
                    }
                }
            }
        }

        for (String key : propertyNames) {

            if (!ordered.contains(key)) {
                ordered.add(key);
            }
        }

        return ordered;
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid() throws SQLException {
        comparisonGrid.getChildren().clear();
        comparisonGrid.getColumnConstraints().clear();
        Connection conn = DBUtil.getConnection();

        List<String> visibleProperties = allPropertiesOrdered.stream()
                .filter(p -> propertyStates.getOrDefault(p, true))
                .toList();

        List<BatchTest> visibleBatches = batches.stream()
                .filter(b -> {
                    String key = b.getBatchCode() != null
                            ? "Batch " + b.getBatchCode()
                            : "Test " + b.getTestId();
                    return batchStates.getOrDefault(key, true);
                })
                .toList();

        if (visibleBatches.isEmpty() || visibleProperties.isEmpty()) {
            Label empty = new Label("No data to display.");
            empty.getStyleClass().add("empty-label");
            comparisonGrid.add(empty, 0, 0);
            return;
        }

        // Collect all unique units per property across all visible batches
        // propertyName → Set<unit>

        int totalCols = 1 + visibleBatches.size();

        for (int i = 0; i < totalCols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(100);
            cc.setPrefWidth(140);
            cc.setHgrow(Priority.ALWAYS);
            comparisonGrid.getColumnConstraints().add(cc);
        }

        // Header row
        comparisonGrid.add(makeHeader("Property"), 0, 0);
        for (int i = 0; i < visibleBatches.size(); i++) {
            BatchTest test = visibleBatches.get(i);
            String title = test.getBatchCode() != null
                    ? batchTestDAO.getBatchIdByTestId(conn, test.getTestId())
                    : productDAO.getProductNameByCode(conn, test.getProductCode());
            comparisonGrid.add(makeHeader(title != null ? title : "Test " + test.getTestId()), i + 1, 0);
        }

        // Data rows — one row per property+unit combination
        int row = 1;

        for (String compositeKey : visibleProperties) {

            boolean isAlt = (row % 2 == 0);

            String[] parts = parseKey(compositeKey);

            String propertyName = parts[0];
            String temperature  = parts[1];
            String direction    = parts[2];
            String unit         = parts[3];

            StringBuilder label =
                    new StringBuilder(propertyName);

            if (!unit.isBlank()) {
                label.append(" (")
                        .append(unit)
                        .append(")");
            }

            if (!temperature.isBlank()) {
                label.append(" | ")
                        .append(temperature);
            }

            if (!direction.isBlank()) {
                label.append(" | ")
                        .append(direction);
            }

            comparisonGrid.add(
                    makeCell(label.toString(), isAlt),
                    0,
                    row
            );

            for (int i = 0;
                 i < visibleBatches.size();
                 i++) {

                BatchTest test =
                        visibleBatches.get(i);

                Integer key =
                        test.getBatchCode() != null
                                ? test.getBatchCode()
                                : -test.getTestId();

                Map<String, Double> props =
                        data.getOrDefault(
                                key,
                                Collections.emptyMap()
                        );

                String value =
                        props.containsKey(compositeKey)
                                ? formatDouble(
                                props.get(compositeKey)
                        )
                                : "—";

                comparisonGrid.add(
                        makeCell(value, isAlt),
                        i + 1,
                        row
                );
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


    @FXML
    private void handlePrint() {
        List<String> batchLabels = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {

            List<BatchTest> visibleBatches = batches.stream()
                    .filter(b -> {
                        String key = b.getBatchCode() != null
                                ? "Batch " + b.getBatchCode()
                                : "Test " + b.getTestId();
                        return batchStates.getOrDefault(key, true);
                    })
                    .toList();

            List<String> visibleProperties = allPropertiesOrdered.stream()
                    .filter(p -> propertyStates.getOrDefault(p, true))
                    .toList();

            // ── Build batch labels ────────────────────────────────────────────────
            for (BatchTest test : visibleBatches) {
                String label;
                if (test.getBatchCode() != null) {
                    label = batchTestDAO.getBatchIdByTestId(conn, test.getTestId());
                } else {
                    label = productDAO.getProductNameByCode(conn, test.getProductCode());
                }
                batchLabels.add(label != null ? label : "Test " + test.getTestId());
            }

            // ── Build rows — unit aware ───────────────────────────────────────────
            Map<String, List<String>> rows = new LinkedHashMap<>();

            for (String propName : visibleProperties) {

                // Collect all unique units for this property across visible batches
                Set<String> units = new LinkedHashSet<>();
                for (BatchTest test : visibleBatches) {
                    Integer key = test.getBatchCode() != null
                            ? test.getBatchCode()
                            : -test.getTestId();
                    Map<String, Double> unitMap = unitData
                            .getOrDefault(key, Collections.emptyMap())
                            .getOrDefault(propName, Collections.emptyMap());
                    units.addAll(unitMap.keySet());
                }

                if (units.size() <= 1) {
                    // Single unit — one row
                    String unit = units.isEmpty() ? "" : units.iterator().next();
                    List<String> values = new ArrayList<>();

                    for (BatchTest test : visibleBatches) {
                        Integer key = test.getBatchCode() != null
                                ? test.getBatchCode()
                                : -test.getTestId();
                        Map<String, Double> props = data.getOrDefault(
                                key, Collections.emptyMap()
                        );
                        values.add(props.containsKey(propName)
                                ? formatDouble(props.get(propName))
                                : "—");
                    }

                    String rowLabel = propName + (unit.isBlank() ? "" : " (" + unit + ")");
                    rows.put(rowLabel, values);

                } else {
                    // Multiple units — one row per unit
                    for (String unit : units) {
                        List<String> values = new ArrayList<>();

                        for (BatchTest test : visibleBatches) {
                            Integer key = test.getBatchCode() != null
                                    ? test.getBatchCode()
                                    : -test.getTestId();
                            Map<String, Double> unitMap = unitData
                                    .getOrDefault(key, Collections.emptyMap())
                                    .getOrDefault(propName, Collections.emptyMap());
                            values.add(unitMap.containsKey(unit)
                                    ? formatDouble(unitMap.get(unit))
                                    : "—");
                        }

                        rows.put(propName + " (" + unit + ")", values);
                    }
                }
            }

            ComparisonReportData reportData = new ComparisonReportData(batchLabels, rows);
            new ComparisonReportService().generateReport(reportData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] parseKey(String key) {

        String[] parts = key.split("\\|\\|", -1);

        if (parts.length < 4) {
            return new String[] {
                    key,
                    "",
                    "",
                    ""
            };
        }

        return parts;
    }
}