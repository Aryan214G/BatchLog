package com.log.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.log.database.DBUtil;
import com.log.model.Direction;

public class DirectionDAO {

    public void insertDirection(Direction direction) {

        String sql = "INSERT INTO Direction (Dir_ID, Dir_VAL) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, direction.getDirId());
            stmt.setString(2, direction.getDirVal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Direction getDirection(int dirId) {

        String sql = "SELECT * FROM Direction WHERE Dir_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dirId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Direction(
                        rs.getInt("Dir_ID"),
                        rs.getString("Dir_VAL")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Direction> getAllDirections() {

        List<Direction> directions = new ArrayList<>();

        String sql = "SELECT * FROM Direction";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Direction direction = new Direction(
                        rs.getInt("Dir_ID"),
                        rs.getString("Dir_VAL")
                );

                directions.add(direction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return directions;
    }


    public void updateDirection(Direction direction) {

        String sql = "UPDATE Direction SET Dir_VAL = ? WHERE Dir_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, direction.getDirVal());
            stmt.setInt(2, direction.getDirId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteDirection(int dirId) {

        String sql = "DELETE FROM Direction WHERE Dir_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dirId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}