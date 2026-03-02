package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MetricsController {

    @FXML
    private HBox metricsContainer;

    @FXML
    private Label meanLabel;

    @FXML
    private Label sdLabel;

    public void setMean(double mean) {
        meanLabel.setText(String.format("%.3f", mean));
        System.out.println("Mean set to: " + mean);
    }

    public void setStandardDeviation(double sd) {
        sdLabel.setText(String.format("%.3f", sd));
    }
}