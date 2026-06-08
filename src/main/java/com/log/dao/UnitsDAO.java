package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitsDAO {


    public Unit getUnitById(int unitId) {

        String sql = "SELECT * FROM Units WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unitId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Unit(
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Unit getUnitByName(Connection conn, String unit){
        String sql = "SELECT Unit_ID FROM Units WHERE Unit = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, unit);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                return new Unit(
                  rs.getInt(1),
                        unit
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Unit> getAllUnits() {

        List<Unit> units = new ArrayList<>();

        String sql = "SELECT * FROM Units";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Unit unit = new Unit(
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit")
                );

                units.add(unit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }


    public void updateUnit(Unit unit) {

        String sql = "UPDATE Units SET Unit = ? WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, unit.getUnit());
            stmt.setInt(2, unit.getUnitId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Unit getUnitById(Connection conn, int unitId) {

        String query = "SELECT * FROM Units WHERE Unit_ID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, unitId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Unit(
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteUnit(int unitId) {

        String sql = "DELETE FROM Units WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unitId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean unitExists(
            Connection conn,
            String unitName
    ) {

        String sql =
                "SELECT 1 FROM Units WHERE Unit = ?";

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, unitName);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public int insertUnit(
            Connection conn,
            Unit unit
    ) {

        String sql =
                "INSERT INTO Units(Unit) VALUES(?)";

        try (PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            stmt.setString(
                    1,
                    unit.getUnit()
            );

            stmt.executeUpdate();

            ResultSet rs =
                    stmt.getGeneratedKeys();

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

        return -1;
    }
}