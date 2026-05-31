package com.log.dto;

import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.model.PropertyRow;

import java.util.List;

public class ReportData {

    private BatchTest batchTest;
    private Batch batch;
    private List<PropertyRow> properties;

    private Double min;
    private Double max;
    private Double average;
    private Double standardDeviation;

    private int testReportNo;

    public BatchTest getBatchTest() {
        return batchTest;
    }

    public void setBatchTest(BatchTest batchTest) {
        this.batchTest = batchTest;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public List<PropertyRow> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyRow> properties) {
        this.properties = properties;
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
}
