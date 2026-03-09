package com.log.model;

public class Property {
private int propertyID;
private String propertyName;
private int categoryID;
private int tempID;
private int dirID;
private int unitID;
private int batchCode;

    public Property(int propertyID, String propertyName, int categoryID, int tempID, int dirID, int unitID, int batchCode) {
        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.categoryID = categoryID;
        this.tempID = tempID;
        this.dirID = dirID;
        this.unitID = unitID;
        this.batchCode = batchCode;
    }

    public Property(String propertyName, int categoryID, int tempID, int dirID, int unitID, int batchCode) {
        this.propertyName = propertyName;
        this.categoryID = categoryID;
        this.tempID = tempID;
        this.dirID = dirID;
        this.unitID = unitID;
        this.batchCode = batchCode;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public int getDirID() {
        return dirID;
    }

    public void setDirID(int dirID) {
        this.dirID = dirID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(int batchCode) {
        this.batchCode = batchCode;
    }
}
