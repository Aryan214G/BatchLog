package com.log.ui;

import com.log.model.Category;
import com.log.model.DefaultProperty;
import com.log.service.CategoryService;
import com.log.service.DefaultPropertyService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class Catrenamesettingspagecontroller {

    private final CategoryService catserv = new CategoryService();
    private final DefaultPropertyService defpropserv =
            new DefaultPropertyService();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML
    private ListView<Category> categoryList;

    @FXML
    private ListView<DefaultProperty> propertyList;

    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField propertyNameField;

    @FXML
    public void initialize() {

        loadCategories();

        categoryList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    if (newVal != null) {
                        loadProperties(
                                newVal.getCategoryId()
                        );
                    }
                });
    }

    @FXML
    private void handleRenameCategory() {

        Category selected =
                categoryList.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            alertUtil.showWarning(
                    "Please select a category."
            );
            return;
        }

        String newValue =
                categoryNameField.getText();

        if (newValue == null ||
                newValue.trim().isBlank()) {

            alertUtil.showWarning(
                    "Please enter a new category name."
            );
            return;
        }

        newValue = newValue.trim();

        if (newValue.equals(
                selected.getCategoryName())) {

            alertUtil.showWarning(
                    "No changes detected."
            );
            return;
        }

        int categoryId =
                selected.getCategoryId();

        catserv.renamecategory(
                categoryId,
                newValue
        );

        loadCategories();

        // restore selection
        for (Category c : categoryList.getItems()) {

            if (c.getCategoryId() ==
                    categoryId) {

                categoryList.getSelectionModel()
                        .select(c);

                loadProperties(
                        c.getCategoryId()
                );

                break;
            }
        }

        categoryNameField.clear();

        alertUtil.showInfo(
                "Rename Category Successful"
        );
    }

    @FXML
    private void handleRenameProperty() {

        DefaultProperty selectedProp =
                propertyList.getSelectionModel()
                        .getSelectedItem();

        if (selectedProp == null) {

            alertUtil.showWarning(
                    "Please select a property."
            );

            return;
        }

        String newName =
                propertyNameField.getText();

        if (newName == null ||
                newName.trim().isBlank()) {

            alertUtil.showWarning(
                    "Please enter a property name."
            );

            return;
        }

        newName = newName.trim();

        if (newName.equals(
                selectedProp.getPropertyName())) {

            alertUtil.showWarning(
                    "No changes detected."
            );

            return;
        }

        defpropserv.renamedefpropname(
                selectedProp.getPropertyId(),
                newName
        );

        Category currentCategory =
                categoryList.getSelectionModel()
                        .getSelectedItem();

        if (currentCategory != null) {

            loadProperties(
                    currentCategory.getCategoryId()
            );
        }

        propertyNameField.clear();

        alertUtil.showInfo(
                "Rename Property Successful"
        );
    }

    @FXML
    private void handleClose() {

    }

    private void loadCategories() {

        categoryList.getItems().setAll(
                catserv.getAllCategories()
        );
    }

    private void loadProperties(
            Integer categoryId) {

        if (categoryId == null) {
            return;
        }

        List<DefaultProperty> properties =
                defpropserv.getPropertiesByCategory(
                        categoryId
                );

        propertyList.getItems()
                .setAll(properties);
    }
}