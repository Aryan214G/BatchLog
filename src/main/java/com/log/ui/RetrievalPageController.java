package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.BatchTest;
import com.log.model.Property;
import com.log.service.PropertyService;
import com.log.ui.util.SearchUtil;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class RetrievalPageController {

    @FXML
    private TextField projectField;

    @FXML
    private TextField productField;

    @FXML
    private TextField BatchIDfield;

    @FXML
    private TextField Dateoftestingfield;

    @FXML
    private TextField Placeoftestingfield;

    @FXML
    private VBox sidebar;

    @FXML
    private Button toggleBtn;


    private boolean isOpen = true;


    @FXML
    private VBox resultsContainer;

    private PropertyService propertyService = new PropertyService();

    public void populateResults(List<BatchTest> results) {

        // Clear previous results
        resultsContainer.getChildren().clear();

        if (results.isEmpty()) {
            Label empty = new Label("No results found");
            empty.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            resultsContainer.getChildren().add(empty);
            return;
        }

        for (BatchTest batch : results) {

            Button btn = new Button(formatBatchText(batch));

            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("""
            -fx-background-color: #34495e;
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-padding: 10;
        """);

            // Click action
            btn.setOnAction(e -> handleBatchClick(batch));

            resultsContainer.getChildren().add(btn);
        }
    }

    private String formatBatchText(BatchTest batch) {
        return "Batch: " + batch.getBatchCode() +
                " | Date: " + batch.getTestDate() +
                " | Site: " + batch.getTestSite();
    }

//    private void handleBatchClick(BatchTest batch) {
//
//
//        try {
//            List<Property> properties =
//                    propertyService.getPropertiesByBatch(batch.getBatchCode());
//
//            // next step → display these
//            System.out.println(properties);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void handleBatchClick(BatchTest batch) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(batch);  // hand over the clicked batch

            Stage stage = (Stage) resultsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {

        double targetX = isOpen ? -250 : 0;

        TranslateTransition sidebarAnim =
                new TranslateTransition(Duration.millis(250), sidebar);
        sidebarAnim.setToX(targetX);

        TranslateTransition buttonAnim =
                new TranslateTransition(Duration.millis(250), toggleBtn);
        buttonAnim.setToX(targetX);

        sidebarAnim.play();
        buttonAnim.play();

        isOpen = !isOpen;
    }

    // ✅ Updated search handler
    @FXML
    private void handleSearch() {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SearchUtil searchUtil = new SearchUtil();
        List<BatchTest> results = null;
        try {
            results = searchUtil.searchBatches(
                    connection,
                    projectField.getText(),
                    productField.getText(),
                    BatchIDfield.getText(),
                    Dateoftestingfield.getText(),
                    Placeoftestingfield.getText()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateResults(results);
    }
}