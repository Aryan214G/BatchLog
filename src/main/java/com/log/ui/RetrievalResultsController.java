package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RetrievalResultsController {

    @FXML private Label batchTitleLabel;
    @FXML private GridPane propertiesGrid;

    private List<PropertyRow> cachedRows = new ArrayList<>();

    private String backDestination = "/com/log/ui/views/RetrievalPage.fxml";

    private Map<String, Boolean> columnStates = new LinkedHashMap<>() {{
        put("Property",      true);
        put("Category",      true);
        put("Temperature",   true);
        put("Direction",     true);
        put("Average Value", true);
        put("Values",        true);
    }};

    private Map<String, Boolean> propertyStates = new LinkedHashMap<>();

    public void setBackDestination(String path) {
        this.backDestination = path;
    }

    private BasePropertiesState basePropertiesState = BasePropertiesState.getInstance();

    private AppState appState = AppState.getInstance();

    private int testId;
    // ── Row model ─────────────────────────────────────────────────────────────

    private static class PropertyRow {
        String name, category, temperature, direction, unit;
        List<Double> values = new ArrayList<>();

        PropertyRow(String name, String category, String temperature,
                    String direction, String unit) {
            this.name        = name;
            this.category    = category;
            this.temperature = temperature;
            this.direction   = direction;
            this.unit        = unit;
        }

        double average() {
            return values.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public void loadBatch(BatchTest batch) {
        batchTitleLabel.setText(
                "Batch: " + batch.getBatchCode() +
                        " | Date: " + batch.getTestDate() +
                        " | Site: " + batch.getTestSite()
        );

        try (Connection conn = DBUtil.getConnection()) {
            cachedRows = fetchProperties(conn, batch.getBatchCode());

            // Build property filter state — all visible by default
            propertyStates.clear();
            for (PropertyRow r : cachedRows) {
                propertyStates.put(r.name, true);
            }

            appState.setProjectCreated(true);

            populateGrid(cachedRows);
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
            controller.setColumnStates(columnStates);
            controller.setPropertyStates(propertyStates);
            controller.loadCheckboxes();
            controller.setOnApply(result -> {
                columnStates.clear();
                columnStates.putAll(result.columnStates);
                propertyStates.clear();
                propertyStates.putAll(result.propertyStates);
                populateGrid(cachedRows);
            });

            Stage popupStage = new Stage();
            popupStage.setTitle("Filter");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.initOwner(propertiesGrid.getScene().getWindow());
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     // ── Edit button ─────────────────────────────────────────────────────────

    @FXML
    private void handleEdit() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/CategoriesPage.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) propertiesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── DB queries ────────────────────────────────────────────────────────────

    // TODO: Redundant DAO method. Consider using getPropertiesByTest method in service class
    private List<PropertyRow> fetchProperties(Connection conn, int batchCode) throws SQLException {
        String propSql = """
    SELECT
        p.Property_ID,
        p.Property_name,
        p.Test_ID,

        c.Category_name,

        t.Temp_VAL || ' ' || tu.Temp_Unit AS temperature,

        d.Dir_VAL,

        u.Unit

    FROM Property p

    LEFT JOIN Category c
        ON p.Category_ID = c.Category_ID

    LEFT JOIN Temperature t
        ON p.Temp_ID = t.Temp_ID

    LEFT JOIN Temperature_Units tu
        ON t.Temp_Unit_ID = tu.Temp_Unit_ID

    LEFT JOIN Direction d
        ON p.Dir_ID = d.Dir_ID

    LEFT JOIN Units u
        ON p.Unit_ID = u.Unit_ID

    JOIN Batch_Test bt
        ON p.Test_ID = bt.Test_ID

    WHERE bt.Batch_CODE = ?
""";

        List<PropertyRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(propSql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                testId = rs.getInt("Test_ID");
                basePropertiesState.setTestId(testId);

                PropertyRow row = new PropertyRow(
                        rs.getString("Property_name"),
                        rs.getString("Category_name"),
                        rs.getString("temperature"),
                        rs.getString("Dir_VAL"),
                        rs.getString("Unit")
                );
                row.values = fetchValues(conn, rs.getInt("Property_ID"));
                rows.add(row);
            }
        }

        return rows;
    }

    private List<Double> fetchValues(Connection conn, int propertyId) throws SQLException {
        String sql = "SELECT Prop_VAL FROM Property_Values WHERE Property_ID = ?";
        List<Double> values = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, propertyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                values.add(rs.getDouble("Prop_VAL"));
            }
        }

        return values;
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid(List<PropertyRow> rows) {
        propertiesGrid.getChildren().clear();
        propertiesGrid.getColumnConstraints().clear();

        // Filter rows by property visibility
        List<PropertyRow> visibleRows = rows.stream()
                .filter(r -> propertyStates.getOrDefault(r.name, true))
                .toList();

        if (visibleRows.isEmpty()) {
            Label empty = new Label("No properties to display.");
            empty.getStyleClass().add("empty-label");
            propertiesGrid.add(empty, 0, 0);
            return;
        }

        // Build active column headers
        List<String> headers = new ArrayList<>();
        if (columnStates.getOrDefault("Property",      true)) headers.add("Property");
        if (columnStates.getOrDefault("Category",      true)) headers.add("Category");
        if (columnStates.getOrDefault("Temperature",   true)) headers.add("Temperature");
        if (columnStates.getOrDefault("Direction",     true)) headers.add("Direction");


        int maxValues = visibleRows.stream().mapToInt(r -> r.values.size()).max().orElse(0);
        if (columnStates.getOrDefault("Values", true)) {
            for (int i = 0; i < maxValues; i++) {
                headers.add("Value " + (i + 1));
            }
        }
        if (columnStates.getOrDefault("Average Value", true)) headers.add("Average Value");

        for (int i = 0; i < headers.size(); i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(80);
            cc.setPrefWidth(headers.get(i).startsWith("Direction") ? 160 : 120);
            cc.setHgrow(Priority.ALWAYS);
            propertiesGrid.getColumnConstraints().add(cc);
        }

        // Header row
        for (int i = 0; i < headers.size(); i++) {
            propertiesGrid.add(makeHeader(headers.get(i)), i, 0);
        }

        // Data rows
        int row = 1;
        for (PropertyRow p : visibleRows) {
            boolean isAlt = (row % 2 == 0);
            int col = 0;

            if (columnStates.getOrDefault("Property",      true))
                propertiesGrid.add(makeCell(p.name, isAlt), col++, row);
            if (columnStates.getOrDefault("Category",      true))
                propertiesGrid.add(makeCell(p.category, isAlt), col++, row);
            if (columnStates.getOrDefault("Temperature",   true))
                propertiesGrid.add(makeCell(p.temperature != null ? p.temperature : "—", isAlt), col++, row);
            if (columnStates.getOrDefault("Direction",     true))
                propertiesGrid.add(makeCell(p.direction, isAlt), col++, row);


            if (columnStates.getOrDefault("Values", true)) {
                for (int i = 0; i < maxValues; i++) {
                    String val = i < p.values.size()
                            ? formatDouble(p.values.get(i)) + " " + (p.unit != null ? p.unit : "")
                            : "";
                    propertiesGrid.add(makeCell(val, isAlt), col++, row);
                }
            }

            if (columnStates.getOrDefault("Average Value", true))
                propertiesGrid.add(makeCell(
                        formatDouble(p.average()) + " " + (p.unit != null ? p.unit : ""), isAlt), col++, row);

            row++;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatDouble(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((int) value);
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(backDestination));
            Parent root = loader.load();
            Stage stage = (Stage) propertiesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}