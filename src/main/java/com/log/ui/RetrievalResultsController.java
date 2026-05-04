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

    // ── Row model ─────────────────────────────────────────────────────────────

    private static class PropertyRow {
        String name, category, temperature, direction, unit, value;

        PropertyRow(String name, String category, String temperature,
                    String direction, String unit, String value) {
            this.name        = name;
            this.category    = category;
            this.temperature = temperature;
            this.direction   = direction;
            this.unit        = unit;
            this.value       = value;
        }
    }

    // ── Entry point called by RetrievalPageController ─────────────────────────

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

    // ── DB query — resolves all foreign keys in one JOIN ──────────────────────

    private List<PropertyRow> fetchProperties(Connection conn, int batchCode) throws SQLException {

        String sql = """
            SELECT
                p.Property_name,
                c.Category_name,
                t.Temp_VAL || ' ' || t.Temp_UNIT   AS temperature,
                d.Dir_VAL,
                u.Unit,
                pv.Prop_VAL
            FROM Property p
            LEFT JOIN Category    c  ON p.Category_ID = c.Category_ID
            LEFT JOIN Temperature t  ON p.Temp_ID     = t.Temp_ID
            LEFT JOIN Direction   d  ON p.Dir_ID      = d.Dir_ID
            LEFT JOIN Units       u  ON p.Unit_ID     = u.Unit_ID
            LEFT JOIN Property_Values pv ON pv.Property_ID = p.Property_ID
            WHERE p.Batch_CODE = ?
        """;

        List<PropertyRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(new PropertyRow(
                        rs.getString("Property_name"),
                        rs.getString("Category_name"),
                        rs.getString("temperature"),
                        rs.getString("Dir_VAL"),
                        rs.getString("Unit"),
                        rs.getString("Prop_VAL")
                ));
            }
        }

        return rows;
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid(List<PropertyRow> rows) {
        propertiesGrid.getChildren().clear();
        propertiesGrid.getColumnConstraints().clear();

        String[] headers = {"Property", "Category", "Temperature", "Direction", "Unit", "Value"};

        for (int i = 0; i < headers.length; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / headers.length);
            cc.setHgrow(Priority.ALWAYS);
            propertiesGrid.getColumnConstraints().add(cc);
        }

        // Header row
        for (int i = 0; i < headers.length; i++) {
            propertiesGrid.add(makeHeader(headers[i]), i, 0);
        }

        // Data rows
        int row = 1;
        for (PropertyRow p : rows) {
            boolean isAlt = (row % 2 == 0);  // alternate every other row
            propertiesGrid.add(makeCell(p.name,        isAlt), 0, row);
            propertiesGrid.add(makeCell(p.category,    isAlt), 1, row);
            propertiesGrid.add(makeCell(p.temperature, isAlt), 2, row);
            propertiesGrid.add(makeCell(p.direction,   isAlt), 3, row);
            propertiesGrid.add(makeCell(p.unit,        isAlt), 4, row);
            propertiesGrid.add(makeCell(p.value,       isAlt), 5, row);
            row++;
        }

        if (rows.isEmpty()) {
            Label empty = new Label("No properties found for this batch.");
            empty.getStyleClass().add("empty-label");
            propertiesGrid.add(empty, 0, 1, headers.length, 1);
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

    private Label makeCell(String text) {
        Label label = new Label(text != null ? text : "—");  // null fallback for LEFT JOINs
        label.getStyleClass().add("grid-cell");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    // ── Back button ───────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalPage.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) propertiesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}