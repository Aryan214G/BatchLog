package com.log.model;

public class PropertyValue {
    private int propertyValID;
    private int propertyVAL;
    private int propertyID;

    public PropertyValue(int propertyValID, int propertyVAL, int propertyID) {
        this.propertyValID = propertyValID;
        this.propertyVAL = propertyVAL;
        this.propertyID = propertyID;
    }

    public PropertyValue(int propertyVAL, int propertyID) {
        this.propertyVAL = propertyVAL;
        this.propertyID = propertyID;
    }

    public int getPropertyValID() {
        return propertyValID;
    }

    public void setPropertyValID(int propertyValID) {
        this.propertyValID = propertyValID;
    }

    public int getPropertyVAL() {
        return propertyVAL;
    }

    public void setPropertyVAL(int propertyVAL) {
        this.propertyVAL = propertyVAL;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }
}
