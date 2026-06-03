package com.log.dto;

import java.util.List;

public class ReportData {

    private String propertyName;
    private String BatchId;
    private String testMethod;
    private String testDate;
    private String testSite;
    private String direction;
    private String temperature;
    private String temperatureUnit;
    private String testReportNo;
    private String date;

    private Double min;
    private Double max;
    private Double average;
    private Double standardDeviation;

    private List<Double> values;
    private String unit;

    // ============== Constructors ==============================

    public ReportData() {
}

    public ReportData(String propertyName, String batchId, String testMethod, String testDate, String testSite, String direction, String temperature, String temperatureUnit, String testReportNo, String date, Double min, Double max, Double average, Double standardDeviation, List<Double> values, String unit) {
        this.propertyName = propertyName;
        BatchId = batchId;
        this.testMethod = testMethod;
        this.testDate = testDate;
        this.testSite = testSite;
        this.direction = direction;
        this.temperature = temperature;
        this.temperatureUnit = temperatureUnit;
        this.testReportNo = testReportNo;
        this.date = date;
        this.min = min;
        this.max = max;
        this.average = average;
        this.standardDeviation = standardDeviation;
        this.values = values;
        this.unit = unit;
    }

    //============= Getters and setters =============================


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBatchId() {
        return BatchId;
    }

    public void setBatchId(String batchId) {
        BatchId = batchId;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestSite() {
        return testSite;
    }

    public void setTestSite(String testSite) {
        this.testSite = testSite;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getTestReportNo() {
        return testReportNo;
    }

    public void setTestReportNo(String testReportNo) {
        this.testReportNo = testReportNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
