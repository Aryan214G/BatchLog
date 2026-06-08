package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.Category;
import com.log.model.DefaultProperty;
import com.log.model.Unit;
import com.log.service.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.log.util.AlertUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class CategorySettingsController {

    @FXML
    private TextField categoryField;

    @FXML
    private TextField propertyField;

    @FXML
    private TextField customUnitField;

    @FXML
    private ComboBox<Unit> unitComboBox;

    @FXML
    private ListView<String> categoryList;

    @FXML
    private ListView<String> propertyList;

    @FXML
    private TextField rowsField;


    private final CategoryService categoryService =
            new CategoryService();

    private final UnitsService unitService =
            new UnitsService();

    private final AlertUtil alertUtil = new AlertUtil();

    private final DefaultPropertyService defaultPropertyService=new DefaultPropertyService();

    @FXML
    public void initialize() {

        loadUnits();
        loadCategories();
    }

    private void loadUnits() {

        unitComboBox.setItems(
                FXCollections.observableList(
                        unitService.getAllUnits()
                )
        );
    }

    private void loadCategories() {

        categoryList.getItems().clear();

        categoryService.getAllCategories()
                .forEach(c ->
                        categoryList.getItems()
                                .add(c.getCategoryName()));
    }

    @FXML
    private void handleAddCategory() {

        String categoryName =
                categoryField.getText().trim();

        if (categoryName.isBlank()) {
            alertUtil.showWarning("Category name cannot be empty");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            Category existing =
                    categoryService.getCategory(
                            conn,
                            categoryName
                    );

            if (existing != null) {
                alertUtil.showWarning("Category already exists");
                return;
            }

            categoryService.createCategory(conn, categoryName);

            categoryList.getItems()
                    .add(categoryName);

            categoryField.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddProperty() {

        String propertyName =
                propertyField.getText().trim();

        if (propertyName.isBlank()) {
            alertUtil.showWarning(
                    "Property name cannot be empty"
            );
            return;
        }

        String selectedCategory =
                categoryList.getSelectionModel()
                        .getSelectedItem();

        if (selectedCategory == null) {
            alertUtil.showWarning(
                    "Select a category first"
            );
            return;
        }

        int rows;

        try {

            rows = Integer.parseInt(
                    rowsField.getText().trim()
            );

            if (rows <= 0) {

                alertUtil.showWarning(
                        "Rows must be greater than 0"
                );

                return;
            }

        } catch (NumberFormatException e) {

            alertUtil.showWarning(
                    "Enter a valid row count"
            );

            return;
        }

        try (Connection conn =
                     DBUtil.getConnection()) {

            int unitId;
            String unitName;

            if (!customUnitField.getText()
                    .trim()
                    .isBlank()) {

                unitName =
                        customUnitField.getText().trim();

                unitId =
                        unitService.createUnitIfNotExists(
                                conn,
                                unitName
                        );

                loadUnits();

            } else {

                Unit selectedUnit =
                        unitComboBox.getValue();

                if (selectedUnit == null) {

                    alertUtil.showWarning(
                            "Select a unit"
                    );

                    return;
                }

                unitId =
                        selectedUnit.getUnitId();

                unitName =
                        selectedUnit.getUnit();
            }

            Category category =
                    categoryService.getCategory(
                            conn,
                            selectedCategory
                    );

            if (category == null) {

                alertUtil.showWarning(
                        "Category not found"
                );

                return;
            }

            if (defaultPropertyService.propertyExists(
                    conn,
                    propertyName,
                    category.getCategoryId()
            )) {

                alertUtil.showWarning(
                        "Property already exists"
                );

                return;
            }

            System.out.println(
                    "Creating property with rows = "
                            + rows
            );

            defaultPropertyService
                    .createPropertyWithDefaultUnit(
                            conn,
                            propertyName,
                            category.getCategoryId(),
                            unitId,
                            rows
                    );

            propertyList.getItems().add(
                    propertyName +
                            " (" +
                            unitName +
                            ", Rows=" +
                            rows +
                            ")"
            );

            propertyField.clear();
            customUnitField.clear();
            rowsField.clear();
            unitComboBox.setValue(null);

            alertUtil.showInfo(
                    "Property added successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();

            alertUtil.showError(
                    "Failed to add property"
            );
        }
    }
}