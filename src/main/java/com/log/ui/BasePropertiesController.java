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
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.log.service.ProductService;
import javafx.stage.Stage;

public class BasePropertiesController {

    // ===== FXML FIELDS =====
    @FXML private TextField projectName;
    @FXML private TextField batchNo;
    @FXML private TextField productName;
    @FXML private TextField productID;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField sop;
    @FXML private TextField testSchedulefield;
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
        System.out.println("BaseProperties initialized");
    }
    // ===== BUTTON ACTIONS =====
    @FXML
    private void handleNext() throws SQLException {

        String originalProjectName = bpropState.getProjectName();
        String originalProductName = bpropState.getProductName();
        String originalProductId   = bpropState.getProductID();

        String project = projectName.getText();
        String batch = batchNo.getText();
        String sopValue = sop.getText() == null ? "" : sop.getText().trim();
        String product = productName.getText();
        String component = productID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();
        String testScheduleValue = testSchedulefield.getText() == null ? "" : testSchedulefield.getText().trim();


        bpropState.setProjectName(projectName.getText());
        bpropState.setBatchNo(StringUtils.nullIfBlank(batchNo.getText()));
        bpropState.setSop(sopValue.isBlank() ? null : sopValue);
        bpropState.setProductName(productName.getText());
        bpropState.setProductID(productID.getText());
        bpropState.setTestDate(testDate.getValue());
        bpropState.setPlaceOfTesting(placeOfTesting.getText());
        bpropState.setTestSchedule(testScheduleValue.isBlank() ? null : testScheduleValue);


        if (isEdit) {
            handleEditBatch(originalProjectName, originalProductName, originalProductId);
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
            String testSchedule=bpropState.getTestSchedule();

            int productCode =
                    productService.getProductCodeFromDB(
                            conn,
                            new Product(
                                    bpropState.getProductID(),
                                    bpropState.getProductName(),
                                    bpropState.getProjectId()
                            )
                    );

            // ================ PRODUCT ONLY =================

            if (batchID == null) {

                batchTestService.createBatchTest(
                        conn,
                        new BatchTest(
                                null,      // Batch_CODE
                                testDate,
                                testSite,
                                productCode,
                                sop,
                                testSchedule
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
                            sop,
                            testSchedule
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

    private void loadhomepage()
    {
      try
      {
          FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/com/log/ui/views/homepage.fxml")
                  );

          Parent root = loader.load();

      }
      catch (IOException e)
      {
          e.printStackTrace();
      }
    }
    private ProductService productService = new ProductService();
    @FXML
    private void handleCancel() {
        loadhomepage();
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
        testSchedulefield.setText(null);
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

        testSchedulefield.setText(
                bpropState.getTestSchedule()
        );


        System.out.println("Project Name = " + bpropState.getProjectName());
        System.out.println("Batch No = " + bpropState.getBatchNo());
        System.out.println("Product Name = " + bpropState.getProductName());
        System.out.println("Product ID = " + bpropState.getProductID());
        System.out.println("Test Date = " + bpropState.getTestDate());
        System.out.println("Place = " + bpropState.getPlaceOfTesting());
        System.out.println("SOP = " + bpropState.getSop());
    }

    private void handleEditBatch(
            String originalProjectName,
            String originalProductName,
            String originalProductId) throws SQLException {

        String project           = projectName.getText().trim();
        String batch             = batchNo.getText() != null ? batchNo.getText().trim() : null;
        String sopValue          = sop.getText() != null ? sop.getText().trim() : null;
        String newProductName    = productName.getText().trim();
        String newProductId      = productID.getText().trim();
        LocalDate date           = testDate.getValue();
        String place             = placeOfTesting.getText().trim();
        String testScheduleValue = testSchedulefield.getText() != null
                ? testSchedulefield.getText().trim() : null;

        int productCode = bpropState.getProductCode();

        System.out.println("Product code: " + productCode);
        System.out.println("Original name: " + originalProductName + " → New: " + newProductName);
        System.out.println("Original ID: "   + originalProductId   + " → New: " + newProductId);

        try (Connection connection = DBUtil.getConnection()) {

            // ── Edit project name ─────────────────────────────────────────────
            int projectId = projectService.getProjectId(connection, originalProjectName);
            if (projectId != -1 && !project.equals(originalProjectName)) {
                projectService.editProject(projectId, project);
            }

            // ── Edit product name ─────────────────────────────────────────────
            if (productCode > 0 && !newProductName.equals(originalProductName)) {
                productDAO.updateProductName(connection, productCode, newProductName);
            }

            // ── Edit product ID ───────────────────────────────────────────────
            if (productCode > 0 && !newProductId.equals(originalProductId)) {
                productDAO.updateProductId(connection, productCode, newProductId);
            }

            // ── Edit batch ID ─────────────────────────────────────────────────
            if (bpropState.getBatchCode() != null && batch != null && !batch.isBlank()) {
                batchService.updateBatchId(connection, bpropState.getBatchCode(), batch);
            }

            // ── Edit test date ────────────────────────────────────────────────
            if (date != null) {
                batchTestService.editTestDate(bpropState.getTestId(), date.toString());
            }

            // ── Edit test site ────────────────────────────────────────────────
            batchTestService.editTestSite(bpropState.getTestId(), place);

            // ── Edit SOP ──────────────────────────────────────────────────────
            batchTestService.editSop(bpropState.getTestId(), sopValue);

            // ── Edit test schedule ────────────────────────────────────────────
            batchTestService.editTestSchedule(
                    bpropState.getTestId(),
                    testScheduleValue != null && !testScheduleValue.isBlank()
                            ? testScheduleValue : null
            );
        }

        // ── Update state ──────────────────────────────────────────────────────
        bpropState.setProjectName(project);
        bpropState.setBatchNo(batch);
        bpropState.setSop(sopValue != null && !sopValue.isBlank() ? sopValue : null);
        bpropState.setProductName(newProductName);
        bpropState.setProductID(newProductId);
        bpropState.setTestDate(date);
        bpropState.setPlaceOfTesting(place);
        bpropState.setTestSchedule(
                testScheduleValue != null && !testScheduleValue.isBlank()
                        ? testScheduleValue : null
        );

        loadCategoriesPage();
    }


}
