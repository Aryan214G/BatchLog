package com.log.model;

public class Unit {

    private int unitId;
    private String unit;

    public Unit(int unitId, String unit) {
        this.unitId = unitId;
        this.unit = unit;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}