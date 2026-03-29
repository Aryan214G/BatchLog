package com.log.model;

public class Temperature {

    private Integer tempId;
    private int tempVal;
    private String tempUnit;

    public Temperature(int tempId, int tempVal, String tempUnit) {
        this.tempId = tempId;
        this.tempVal = tempVal;
        this.tempUnit = tempUnit;
    }

    public Temperature(int tempVal, String tempUnit) {
        this.tempVal = tempVal;
        this.tempUnit = tempUnit;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public int getTempVal() {
        return tempVal;
    }

    public void setTempVal(int tempVal) {
        this.tempVal = tempVal;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

}
