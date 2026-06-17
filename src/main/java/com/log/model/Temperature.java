package com.log.model;

public class Temperature {

    private Integer tempId;
    private String tempVal;
    private Integer tempUnitID;
    private String tempUnitVal;

    public Temperature() {
    }

    public Temperature(Integer tempId, String tempVal, Integer tempUnitID, String tempUnitVal) {
        this.tempId = tempId;
        this.tempVal = tempVal;
        this.tempUnitID = tempUnitID;
        this.tempUnitVal = tempUnitVal;
    }

    public Temperature(String tempVal, Integer tempUnitID, String tempUnitVal) {
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

    public Integer getTempUnitID() {
        return tempUnitID;
    }

    public void setTempUnitID(Integer tempUnitID) {
        this.tempUnitID = tempUnitID;
    }

    public String getTempUnitVal() {
        return tempUnitVal;
    }

    public void setTempUnitVal(String tempUnitVal) {
        this.tempUnitVal = tempUnitVal;
    }

    public boolean isEmpty() {
        return (tempVal == null || tempVal.isBlank())
                && tempUnitID == null;
    }

    }

