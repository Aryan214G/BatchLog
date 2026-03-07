package com.log.ui;

import com.log.core.AppState;
import com.log.core.basePropertiesState;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import com.log.service.ProductService;

public class BasePropertiesController {

    // ===== FXML FIELDS =====
    @FXML private TextField projectName;
    @FXML private TextField batchNo;
    @FXML private TextField productName;
    @FXML private TextField productID;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField fileName;

    private final AppState appState = AppState.getInstance();
    private final basePropertiesState bpropState = basePropertiesState.getInstance();
    private ProjectService projectService = new ProjectService();

    // ===== INITIALIZATION =====
    @FXML
    public void initialize() {
        // You can preload data here if needed
        setDefaultProject();
        System.out.println("Base Properties Loaded");
    }

    // ===== BUTTON ACTIONS =====

    @FXML
    private void handleNext() {

        if (!validateInputs()) {
            System.out.println("Validation failed");
            return;
        }

        String project = projectName.getText();
        String batch = batchNo.getText();
        String product = productName.getText();
        String component = productID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();
        String file = fileName.getText();

        handleCreateProject(project);

        int productId = Integer.parseInt(productID.getText());
        productService.createProduct(productId, product);

        // Example: store something in AppState if needed
        System.out.println("Project: " + project);
        System.out.println("Batch: " + batch);

        // TODO: Save to AppState / Database / Model
        bpropState.setProjectName(projectName.getText());
        bpropState.setBatchNo(batchNo.getText());
        bpropState.setProductName(productName.getText());
        bpropState.setProductID(productID.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());
        bpropState.setFileName(fileName.getText());

        appState.setProjectCreated(true);
        // TODO: Navigate to next screen
        loadCategoriesPage();

    }
    private void loadCategoriesPage() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/CategoriesPage.fxml")
            );

            Parent root = loader.load();

            // Replace current scene content
            projectName.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProductService productService = new ProductService();
    @FXML
    private void handleCancel() {
        loadCategoriesPage();
    }

    // ===== HELPERS =====

    private boolean validateInputs() {

        if (projectName.getText().isEmpty()) return false;
        if (batchNo.getText().isEmpty()) return false;
        if (productName.getText().isEmpty()) return false;

        return true;
    }

    private void clearFields() {
        projectName.clear();
        batchNo.clear();
        productName.clear();
        productID.clear();
        placeOfTesting.clear();
        fileName.clear();
        testDate.setValue(null);
    }
    public void setDefaultProject(){
        bpropState.setProjectName("Project");
        bpropState.setBatchNo("456");
        bpropState.setProductName("Product");
        bpropState.setProductID("1738");
        bpropState.setTestDate(LocalDate.now());
        bpropState.setPlaceOfTesting("Lab 4");
        bpropState.setFileName("TestFile");

    }

    private void handleCreateProject(String project) {

        int projectId = projectService.createProject(project);

        bpropState.setProjectId(projectId);

        System.out.println("Project created with ID: " + projectId);
    }

}
