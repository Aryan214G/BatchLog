package com.log.service;

import com.log.dao.TemperatureUnitDAO;
import com.log.model.Unit;

import java.sql.Connection;
import java.util.List;

public class TemperatureUnitService {

    private final TemperatureUnitDAO dao =
            new TemperatureUnitDAO();

    // =========================================
    // Insert
    // =========================================

    public int insertTemperatureUnit(
            Connection conn,
            Unit unit
    ) {

        return dao.insertTemperatureUnit(conn, unit);
    }

    // =========================================
    // Get by ID
    // =========================================

    public Unit getTemperatureUnitById(
            Connection conn,
            int id
    ) {

        return dao.getTemperatureUnitById(conn, id);
    }

    // =========================================
    // Get by Unit
    // =========================================

    public Unit getTemperatureUnit(
            Connection conn,
            String tempUnit
    ) {

        return dao.getTemperatureUnit(conn, tempUnit);
    }

    public List<String> getAllTemperatureUnits(Connection conn) {

        return dao.getAllTemperatureUnits(conn);
    }
}