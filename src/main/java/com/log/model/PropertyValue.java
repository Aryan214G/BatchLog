package com.log.model;

public class PropertyValue {
    private int propertyValID;
    private Double propertyVAL;
    private int propertyID;
    private String componentNumber;

    public PropertyValue() {
    }

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

    public Double getPropertyVAL() {
        return propertyVAL;
    }

    public void setPropertyVAL(Double propertyVAL) {
        this.propertyVAL = propertyVAL;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }


    public String getComponentNumber() {
        return componentNumber;
    }

    public void setComponentNumber(String componentNumber) {
        this.componentNumber = componentNumber;
    }
}
