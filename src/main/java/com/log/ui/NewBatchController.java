package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.dao.ProductDAO;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.model.Product;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.ProductService;
import com.log.service.ProjectService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class NewBatchController {

    @FXML private TextField productField;
    @FXML private TextField batchNo;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField sopField;  // add this field

    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private final AppState appState = AppState.getInstance();
    private final ProductDAO productDAO = new ProductDAO();
    private final ProductService productService = new ProductService();
    private final BatchService batchService = new BatchService();
    private final BatchTestService batchTestService = new BatchTestService();
    @FXML
    private void handleNext() {

        if (!validateInputs()) {

            AlertUtil.showWarning(
                    "Please fill in all required fields."
            );

            return;
        }

        // Store form values in state
        bpropState.setProductName(productField.getText().trim());
        bpropState.setBatchNo(batchNo.getText().trim());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText().trim());
        bpropState.setSop(sopField.getText().trim());

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. Create product if it doesn't exist
            productService.createProduct(
                    conn,
                    bpropState.getProductName(),  // productId
                    bpropState.getProductName()   // productName
            );

                int productCode =
                        bpropState.getProductCode();

                // -------------------------------------------------
                // Optional Batch
                // -------------------------------------------------

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

                // -------------------------------------------------
                // Batch Test
                // -------------------------------------------------

                BatchTest batchTest =
                        new BatchTest(
                                batchCode,
                                bpropState.getTestDate().toString(),
                                bpropState.getPlaceOfTesting(),
                                productCode,
                                bpropState.getSop()
                        );

                batchTestService.createBatchTest(
                        conn,
                        batchTest
                );

                conn.commit();

                System.out.println(
                        "Created BatchCode = "
                                + batchCode
                                + " | TestId = "
                                + bpropState.getTestId()
                );

                appState.setProjectCreated(true);

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
            AlertUtil.showError("Failed to create batch. Please try again.");

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
        if (productField.getText().isEmpty()) return false;
        if (placeOfTesting.getText().isEmpty()) return false;
        if (testDate.getValue() == null) return false;
        return true;
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

    @FXML
    private void handleCancel() {
        loadCategoriesPage();
    }
}