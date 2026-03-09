package com.log.service;

import com.log.dao.UnitsDAO;
import com.log.model.Unit;

import java.util.List;

public class UnitsService {

    private UnitsDAO unitsDAO;

    public UnitsService() {
        this.unitsDAO = new UnitsDAO();
    }

    public void createUnit(Unit unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (unit.getUnit() == null || unit.getUnit().isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }

        unitsDAO.insertUnit(unit);
    }

    public Unit getUnitById(int unitId) {

        if (unitId <= 0) {
            throw new IllegalArgumentException("Invalid Unit ID");
        }

        return unitsDAO.getUnit(unitId);
    }

    public List<Unit> getAllUnits() {
        return unitsDAO.getAllUnits();
    }

    public void updateUnit(Unit unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (unit.getUnitId() <= 0) {
            throw new IllegalArgumentException("Invalid Unit ID");
        }

        if (unit.getUnit() == null || unit.getUnit().isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }

        unitsDAO.updateUnit(unit);
    }

    public void deleteUnit(int unitId) {

        if (unitId <= 0) {
            throw new IllegalArgumentException("Invalid Unit ID");
        }

        unitsDAO.deleteUnit(unitId);
    }
}