package com.log.ui;

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
import java.util.List;

public class RetrievalResultsController {

    @FXML private Label batchTitleLabel;
    @FXML private GridPane propertiesGrid;

    private String backDestination = "/com/log/ui/views/RetrievalPage.fxml";

    public void setBackDestination(String path) {
        this.backDestination = path;
    }

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
            List<PropertyRow> rows = fetchProperties(conn, batch.getBatchCode());
            populateGrid(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    private List<PropertyRow> fetchProperties(Connection conn, int batchCode) throws SQLException {

        // First fetch all properties for the batch
        String propSql = """
            SELECT
                p.Property_ID,
                p.Property_name,
                c.Category_name,
                t.Temp_VAL || ' ' || t.Temp_UNIT AS temperature,
                d.Dir_VAL,
                u.Unit
            FROM Property p
            LEFT JOIN Category    c  ON p.Category_ID = c.Category_ID
            LEFT JOIN Temperature t  ON p.Temp_ID     = t.Temp_ID
            LEFT JOIN Direction   d  ON p.Dir_ID      = d.Dir_ID
            LEFT JOIN Units       u  ON p.Unit_ID     = u.Unit_ID
            WHERE p.Batch_CODE = ?
        """;

        List<PropertyRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(propSql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PropertyRow row = new PropertyRow(
                        rs.getString("Property_name"),
                        rs.getString("Category_name"),
                        rs.getString("temperature"),
                        rs.getString("Dir_VAL"),
                        rs.getString("Unit")
                );

                // Fetch values for this property
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

        if (rows.isEmpty()) {
            Label empty = new Label("No properties found for this batch.");
            empty.getStyleClass().add("empty-label");
            propertiesGrid.add(empty, 0, 0);
            return;
        }

        // Find max number of values across all properties for dynamic columns
        int maxValues = rows.stream()
                .mapToInt(r -> r.values.size())
                .max()
                .orElse(0);

        // Fixed columns: Property, Category, Temperature, Direction, Average Value
        // Then dynamic value columns: Value 1, Value 2, ...
        int fixedCols  = 5;
        int totalCols  = fixedCols + maxValues;

        for (int i = 0; i < totalCols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(100);
            cc.setPrefWidth(120);  // give each column a preferred width
            cc.setHgrow(Priority.ALWAYS);
            propertiesGrid.getColumnConstraints().add(cc);
        }
        // ── Header row ────────────────────────────────────────────────────────
        propertiesGrid.add(makeHeader("Property"),       0, 0);
        propertiesGrid.add(makeHeader("Category"),       1, 0);
        propertiesGrid.add(makeHeader("Temperature"), 2, 0);
        propertiesGrid.add(makeHeader("Direction"),      3, 0);
        propertiesGrid.add(makeHeader("Average Value"),  4, 0);

        for (int i = 0; i < maxValues; i++) {
            propertiesGrid.add(makeHeader("Value " + (i + 1)), fixedCols + i, 0);
        }

        // ── Data rows ─────────────────────────────────────────────────────────
        int row = 1;
        for (PropertyRow p : rows) {
            boolean isAlt = (row % 2 == 0);

            String tempDisplay = p.temperature != null ? p.temperature : "—";

            propertiesGrid.add(makeCell(p.name,        isAlt), 0, row);
            propertiesGrid.add(makeCell(p.category,    isAlt), 1, row);
            propertiesGrid.add(makeCell(tempDisplay,   isAlt), 2, row);
            propertiesGrid.add(makeCell(p.direction,   isAlt), 3, row);
            propertiesGrid.add(makeCell(formatDouble(p.average()) + " " + (p.unit != null ? p.unit : ""), isAlt), 4, row);

            // Fill ALL value columns, empty for missing values
            for (int i = 0; i < maxValues; i++) {
                String val = i < p.values.size()
                        ? formatDouble(p.values.get(i)) + " " + (p.unit != null ? p.unit : "")
                        : "";
                propertiesGrid.add(makeCell(val, isAlt), fixedCols + i, row);
            }

            row++;
        }
    }

    private String formatDouble(double value) {
        // Show as integer if whole number, otherwise 2 decimal places
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
        label.setWrapText(true);  // add this
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