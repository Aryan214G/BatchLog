package com.log.model;

public class Temperature {

    private Integer tempId;
    private String tempVal;
    private int tempUnitID;
    private String tempUnitVal;

    public Temperature() {
    }

    public Temperature(Integer tempId, String tempVal, int tempUnitID, String tempUnitVal) {
        this.tempId = tempId;
        this.tempVal = tempVal;
        this.tempUnitID = tempUnitID;
        this.tempUnitVal = tempUnitVal;
    }

    public Temperature(String tempVal, int tempUnitID, String tempUnitVal) {
        this.tempVal = tempVal;
        this.tempUnitID = tempUnitID;
        this.tempUnitVal = tempUnitVal;
    }

    public Temperature(String tempVal, String tempUnitVal) {
        this.tempVal = tempVal;
        this.tempUnitVal = tempUnitVal;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public String getTempVal() {
        return tempVal;
    }

    public void setTempVal(String tempVal) {
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
    }

