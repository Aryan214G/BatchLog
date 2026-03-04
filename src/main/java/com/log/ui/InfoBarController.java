package com.log.ui;

import com.log.core.AppState;
import com.log.core.basePropertiesState;
import com.log.core.SelectedState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InfoBarController {

    @FXML private Button infobar_productName;
    @FXML private Button infobar_batchNo;
    @FXML private Button infobar_propertyName;
    @FXML private Button infobar_attributeName;

    private final AppState state = AppState.getInstance();
    private final basePropertiesState bpropState = basePropertiesState.getInstance();
    private final SelectedState selectedState = SelectedState.getInstance();

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {

        infobar_productName.setText(
                "Product: " + safe(bpropState.getProductName()));

        infobar_batchNo.setText(
                "Batch: " + safe(bpropState.getBatchNo()));

        infobar_propertyName.setText(
                safe(selectedState.getSelectedCategory()));

        infobar_attributeName.setText(
                safe(selectedState.getSelectedProperty()));
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}
