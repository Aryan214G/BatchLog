package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.BatchTest;
import com.log.ui.util.SearchUtil;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

    private void handleBatchClick(BatchTest batch) {
        System.out.println("Clicked batch: " + batch.getBatchCode());

        // Later:
        // load properties for this batch
        // navigate to detail page
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