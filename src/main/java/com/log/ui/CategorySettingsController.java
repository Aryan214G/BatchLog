package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.Category;
import com.log.model.DefaultProperty;
import com.log.model.Unit;
import com.log.service.CategoryService;
import com.log.service.DefaultPropertyService;
import com.log.service.PropertyService;
import com.log.service.UnitsService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    private final CategoryService categoryService =
            new CategoryService();

    private final UnitsService unitService =
            new UnitsService();

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
            System.out.println("Category name cannot be empty");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            Category existing =
                    categoryService.getCategory(
                            conn,
                            categoryName
                    );

            if (existing != null) {
                System.out.println("Category already exists");
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
            System.out.println(
                    "Property name cannot be empty"
            );
            return;
        }

        String selectedCategory =
                categoryList.getSelectionModel()
                        .getSelectedItem();

        if (selectedCategory == null) {
            System.out.println(
                    "Select a category first"
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

                    System.out.println(
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

                System.out.println(
                        "Category not found"
                );

                return;
            }

            int rows = 1; // replace with rows field later

            System.out.println("========== INSERTING PROPERTY ==========");
            System.out.println("Property Name = " + propertyName);
            System.out.println("Category ID   = " + category.getCategoryId());
            System.out.println("Unit ID       = " + unitId);
            System.out.println("Rows          = " + rows);
            defaultPropertyService
                    .createDefaultProperty(
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
                            ")"
            );

            propertyField.clear();
            customUnitField.clear();
            unitComboBox.setValue(null);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}