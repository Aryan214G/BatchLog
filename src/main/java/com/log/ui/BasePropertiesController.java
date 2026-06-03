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
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;
import com.log.service.ProductService;
import com.log.core.StateManager;

public class BasePropertiesController {

    // ===== FXML FIELDS =====
    @FXML private TextField projectName;
    @FXML private TextField batchNo;
    @FXML private TextField productName;
    @FXML private TextField productID;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField fileName;
    @FXML private TextField sop;
    private final AppState appState = AppState.getInstance();
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private ProjectService projectService = new ProjectService();
    private ProductDAO productDAO = new ProductDAO();
    private BatchService batchService = new BatchService();
    private BatchTestService batchTestService = new BatchTestService();
    //disable this when you want to ignore debugging operations such as preloading base properties fields.
    private boolean debug = true;


    // ===== INITIALIZATION =====
    @FXML
    public void initialize()
    {
        StateManager.clearAll();

        if(debug){
            setDefaultProject();
            System.out.println("Temporary Base Properties Loaded");
        }
    }
    // ===== BUTTON ACTIONS =====
    @FXML
    private void handleNext() {

        String project = projectName.getText();
        String batch = batchNo.getText();
        String sopValue = sop.getText().trim();
        String product = productName.getText();
        String component = productID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();
        String file = fileName.getText();
        bpropState.setProjectName(projectName.getText());
        bpropState.setBatchNo(batchNo.getText());
        bpropState.setSop(sopValue.isBlank() ? null : sopValue);
        bpropState.setProductName(productName.getText());
        bpropState.setProductID(productID.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());
        bpropState.setFileName(fileName.getText());

        //manual transaction handling to prevent DB lock issues
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            handleCreateProject(conn, project);

            String productId = productID.getText();
            productService.createProduct(conn, productId, product);

            handleCreateBatch(conn);

            conn.commit();

        } catch (Exception e) {

            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Example: store something in AppState if needed
        System.out.println("Project: " + project);
        System.out.println("Batch: " + batch);
        // TODO: Save to AppState / Database / Model
        appState.setProjectCreated(true);
        loadCategoriesPage();
    }

    private void handleCreateBatch(Connection conn) {
        String batchID = bpropState.getBatchNo();
        int projectID = bpropState.getProjectId();
        String TestDate = bpropState.getTestDate().toString();
        String TestSite = bpropState.getPlaceOfTesting();

        int productCode = productService.getProductCodeFromDB(conn, new Product(
                bpropState.getProductID(),
                bpropState.getProductName(),
                bpropState.getProjectId()
        ));
        batchService.createBatch(conn, new Batch(
                batchID,
                productCode
        ));

        int batchCODE = bpropState.getBatchCode();
        batchTestService.createBatchTest(conn, new BatchTest(
                batchCODE,
                TestDate,
                TestSite,
                productCode
                ));
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

        projectName.setText("project6april");
        batchNo.setText("6");
        productName.setText("Product6");
        productID.setText("6");
        testDate.setValue(LocalDate.now());
        placeOfTesting.setText("Lab 6");
        fileName.setText("TestFile");
    }

    private void handleCreateProject(Connection conn, String project) throws SQLException {

        int projectId = projectService.createProject(conn, project);

        bpropState.setProjectId(projectId);

        System.out.println("Project created with ID: " + projectId);
    }



}
