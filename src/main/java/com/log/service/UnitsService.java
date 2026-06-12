package com.log.service;

import com.log.dao.UnitsDAO;
import com.log.database.DBUtil;
import com.log.model.Unit;

import java.sql.Connection;
import java.util.List;

public class UnitsService {

    private UnitsDAO unitsDAO;

    public UnitsService() {
        this.unitsDAO = new UnitsDAO();
    }

    public int createUnit(Unit unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (unit.getUnit() == null || unit.getUnit().isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }

        String unitName = unit.getUnit().trim();

        try (Connection conn = DBUtil.getConnection()) {

            Unit existing =
                    unitsDAO.getUnitByName(
                            conn,
                            unit.getUnit()
                    );

            if (existing != null) {
                return existing.getUnitId();
            }

            return unitsDAO.insertUnit(
                    conn,
                    unit
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
    public String getUnitNameById(int unitId) {

        try (Connection conn = DBUtil.getConnection()) {

            Unit unit = unitsDAO.getUnitById(conn, unitId);

            if (unit != null) {
                return unit.getUnit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Unit getUnit(Connection conn, String unit) {
        return unitsDAO.getUnitByName(conn, unit);
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

    public int getUnitIdByName(String unitName) {

        try (Connection conn = DBUtil.getConnection()) {

            Unit unit =
                    unitsDAO.getUnitByName(
                            conn,
                            unitName
                    );

            if (unit != null) {

                return unit.getUnitId();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }
    public boolean unitExists(
            Connection conn,
            String unitName
    ) {

        return unitsDAO.unitExists(
                conn,
                unitName
        );
    }

    public int createUnitIfNotExists(
            Connection conn,
            String unitName
    ) {

        Unit existing =
                unitsDAO.getUnitByName(
                        conn,
                        unitName
                );

        if (existing != null) {
            return existing.getUnitId();
        }

        return unitsDAO.insertUnit(
                conn,
                new Unit(unitName)
        );
    }

    public int insertUnit(
            Connection conn,
            String unitName
    ) {

        Unit unit = new Unit();
        unit.setUnit(unitName);

        return unitsDAO.insertUnit(
                conn,
                unit
        );
    }
}
