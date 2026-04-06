package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.dao.ProductDAO;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.service.BatchService;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
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
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private ProjectService projectService = new ProjectService();
    private ProductDAO productDAO = new ProductDAO();
    private BatchService batchService = new BatchService();

    //disable this when you want to ignore debugging operations such as preloading base properties fields.
    private boolean debug = true;

    // ===== INITIALIZATION =====
    @FXML
    public void initialize() {
        if(debug){
        // preload base properties
        handleNext();
        }
    }
    // ===== BUTTON ACTIONS =====
    @FXML
    private void handleNext() {

        if(!debug){
            if (!validateInputs()) {
                System.out.println("Validation failed");
                return;
            }
        }

        String project = null;
        String batch = null;
        String product = null;
        String productId = null;
        LocalDate date = null;
        String place = null;
        String file = null;

        if(!debug){
             project = projectName.getText();
             batch = batchNo.getText();
             product = productName.getText();
             productId = productID.getText();
             date = testDate.getValue();
             place = placeOfTesting.getText();
             file = fileName.getText();

            bpropState.setProjectName(projectName.getText());
            bpropState.setBatchNo(batchNo.getText());
            bpropState.setProductName(productName.getText());
            bpropState.setProductID(productID.getText());
            bpropState.setTestDate(testDate.getValue());
            bpropState.setPlaceOfTesting(placeOfTesting.getText());
            bpropState.setFileName(fileName.getText());
        }
        else{
            setDefaultProject();
            project = bpropState.getProjectName();
            batch = bpropState.getBatchNo();
            product = bpropState.getProductName();
            productId = bpropState.getProductID();
            date = testDate.getValue();
            place = bpropState.getPlaceOfTesting();
            file = bpropState.getFileName();
        }

        //manual transaction handling to prevent DB lock issues
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            handleCreateProject(conn, project);

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
        int batchID = Integer.parseInt(bpropState.getBatchNo());
        int projectID = bpropState.getProjectId();
        String TestDate = bpropState.getTestDate().toString();
        String TestSite = bpropState.getPlaceOfTesting();
        //TODO: call service class and not DAO class
        int productCode = productService.getProductCode(conn, bpropState.getProductID(),bpropState.getProductName(),bpropState.getProjectId());
        batchService.createBatch(conn, new Batch(
                batchID,
                TestDate,
                TestSite,
                projectID,
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
        bpropState.setProjectName("project6april");
        bpropState.setBatchNo("6");
        bpropState.setProductName("Product6");
        bpropState.setProductID("6");
        bpropState.setTestDate(LocalDate.now());
        bpropState.setPlaceOfTesting("Lab 6");
        bpropState.setFileName("TestFile");

    }

    private void handleCreateProject(Connection conn, String project) throws SQLException {

        int projectId = projectService.createProject(conn, project);

        bpropState.setProjectId(projectId);

        System.out.println("Project created with ID: " + projectId);
    }

}
