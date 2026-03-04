package com.log.model;

public class Temperature {

    private int tempId;
    private int tempVal;
    private int tempUnit;

    public Temperature(int tempId, int tempVal, int tempUnit) {
        this.tempId = tempId;
        this.tempVal = tempVal;
        this.tempUnit = tempUnit;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getTempVal() {
        return tempVal;
    }

    public void setTempVal(int tempVal) {
        this.tempVal = tempVal;
    }

    public int getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(int tempUnit) {
        this.tempUnit = tempUnit;
    }

}
