package com.log.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.log.database.DBUtil;
import com.log.model.Temperature;


public class TemperatureDAO {

    private int tempId;

    public int getTempId() {
        return tempId;
    }

    public int insertTemperature(Connection conn, Temperature temperature) {

        String sql = "INSERT INTO Temperature (Temp_VAL, Temp_Unit_ID) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, temperature.getTempVal());
            stmt.setInt(2, temperature.getTempUnitID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int tempId = -1;

            if(rs.next()){
                return tempId = rs.getInt(1);
            }
            this.tempId = tempId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Temperature getTemperature(int tempId) {

        String sql = """
                        SELECT 
                            t.Temp_ID,
                            t.Temp_VAL,
                            t.Temp_Unit_ID,
                            u.Unit AS Temp_Unit
                        FROM Temperature t
                        JOIN Units u ON t.Temp_Unit_ID = u.Unit_ID
                        WHERE t.Temp_ID = ?
                    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tempId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tempId = rs.getInt("Temp_ID");
                int tempVal = rs.getInt("Temp_VAL");

                int tempUnitId = rs.getInt("Temp_Unit_ID");
                String tempUnitVal = rs.getString("Temp_Unit");

                return new Temperature(
                        tempId,
                        tempVal,
                        tempUnitId,
                        tempUnitVal
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Uncomment if needed

//    public List<Temperature> getAllTemperatures() {
//
//        List<Temperature> temperatures = new ArrayList<>();
//
//        String sql = "SELECT * FROM Temperature";
//
//        try (Connection conn = DBUtil.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//
//                Temperature temp = new Temperature(
//                        rs.getInt("Temp_ID"),
//                        rs.getInt("Temp_VAL"),
//                        rs.getInt("Temp_Unit_ID")
//                );
//
//                temperatures.add(temp);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return temperatures;
//    }

    public void updateTemperature(Connection conn, Temperature temperature) {

        String sql = "UPDATE Temperature SET Temp_VAL = ?, Temp_Unit_ID = ? WHERE Temp_ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, temperature.getTempVal());
            stmt.setInt(2, temperature.getTempUnitID());
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
