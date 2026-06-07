package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.core.SelectedState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InfoBarController {

    @FXML private Button infobar_projectName;
    @FXML private Button infobar_productName;
    @FXML private Button infobar_batchNo;
    @FXML private Button infobar_testDate;
    @FXML private Button infobar_testSite;
    @FXML private Button infobar_propertyName;
    @FXML private Button infobar_attributeName;

    private final AppState state = AppState.getInstance();
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private final SelectedState selectedState = SelectedState.getInstance();

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {

        infobar_projectName.setText(
                "Project: " + safe(bpropState.getProjectName()));

        infobar_productName.setText(
                "Product: " + safe(bpropState.getProductName()));

        infobar_batchNo.setText(
                "Batch: " + safe(bpropState.getBatchNo()));

        infobar_testDate.setText(
                "Date: " + (bpropState.getTestDate() != null
                        ? bpropState.getTestDate().toString()
                        : "-"));

        infobar_testSite.setText(
                "Site: " + safe(bpropState.getPlaceOfTesting()));

        infobar_propertyName.setText(
                safe(selectedState.getSelectedCategory()));

        String propertyName = selectedState.getSelectedProperty() == null
                ? null
                : selectedState.getSelectedProperty().getPropertyName();

        infobar_attributeName.setText(
                safe(propertyName));
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}