package com.log.model;

public class PropertyValue {
    private int propertyValID;
    private double propertyVAL;
    private int propertyID;

    public PropertyValue(int propertyValID, double propertyVAL, int propertyID) {
        this.propertyValID = propertyValID;
        this.propertyVAL = propertyVAL;
        this.propertyID = propertyID;
    }

    public PropertyValue(double propertyVAL, int propertyID) {
        this.propertyVAL = propertyVAL;
        this.propertyID = propertyID;
    }

    public int getPropertyValID() {
        return propertyValID;
    }

    public void setPropertyValID(int propertyValID) {
        this.propertyValID = propertyValID;
    }

    public double getPropertyVAL() {
        return propertyVAL;
    }

    public void setPropertyVAL(double propertyVAL) {
        this.propertyVAL = propertyVAL;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }
}
