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
import com.log.util.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @FXML private TextField sop;
    private boolean isEdit = false;
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
        loadExistingProject();
    }
    // ===== BUTTON ACTIONS =====
    @FXML
    private void handleNext() throws SQLException {

        String project = projectName.getText();
        String batch = batchNo.getText();
        String sopValue = sop.getText().trim();
        String product = productName.getText();
        String component = productID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();


        bpropState.setProjectName(projectName.getText());
        bpropState.setBatchNo(StringUtils.nullIfBlank(batchNo.getText()));
        bpropState.setSop(sopValue.isBlank() ? null : sopValue);
        bpropState.setProductName(productName.getText());
        bpropState.setProductID(productID.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());


        if(isEdit){
            handleEditBatch();
            return;
        }

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

            String batchID = StringUtils.nullIfBlank(bpropState.getBatchNo());
            String sop=bpropState.getSop();
            String testDate = bpropState.getTestDate().toString();
            String testSite = bpropState.getPlaceOfTesting();

            int productCode =
                    productService.getProductCodeFromDB(
                            conn,
                            new Product(
                                    bpropState.getProductID(),
                                    bpropState.getProductName(),
                                    bpropState.getProjectId()
                            )
                    );

            // ================= PRODUCT ONLY =================

            if (batchID == null) {

                batchTestService.createBatchTest(
                        conn,
                        new BatchTest(
                                null,      // Batch_CODE
                                testDate,
                                testSite,
                                productCode,
                                sop
                        )
                );

                return;
            }

            // ================= BATCH TEST =================

            batchService.createBatch(
                    conn,
                    new Batch(batchID, productCode)
            );

            Integer batchCode = bpropState.getBatchCode();

            batchTestService.createBatchTest(
                    conn,
                    new BatchTest(
                            batchCode,
                            testDate,
                            testSite,
                            productCode,
                            sop
                    )
            );
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

        if (projectName.getText().isBlank()) return false;

        if (productID.getText().isBlank()) return false;

        if (productName.getText().isBlank()) return false;

        return true;
    }

    private void clearFields() {
        projectName.clear();
        batchNo.clear();
        productName.clear();
        productID.clear();
        placeOfTesting.clear();
        testDate.setValue(null);
        sop.setText(null);
    }

    public void setEdit(boolean value){
        isEdit = value;
    }

    private void handleCreateProject(Connection conn, String project) throws SQLException {

        int projectId = projectService.createProject(conn, project);

        bpropState.setProjectId(projectId);

        System.out.println("Project created with ID: " + projectId);
    }

    public void loadExistingProject() {

        projectName.setText(
                bpropState.getProjectName()
        );

        batchNo.setText(
                bpropState.getBatchNo()
        );

        sop.setText(
                bpropState.getSop()
        );

        productName.setText(
                bpropState.getProductName()
        );

        productID.setText(
                bpropState.getProductID()
        );

        testDate.setValue(
                bpropState.getTestDate()
        );

        placeOfTesting.setText(
                bpropState.getPlaceOfTesting()
        );


        System.out.println("Project Name = " + bpropState.getProjectName());
        System.out.println("Batch No = " + bpropState.getBatchNo());
        System.out.println("Product Name = " + bpropState.getProductName());
        System.out.println("Product ID = " + bpropState.getProductID());
        System.out.println("Test Date = " + bpropState.getTestDate());
        System.out.println("Place = " + bpropState.getPlaceOfTesting());
        System.out.println("SOP = " + bpropState.getSop());
    }

    private void handleEditBatch() throws SQLException {
        String project = projectName.getText();
        String batch = batchNo.getText();
        String sopValue = sop.getText().trim();
        String product = productName.getText();
        String component = productID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();


        Connection connection = DBUtil.getConnection();

        projectService.editProject(projectService.getProjectId(connection, bpropState.getProjectName()),project);

        batchService.updateBatchId(connection, bpropState.getBatchCode(), batch);

        //TODO: update products later

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String newDate = date.format(formatter);

        batchTestService.editTestDate(bpropState.getTestId(), newDate);

        //edit place
        batchTestService.editTestSite(bpropState.getTestId(), place);


        //edit file



        bpropState.setProjectName(projectName.getText());
        bpropState.setBatchNo(batchNo.getText());
        bpropState.setSop(sopValue.isBlank() ? null : sopValue);
        bpropState.setProductName(productName.getText());
        bpropState.setProductID(productID.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());

        loadCategoriesPage();
    }

}
