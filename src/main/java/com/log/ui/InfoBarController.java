package com.log.ui;

import com.log.core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InfoBarController {

    @FXML private Button infobar_productName;
    @FXML private Button infobar_batchNo;
    @FXML private Button infobar_propertyName;
    @FXML private Button infobar_attributeName;

    private final AppState state = AppState.getInstance();

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {

        infobar_productName.setText(
                "Product: " + safe(state.getProductName()));

        infobar_batchNo.setText(
                "Batch: " + safe(state.getBatchNo()));

        infobar_propertyName.setText(
                safe(state.getSelectedCategory()));

        infobar_attributeName.setText(
                safe(state.getSelectedProperty()));
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}
