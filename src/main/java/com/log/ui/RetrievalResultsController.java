package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.dto.ReportData;
import com.log.dto.RetrievalTableReportData;
import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.model.PropertyRow;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.CategoryService;
import com.log.service.export.ReportGenerator;
import com.log.service.export.PropertyReportService;
import com.log.service.export.RetrievalTableReportService;
import com.log.util.AlertUtil;
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

import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RetrievalResultsController {

    @FXML private Label batchTitleLabel;
    @FXML private GridPane propertiesGrid;

    private List<PropertyRow> cachedRows = new ArrayList<>();

    private String backDestination = "/com/log/ui/views/RetrievalPage.fxml";

    private boolean groupByCategory = false;



    @FXML
    private void handleToggleGrouping() {
        groupByCategory = !groupByCategory;
        populateGrid(cachedRows);
    }

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
    private CategoryService categoryService = new CategoryService();

    private int testId;

// ============== SERVICES =============================================

    private BatchTestService batchTestService = new BatchTestService();
    private BatchService batchService = new BatchService();

    // REPORT SERVICES
    private PropertyReportService propertyReportService = new PropertyReportService();
    private ReportGenerator<RetrievalTableReportData> rtReportService = new RetrievalTableReportService();


    // ── Entry point ───────────────────────────────────────────────────────────
    // Add this import


    // Add this overload alongside the existing loadBatch(BatchTest)
    public void loadBatch(BatchTest batch) {
        batchTitleLabel.setText("Test ID: " + batch.getTestId());

        try (Connection conn = DBUtil.getConnection()) {
            // Use Test_ID directly since product tests have no Batch_CODE
            cachedRows = fetchPropertiesByTestId(conn, batch.getTestId());

            propertyStates.clear();
            for (PropertyRow r : cachedRows) {
                propertyStates.put(r.getName(), true);
            }

            appState.setProjectCreated(true);
            populateGrid(cachedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<PropertyRow> sortByCategoryOrder(List<PropertyRow> rows) {
        categoryService.refreshCategoriesState();
        List<String> categoryOrder = appState.getCategories();

        // Build a category index map for fast lookup
        Map<String, Integer> categoryIndex = new LinkedHashMap<>();
        for (int i = 0; i < categoryOrder.size(); i++) {
            categoryIndex.put(categoryOrder.get(i), i);
        }

        return rows.stream()
                .sorted(Comparator.comparingInt(r -> {
                    String cat = r.getCategory();
                    return cat != null ? categoryIndex.getOrDefault(cat, Integer.MAX_VALUE) : Integer.MAX_VALUE;
                }))
                .toList();
    }

    // New query — fetches properties across ALL tests in the batch
    private List<PropertyRow> fetchPropertiesByTestId(Connection conn, int testId) throws SQLException {
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
        LEFT JOIN Category c           ON p.Category_ID    = c.Category_ID
        LEFT JOIN Temperature t        ON p.Temp_ID        = t.Temp_ID
        LEFT JOIN Temperature_Units tu ON t.Temp_Unit_ID   = tu.Temp_Unit_ID
        LEFT JOIN Direction d          ON p.Dir_ID         = d.Dir_ID
        LEFT JOIN Units u              ON p.Unit_ID        = u.Unit_ID
        WHERE p.Test_ID = ?
    """;

        List<PropertyRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(propSql)) {
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                basePropertiesState.setTestId(rs.getInt("Test_ID"));
                PropertyRow row = new PropertyRow(
                        rs.getInt("Property_ID"),
                        rs.getString("Property_name"),
                        rs.getString("Category_name"),
                        rs.getString("temperature"),
                        rs.getString("Dir_VAL"),
                        rs.getString("Unit")
                );
                row.setValues(fetchValues(conn, rs.getInt("Property_ID")));
                rows.add(row);
            }
        }

        return rows;
    }
    public void loadBatch(Batch batch) {
        batchTitleLabel.setText("Batch: " + batch.getBatchId());

        try (Connection conn = DBUtil.getConnection()) {
            cachedRows = fetchPropertiesForBatch(conn, batch.getBatchCode());

            propertyStates.clear();
            for (PropertyRow r : cachedRows) {
                propertyStates.put(r.getName(), true);
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

        List<PropertyRow> visibleRows = rows.stream()
                .filter(r -> propertyStates.getOrDefault(r.getName(), true))
                .toList();

        if (groupByCategory) {
            visibleRows = sortByCategoryOrder(visibleRows);
        }

        if (visibleRows.isEmpty()) {
            Label empty = new Label("No properties to display.");
            empty.getStyleClass().add("empty-label");
            propertiesGrid.add(empty, 0, 0);
            return;
        }

        // Build active column headers
        List<String> headers = new ArrayList<>();
        if (columnStates.getOrDefault("Property",      true)) headers.add("Property");
        if (!groupByCategory && columnStates.getOrDefault("Category", true)) headers.add("Category");
        if (columnStates.getOrDefault("Temperature",   true)) headers.add("Temperature");
        if (columnStates.getOrDefault("Direction",     true)) headers.add("Direction");

        int maxValues = visibleRows.stream().mapToInt(r -> r.getValues().size()).max().orElse(0);
        if (columnStates.getOrDefault("Values", true)) {
            for (int i = 0; i < maxValues; i++) headers.add("Value " + (i + 1));
        }
        if (columnStates.getOrDefault("Average Value", true)) headers.add("Average Value");

        // Column constraints — grow to fill available width
        for (String header : headers) {
            ColumnConstraints cc = new ColumnConstraints();
            switch (header) {
                case "Property"     -> { cc.setMinWidth(150); cc.setPrefWidth(250); }
                case "Direction"    -> { cc.setMinWidth(120); cc.setPrefWidth(180); }
                case "Temperature"  -> { cc.setMinWidth(80);  cc.setPrefWidth(120); }
                default             -> { cc.setMinWidth(60);  cc.setPrefWidth(90);  }
            }
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            propertiesGrid.getColumnConstraints().add(cc);
        }

        // Header row
        for (int i = 0; i < headers.size(); i++) {
            propertiesGrid.add(makeHeader(headers.get(i)), i, 0);
        }

        // Data rows
        int row = 1;
        String lastCategory = null;

        for (PropertyRow p : visibleRows) {
            boolean isAlt = (row % 2 == 0);
            int col = 0;

            // Category divider when grouped
            if (groupByCategory) {
                String currentCategory = p.getCategory() != null ? p.getCategory() : "—";
                if (!currentCategory.equals(lastCategory)) {
                    propertiesGrid.add(
                            makeCategoryDivider(currentCategory, headers.size()),
                            0, row
                    );
                    row++;
                    lastCategory = currentCategory;
                    isAlt = false;
                }
            }

            ContextMenu rowMenu = createRowContextMenu(p);

            String displayName = p.getName();

            if (p.getUnit() != null
                    && !p.getUnit().isBlank()
                    && !"Not Applicable".equalsIgnoreCase(p.getUnit())) {

                displayName += " (" + p.getUnit() + ")";
            }

            propertiesGrid.add(
                    makeCell(displayName, isAlt, rowMenu),
                    col++,
                    row
            );
            if (!groupByCategory && columnStates.getOrDefault("Category", true))
                propertiesGrid.add(makeCell(p.getCategory(), isAlt, rowMenu), col++, row);
            if (columnStates.getOrDefault("Temperature", true))
                propertiesGrid.add(makeCell(p.getTemperature() != null ? p.getTemperature() : "—", isAlt, rowMenu), col++, row);
            if (columnStates.getOrDefault("Direction", true))
                propertiesGrid.add(makeCell(p.getDirection(), isAlt, rowMenu), col++, row);

            if (columnStates.getOrDefault("Values", true)) {
                for (int i = 0; i < maxValues; i++) {
                    String val = i < p.getValues().size() ? formatDouble(p.getValues().get(i)) + " " : "";
                    propertiesGrid.add(makeCell(val, isAlt, rowMenu), col++, row);
                }
            }

            if (columnStates.getOrDefault("Average Value", true))
                propertiesGrid.add(makeCell(formatDouble(p.getAverage()), isAlt, rowMenu), col++, row);

            row++;
        }
    }

    private Label makeCategoryDivider(String categoryName, int colSpan) {
        Label label = new Label(categoryName);

        label.getStyleClass().add("category-divider");

        label.setMaxWidth(Double.MAX_VALUE);
        GridPane.setFillWidth(label, true);
        GridPane.setFillHeight(label, true);
        GridPane.setColumnSpan(label, colSpan);

        return label;
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
        label.setPrefWidth(Double.MAX_VALUE);  // add this
        label.setWrapText(true);
        return label;
    }

    private Label makeCell(String text, boolean isAlt, ContextMenu menu) {

        Label label = new Label(text != null ? text : "—");

        label.getStyleClass().add("grid-cell");

        if (isAlt)
            label.getStyleClass().add("grid-cell-alt");

        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(Double.MAX_VALUE);
        label.setWrapText(true);

        label.setContextMenu(menu);

        return label;
    }

    // ── Back ──────────────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(backDestination));
            Parent root = loader.load();

            // If going back to ProjectPage, reload the project
            if (backDestination.contains("ProjectPage")) {
                ProjectPageController controller = loader.getController();
                controller.loadProject(basePropertiesState.getProjectName());
            }

            Stage stage = (Stage) propertiesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ContextMenu createRowContextMenu(PropertyRow property) {

        MenuItem exportItem = new MenuItem("Print property report");
        exportItem.setOnAction(e -> {
            System.out.println("Export: " + property.getName());
            try {
                handleExport(property.getPropertyId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (PrinterException ex) {
                throw new RuntimeException(ex);
            }
        });
        MenuItem editItem = new MenuItem("Edit Property");
        editItem.setOnAction(e -> handleEditProperty(property));

        return new ContextMenu(exportItem,editItem);
    }

    private void handleEditProperty(PropertyRow property) {
        appState.setEditMode(true);
        appState.setEditPropertyId(property.getPropertyId());
        appState.setProjectCreated(true);

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

    private void handleExport(int propertyId)
            throws SQLException, IOException, PrinterException {

        ReportData propertyReport =
                propertyReportService.buildReportData(propertyId);

        Thread printThread = new Thread(() -> {
            try {
                propertyReportService.generateReport(propertyReport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        printThread.setDaemon(true);
        printThread.start();
    }

    private List<PropertyRow> fetchPropertiesForBatch(Connection conn, int batchCode) throws SQLException {
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
        LEFT JOIN Category c           ON p.Category_ID    = c.Category_ID
        LEFT JOIN Temperature t        ON p.Temp_ID        = t.Temp_ID
        LEFT JOIN Temperature_Units tu ON t.Temp_Unit_ID   = tu.Temp_Unit_ID
        LEFT JOIN Direction d          ON p.Dir_ID         = d.Dir_ID
        LEFT JOIN Units u              ON p.Unit_ID        = u.Unit_ID
        JOIN Batch_Test bt             ON p.Test_ID        = bt.Test_ID
        WHERE bt.Batch_CODE = ?
        ORDER BY p.Test_ID, p.Property_name
    """;

        List<PropertyRow> rows = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(propSql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                testId = rs.getInt("Test_ID");
                basePropertiesState.setTestId(testId);

                PropertyRow row = new PropertyRow(
                        rs.getInt("Property_ID"),
                        rs.getString("Property_name"),
                        rs.getString("Category_name"),
                        rs.getString("temperature"),
                        rs.getString("Dir_VAL"),
                        rs.getString("Unit")
                );
                row.setPropertyId(rs.getInt("Property_ID"));
                row.setValues(fetchValues(conn, rs.getInt("Property_ID")));
                rows.add(row);
            }
        }

        return rows;
    }

    @FXML
    private void handlePrint() throws Exception {

        List<PropertyRow> visibleRows = cachedRows.stream()
                .filter(r -> propertyStates.getOrDefault(r.getName(), true))
                .toList();

        if (visibleRows.isEmpty()) {
            return;
        }

        // ── Unit consistency check ────────────────────────────────────────────────
        Map<String, String> propertyUnits = new LinkedHashMap<>();
        List<String> inconsistentProperties = new ArrayList<>();

        for (PropertyRow p : visibleRows) {
            String unit = p.getUnit() != null ? p.getUnit() : "";
            String existingUnit = propertyUnits.get(p.getName());

            if (existingUnit == null) {
                propertyUnits.put(p.getName(), unit);
            } else if (!existingUnit.equals(unit)) {
                if (!inconsistentProperties.contains(p.getName())) {
                    inconsistentProperties.add(p.getName());
                }
            }
        }

        if (!inconsistentProperties.isEmpty()) {
            String propertyList = String.join(", ", inconsistentProperties);
            AlertUtil.showError(
                    "Cannot export: the following properties have inconsistent units across entries:\n\n"
                            + propertyList
                            + "\n\nPlease ensure all entries for the same property use the same unit."
            );
            return;
        }
        // ─────────────────────────────────────────────────────────────────────────

        int maxValues = visibleRows.stream()
                .mapToInt(r -> r.getValues().size())
                .max()
                .orElse(0);

        // Build headers respecting column filters
        List<String> headers = new ArrayList<>();
        if (columnStates.getOrDefault("Property",      true)) headers.add("Property");
        if (columnStates.getOrDefault("Temperature",   true)) headers.add("Temperature");
        if (columnStates.getOrDefault("Direction",     true)) headers.add("Direction");
        if (columnStates.getOrDefault("Values",        true)) {
            for (int i = 0; i < maxValues; i++) headers.add("Value " + (i + 1));
        }
        if (columnStates.getOrDefault("Average Value", true)) headers.add("Average Value");

        // Build rows respecting column filters
        Map<String, List<List<String>>> groupedTableData = new LinkedHashMap<>();

        for (PropertyRow p : visibleRows) {
            List<String> rowData = new ArrayList<>();

            if (columnStates.getOrDefault("Property",      true))
                rowData.add(p.getName() + (p.getUnit() != null ? " (" + p.getUnit() + ")" : ""));
            if (columnStates.getOrDefault("Temperature",   true))
                rowData.add(p.getTemperature() != null ? p.getTemperature() : "—");
            if (columnStates.getOrDefault("Direction",     true))
                rowData.add(p.getDirection() != null ? p.getDirection() : "—");
            if (columnStates.getOrDefault("Values",        true)) {
                for (int i = 0; i < maxValues; i++) {
                    rowData.add(i < p.getValues().size() ? formatDouble(p.getValues().get(i)) : "");
                }
            }
            if (columnStates.getOrDefault("Average Value", true))
                rowData.add(formatDouble(p.getAverage()));

            groupedTableData
                .computeIfAbsent(
                        p.getCategory(),
                        k -> new ArrayList<>()
                )
                .add(rowData);
        }

        System.out.println(headers);
        System.out.println(headers);

        groupedTableData.forEach(
                (category, rows) -> {

                    System.out.println(category);

                    rows.forEach(System.out::println);
                }
        );
        System.out.println(basePropertiesState.getProductID());
        RetrievalTableReportData data =
        new RetrievalTableReportData(
                headers,
                groupedTableData,
                basePropertiesState.getProjectName(),
                basePropertiesState.getSop(),
                basePropertiesState.getProductName(),
                basePropertiesState.getProductID(),
                basePropertiesState.getBatchNo(),
                basePropertiesState.getTestSchedule()
        );

    rtReportService.generateReport(data);
    }

    @FXML private Button groupToggleBtn;




}