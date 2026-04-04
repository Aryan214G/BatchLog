package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Property;

import java.sql.*;

public class  PropertyDAO {

    public int insertProperty(Connection conn, Property property) {
        String sql = """
                INSERT INTO Property
                (Property_name, Category_ID, Temp_ID, Dir_ID, Unit_ID, Batch_CODE)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, property.getPropertyName());
            statement.setInt(2, property.getCategoryID());
            statement.setInt(3, property.getTempID());
            statement.setInt(4, property.getDirID());
            statement.setInt(5, property.getUnitID());
            statement.setInt(6, property.getBatchCode());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    //TODO: fix later
//    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
//        List<PropertyView> properties = new ArrayList<>();
//
//        String sql = """
//                SELECT
//                    p.Property_ID,
//                    p.Property_name,
//                    c.Category_name,
//                    t.Temp_VAL,
//                    d.Dir_VAL,
//                    u.Unit
//                FROM Property p
//                JOIN Category c ON p.Category_ID = c.Category_ID
//                JOIN Units u ON p.Unit_ID = u.Unit_ID
//                JOIN Direction d ON p.Dir_ID = d.Dir_ID
//                JOIN Temperature t ON p.Temp_ID = t.Temp_ID
//                WHERE p.Batch_CODE = ?
//                """;
//
//        try(Connection conn = DBUtil.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, batchCode);
//            ResultSet rs = stmt.executeQuery();
//
//            while(rs.next()){
//                    int id = rs.getInt("Property_ID");
//                    String propertyName = rs.getString("Property_name");
//                    String categoryName = rs.getString("Category_name");
//                    double tempValue = rs.getDouble("Temp_VAL");
//                    String direction = rs.getString("Dir_VAL");
//                    String propertyUnit = rs.getString("Unit");
//
//                    properties.add(new PropertyView(id, propertyName, categoryName, tempValue,
//                            direction, propertyUnit));
//
//            }
//        } catch (SQLException e) {
//            throw new SQLException("Failed to fetch properties for batch " + batchCode, e);
//        }
//
//        return properties;
//    }

    public Property getProperty(Connection conn, int id){
        String sql = "SELECT * FROM Property WHERE Property_ID = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return new Property(
                        rs.getInt(1),
                        rs.getString("Property_name"),
                        rs.getInt("Category_ID"),
                        rs.getInt("Temp_ID"),
                        rs.getInt("Dir_ID"),
                        rs.getInt("Unit_ID"),
                        rs.getInt("Batch_CODE")
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Property not found");
    }

    void updateProperty(Property property){
    }

    void deleteProperty(int propertyId){

    }
}
