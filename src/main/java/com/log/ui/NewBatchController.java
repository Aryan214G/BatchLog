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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class NewBatchController {

    @FXML private TextField productField;
    @FXML private TextField batchNo;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField fileName;

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

        bpropState.setFileName(
                fileName.getText().trim()
        );

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // -------------------------------------------------
                // Product
                // -------------------------------------------------

                productService.createProduct(
                        conn,
                        bpropState.getProductName(),
                        bpropState.getProductName()
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
                                productCode
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

                conn.rollback();

                e.printStackTrace();

                AlertUtil.showError(
                        "Failed to create test."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Database connection failed."
            );
        }
    }

    private boolean validateInputs() {

        if (productField.getText().isBlank())
            return false;

        if (placeOfTesting.getText().isBlank())
            return false;

        if (fileName.getText().isBlank())
            return false;

        return testDate.getValue() != null;
    }

    private void loadCategoriesPage() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/log/ui/views/CategoriesPage.fxml"
                            )
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