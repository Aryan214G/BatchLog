package com.log.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.log.database.DBUtil;
import com.log.model.Temperature;


public class TemperatureDAO {

    public void insertTemperature(Temperature temperature) {

        String sql = "INSERT INTO Temperature (Temp_ID, Temp_VAL, Temp_UNIT) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, temperature.getTempId());
            stmt.setInt(2, temperature.getTempVal());
            stmt.setInt(3, temperature.getTempUnit());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Temperature getTemperature(int tempId) {

        String sql = "SELECT * FROM Temperature WHERE Temp_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tempId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Temperature(
                        rs.getInt("Temp_ID"),
                        rs.getInt("Temp_VAL"),
                        rs.getInt("Temp_UNIT")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Temperature> getAllTemperatures() {

        List<Temperature> temperatures = new ArrayList<>();

        String sql = "SELECT * FROM Temperature";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Temperature temp = new Temperature(
                        rs.getInt("Temp_ID"),
                        rs.getInt("Temp_VAL"),
                        rs.getInt("Temp_UNIT")
                );

                temperatures.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return temperatures;
    }

    public void updateTemperature(Temperature temperature) {

        String sql = "UPDATE Temperature SET Temp_VAL = ?, Temp_UNIT = ? WHERE Temp_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, temperature.getTempVal());
            stmt.setInt(2, temperature.getTempUnit());
            stmt.setInt(3, temperature.getTempId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTemperature(int tempId) {

        String sql = "DELETE FROM Temperature WHERE Temp_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tempId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
