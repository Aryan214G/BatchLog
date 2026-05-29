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
import java.util.*;

public class BatchComparisonController {

    @FXML private GridPane comparisonGrid;
    @FXML private Label titleLabel;

    // batchCode → propertyName → average value
    private Map<Integer, Map<String, Double>> data = new LinkedHashMap<>();

    // All unique property names across all batches
    private List<String> allProperties = new ArrayList<>();

    // BatchTest list for display
    private List<BatchTest> batches;

    public void loadComparison(List<BatchTest> batches) {
        this.batches = batches;
        titleLabel.setText("Batch Comparison — " + batches.size() + " batches selected");

        try (Connection conn = DBUtil.getConnection()) {
            fetchData(conn);
            populateGrid();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── DB query ──────────────────────────────────────────────────────────────

    private void fetchData(Connection conn) throws SQLException {
        Set<String> propertySet = new LinkedHashSet<>();

        for (BatchTest batch : batches) {
            Map<String, Double> propertyAverages = new LinkedHashMap<>();

            String sql = """
                SELECT
                    p.Property_name,
                    AVG(pv.Prop_VAL) AS avg_val
                FROM Property p
                JOIN Property_Values pv ON pv.Property_ID = p.Property_ID
                JOIN Batch_Test bt      ON p.Test_ID      = bt.Test_ID
                WHERE bt.Batch_CODE = ?
                GROUP BY p.Property_name
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, batch.getBatchCode());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("Property_name");
                    double avg  = rs.getDouble("avg_val");
                    propertyAverages.put(name, avg);
                    propertySet.add(name);
                }
            }

            data.put(batch.getBatchCode(), propertyAverages);
        }

        allProperties = new ArrayList<>(propertySet);
    }

    // ── Grid rendering ────────────────────────────────────────────────────────

    private void populateGrid() {
        comparisonGrid.getChildren().clear();
        comparisonGrid.getColumnConstraints().clear();

        // Columns: Batch/Component ID + one per property
        int totalCols = 1 + allProperties.size();

        for (int i = 0; i < totalCols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(100);
            cc.setPrefWidth(140);
            cc.setHgrow(Priority.ALWAYS);
            comparisonGrid.getColumnConstraints().add(cc);
        }

        // Header row — first cell is "Batch/Component ID", rest are property names
        comparisonGrid.add(makeHeader("Batch / Component ID"), 0, 0);
        for (int i = 0; i < allProperties.size(); i++) {
            comparisonGrid.add(makeHeader(allProperties.get(i)), i + 1, 0);
        }

        // Data rows — one per batch
        int row = 1;
        for (BatchTest batch : batches) {
            boolean isAlt = (row % 2 == 0);
            Map<String, Double> props = data.getOrDefault(batch.getBatchCode(), Collections.emptyMap());

            // First cell — batch identifier
            comparisonGrid.add(makeCell("Batch " + batch.getBatchCode(), isAlt), 0, row);

            // Property cells — average value or "—" if not present
            for (int i = 0; i < allProperties.size(); i++) {
                String propName = allProperties.get(i);
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