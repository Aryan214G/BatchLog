package com.log.model;

public class Temperature {

    private Integer tempId;
    private double tempVal;
    private int tempUnitID;
    private String tempUnitVal;
    private int tempUnitId;

    public Temperature() {
    }

    public Temperature(Integer tempId, double tempVal, int tempUnitID, String tempUnitVal) {
        this.tempId = tempId;
        this.tempVal = tempVal;
        this.tempUnitID = tempUnitID;
        this.tempUnitVal = tempUnitVal;
    }

    public Temperature(double tempVal, int tempUnitID, String tempUnitVal) {
        this.tempVal = tempVal;
        this.tempUnitID = tempUnitID;
        this.tempUnitVal = tempUnitVal;
    }

    public Temperature(double tempVal, String tempUnitVal) {
        this.tempVal = tempVal;
        this.tempUnitVal = tempUnitVal;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public double getTempVal() {
        return tempVal;
    }

    public void setTempVal(double tempVal) {
        this.tempVal = tempVal;
    }

    public int getTempUnitID() {
        return tempUnitID;
    }

    public void setTempUnitID(int tempUnitID) {
        this.tempUnitID = tempUnitID;
    }

    public String getTempUnitVal() {
        return tempUnitVal;
    }

    public void setTempUnitVal(String tempUnitVal) {
        this.tempUnitVal = tempUnitVal;
    }

    public int getTempUnitId() {
        return tempUnitId;
    }

    public void setTempUnitId(int tempUnitId) {
        this.tempUnitId = tempUnitId;
    }
}
