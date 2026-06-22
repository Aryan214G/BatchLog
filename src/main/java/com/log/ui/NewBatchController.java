package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.ProductService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class NewBatchController {

    @FXML private TextField productField;
    @FXML private TextField batchNo;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField componentIdField;
    @FXML private TextField sopField;
    @FXML private TextField testSchedulefield;

    private final BasePropertiesState bpropState =
            BasePropertiesState.getInstance();

    private final AppState appState =
            AppState.getInstance();

    private final ProductService productService =
            new ProductService();

    private final BatchService batchService =
            new BatchService();

    private final BatchTestService batchTestService =
            new BatchTestService();

    @FXML
    private void handleNext() {

        if (!validateInputs()) {

            AlertUtil.showWarning(
                    "Please fill in all required fields."
            );

            return;
        }

        bpropState.setProductName(
                productField.getText().trim()
        );

        bpropState.setBatchNo(
                batchNo.getText().trim()
        );

        bpropState.setTestDate(
                testDate.getValue()
        );

        bpropState.setPlaceOfTesting(
                placeOfTesting.getText().trim()
        );

        bpropState.setSop(
                sopField.getText().trim()
        );

        bpropState.setProductID(
                componentIdField.getText().trim()
        );

        bpropState.setTestSchedule(
                testSchedulefield.getText().trim()
        );

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();

            conn.setAutoCommit(false);

            productService.createProduct(
                    conn,
                    bpropState.getProductID(),
                    bpropState.getProductName()
            );

            int productCode =
                    bpropState.getProductCode();

            Integer batchCode = null;

            if (!batchNo.getText().isBlank()) {

                Batch batch = new Batch(
                        bpropState.getBatchNo(),
                        productCode
                );

                batchService.createBatch(
                        conn,
                        batch
                );

                batchCode =
                        bpropState.getBatchCode();
            }

            BatchTest batchTest =
                    new BatchTest(
                            batchCode,
                            bpropState.getTestDate().toString(),
                            bpropState.getPlaceOfTesting(),
                            productCode,
                            bpropState.getSop(),
                            bpropState.getTestSchedule()
                    );

            batchTestService.createBatchTest(
                    conn,
                    batchTest
            );

            conn.commit();

            appState.setProjectCreated(true);

            AlertUtil.showInfo(
                    "Batch created successfully."
            );

            loadCategoriesPage();

        } catch (Exception e) {

            e.printStackTrace();

            if (conn != null) {

                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

            AlertUtil.showError(
                    "Failed to create batch."
            );

        } finally {

            if (conn != null) {

                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean validateInputs() {

        if (productField.getText().isEmpty())
            return false;

        if (placeOfTesting.getText().isEmpty())
            return false;

        return testDate.getValue() != null;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {

        Stage stage =
                (Stage) batchNo
                        .getScene()
                        .getWindow();

        stage.close();
    }
    private void loadCategoriesPage() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/CategoriesPage.fxml")
            );
            Parent root = loader.load();
            batchNo.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}