package com.log.model;

public class Unit {

    private int unitId;
    private String unit;

    public Unit() {
    }

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

    public Unit(String unit) {
        this.unit = unit;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Override
    public String toString() {
        return unit;
    }
}
