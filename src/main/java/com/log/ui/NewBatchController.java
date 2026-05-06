package com.log.ui;

import com.log.core.BasePropertiesState;
import com.log.dao.ProductDAO;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.model.Product;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.ProjectService;
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
    @FXML private TextField fileName;

    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private ProjectService projectService = new ProjectService();
    private ProductDAO productDAO = new ProductDAO();
    private BatchService batchService = new BatchService();
    private BatchTestService batchTestService = new BatchTestService();

    private Connection conn;
    {
        try {
            conn = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleNext() {
        if (!validateInputs()) {
            System.out.println("Validation failed");
            return;
        }

        bpropState.setProductName(productField.getText().trim());
        bpropState.setBatchNo(batchNo.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());
        bpropState.setFileName(fileName.getText());

        handleCreateBatch();
        loadCategoriesPage();
    }

    private void handleCreateBatch() {
        String batchID  = bpropState.getBatchNo();
        String testDate = bpropState.getTestDate().toString();
        String testSite = bpropState.getPlaceOfTesting();

        int productCode = productDAO.getProductCode(conn, new Product(
                bpropState.getProductID(),
                bpropState.getProductName(),
                bpropState.getProjectId()
        ));

        Batch batch = new Batch(batchID, productCode);
        batchService.createBatch(conn, batch);
        batchTestService.createBatchTest(conn, new BatchTest(
                bpropState.getBatchCode(),
                testDate,
                testSite
        ));
    }

    private boolean validateInputs() {
        if (productField.getText().isEmpty()) return false;
        if (batchNo.getText().isEmpty()) return false;
        if (placeOfTesting.getText().isEmpty()) return false;
        if (fileName.getText().isEmpty()) return false;
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