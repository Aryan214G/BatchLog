package com.log.ui;

import com.log.core.basePropertiesState;
import com.log.dao.BatchDAO;
import com.log.dao.ProductDAO;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class NewBatchController {
    @FXML private TextField batchNo;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField fileName;


    private final basePropertiesState bpropState = basePropertiesState.getInstance();
    private ProjectService projectService = new ProjectService();
    private ProductDAO productDAO = new ProductDAO();
    private BatchDAO batchDAO = new BatchDAO();

    @FXML
    private void handleNext() {

        if (!validateInputs()) {
            System.out.println("Validation failed");
            return;
        }


        String batch = batchNo.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();
        String file = fileName.getText();
        bpropState.setBatchNo(batchNo.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());
        bpropState.setFileName(fileName.getText());


        handleCreateBatch();

        System.out.println("Batch: " + batch);
        loadCategoriesPage();


    }

    private void handleCreateBatch() {
        int batchID = Integer.parseInt(bpropState.getBatchNo());
        int projectID = bpropState.getProjectId();
        String TestDate = bpropState.getTestDate().toString();
        String TestSite = bpropState.getPlaceOfTesting();
        int productCode = productDAO.getProductCode(bpropState.getProductID(),bpropState.getProductName(),bpropState.getProjectId());
        batchDAO.insertBatch(batchID,TestDate,TestSite,projectID,productCode);
    }
    private boolean validateInputs() {

        if (placeOfTesting.getText().isEmpty()) return false;
        if (batchNo.getText().isEmpty()) return false;
        if (fileName.getText().isEmpty()) return false;
        return true;
    }
    private void loadCategoriesPage() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/CategoriesPage.fxml")
            );

            Parent root = loader.load();

            // Replace current scene content
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
