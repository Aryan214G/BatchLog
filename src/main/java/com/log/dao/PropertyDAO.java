package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class  PropertyDAO {

    public int insertProperty(Connection conn, Property property) {
        String sql = """
                INSERT INTO Property
                (Property_name, Category_ID, Temp_ID, Dir_ID, Unit_ID, Test_ID, test_method)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        System.out.println("=== INSERT PROPERTY ===");
System.out.println("Property Name = " + property.getPropertyName());
System.out.println("Category ID   = " + property.getCategory().getCategoryId());
System.out.println("Temp ID       = " + property.getTemperature().getTempId());
System.out.println("Dir ID        = " + property.getDirection().getDirId());
System.out.println("Unit ID       = " + property.getUnit().getUnitId());
System.out.println("Test ID       = " + property.getTestID());
System.out.println("Test Method   = " + property.getTestMethod());

        try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, property.getPropertyName());
            statement.setInt(2, property.getCategory().getCategoryId());
            statement.setInt(3, property.getTemperature().getTempId());
            statement.setInt(4, property.getDirection().getDirId());
            statement.setInt(5, property.getUnit().getUnitId());
            statement.setInt(6, property.getTestID());
            statement.setString(7, property.getTestMethod());

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


    public List<Property> getPropertiesByTest(int testId) throws SQLException {

    List<Property> properties = new ArrayList<>();

    String sql = """
        SELECT
            p.Property_ID,
            p.Property_name,
            p.test_method,

            p.Category_ID,
            c.Category_name,

            p.Temp_ID,
            t.Temp_VAL,
            t.Temp_Unit_ID,
            tu.Temp_Unit AS Temp_Unit,

            p.Dir_ID,
            d.Dir_VAL,

            p.Unit_ID,
            u.Unit AS Property_Unit,

            p.Test_ID

        FROM Property p

        JOIN Category c
            ON p.Category_ID = c.Category_ID

        JOIN Units u
            ON p.Unit_ID = u.Unit_ID

        JOIN Direction d
            ON p.Dir_ID = d.Dir_ID

        JOIN Temperature t
            ON p.Temp_ID = t.Temp_ID

        JOIN Temperature_Units tu
            ON t.Temp_Unit_ID = tu.Temp_Unit_ID

        WHERE p.Test_ID = ?
        """;

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, testId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Property property = new Property();

            property.setPropertyID(rs.getInt("Property_ID"));
            property.setPropertyName(rs.getString("Property_name"));

            property.getCategory().setCategoryId(rs.getInt("Category_ID"));
            property.getCategory().setCategoryName(rs.getString("Category_name"));

            property.getTemperature().setTempId(rs.getInt("Temp_ID"));
            property.getTemperature().setTempVal(rs.getString("Temp_VAL"));
            property.getTemperature().setTempUnitVal(rs.getString("Temp_Unit"));
            property.getTemperature().setTempUnitID(rs.getInt("Temp_Unit_ID"));

            property.getDirection().setDirId(rs.getInt("Dir_ID"));
            property.getDirection().setDirVal(rs.getString("Dir_VAL"));

            property.getUnit().setUnitId(rs.getInt("Unit_ID"));
            property.getUnit().setUnit(rs.getString("Property_Unit"));

            property.setTestID(rs.getInt("Test_ID"));

            property.setTestMethod(rs.getString("test_method"));

            properties.add(property);
        }

    } catch (SQLException e) {
        throw new SQLException(
                "Failed to fetch properties for Test " + testId,
                e
        );
    }

    return properties;
}

    public void updateProperty(Connection conn, Property property) {

        String sql = """
            UPDATE Property
            SET Property_name = ?, 
                Category_ID = ?, 
                Temp_ID = ?, 
                Dir_ID = ?, 
                Unit_ID = ?, 
                Test_ID = ?,
                test_method = ?
            WHERE Property_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategory().getCategoryId());
            stmt.setInt(3, property.getTemperature().getTempId());
            stmt.setInt(4, property.getDirection().getDirId());
            stmt.setInt(5, property.getUnit().getUnitId());
            stmt.setInt(6, property.getTestID());
            stmt.setString(7, property.getTestMethod());
            stmt.setInt(8, property.getPropertyID());

            int rowsAffected = stmt.executeUpdate();

            System.out.println(
                    (rowsAffected > 0)
                            ? "Updated property"
                            : "Failed to update property"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteProperty(int propertyId){

    }

    public int getPropertyId(Connection conn, Property property) {

        String sql = """
            SELECT Property_ID
            FROM Property
            WHERE Property_name = ?
              AND Category_ID = ?
              AND Test_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategory().getCategoryId());
            stmt.setInt(3, property.getTestID());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Property_ID"); // found
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

    public Map<String, Double> getPropertyAveragesByBatch(Connection conn, int batchCode) throws SQLException {
        Map<String, Double> propertyAverages = new LinkedHashMap<>();

        String sql = """
        SELECT
            p.Property_name,
            AVG(pv.Prop_VAL) AS avg_val
        FROM Property p
        JOIN Property_Values pv ON pv.Property_ID = p.Property_ID
        JOIN Batch_Test bt      ON p.Test_ID      = bt.Test_ID
        WHERE bt.Batch_CODE = ?
        GROUP BY p.Property_name
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                propertyAverages.put(
                        rs.getString("Property_name"),
                        rs.getDouble("avg_val")
                );
            }
        }

        return propertyAverages;
    }

    public Property getPropertyById(Connection conn, int propertyId){

        String sql = """
                SELECT
            p.Property_name,
            p.test_method,

            p.Category_ID,
            c.Category_name,

            p.Temp_ID,
            t.Temp_VAL,
            t.Temp_Unit_ID,
            tu.Temp_Unit AS Temp_Unit,

            p.Dir_ID,
            d.Dir_VAL,

            p.Unit_ID,
            u.Unit AS Property_Unit,

            p.Test_ID

        FROM Property p

        JOIN Category c
            ON p.Category_ID = c.Category_ID

        JOIN Units u
            ON p.Unit_ID = u.Unit_ID

        JOIN Direction d
            ON p.Dir_ID = d.Dir_ID

        JOIN Temperature t
            ON p.Temp_ID = t.Temp_ID

        JOIN Temperature_Units tu
            ON t.Temp_Unit_ID = tu.Temp_Unit_ID

        WHERE p.Property_ID = ?""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, propertyId);

            ResultSet rs = stmt.executeQuery();

            Property property = new Property();

            if (rs.next()) {

            property.setPropertyID(propertyId);
            property.setPropertyName(rs.getString("Property_name"));

            property.getCategory().setCategoryId(rs.getInt("Category_ID"));
            property.getCategory().setCategoryName(rs.getString("Category_name"));

            property.getTemperature().setTempId(rs.getInt("Temp_ID"));
            property.getTemperature().setTempVal(rs.getString("Temp_VAL"));
            property.getTemperature().setTempUnitVal(rs.getString("Temp_Unit"));
            property.getTemperature().setTempUnitID(rs.getInt("Temp_Unit_ID"));

            property.getDirection().setDirId(rs.getInt("Dir_ID"));
            property.getDirection().setDirVal(rs.getString("Dir_VAL"));

            property.getUnit().setUnitId(rs.getInt("Unit_ID"));
            property.getUnit().setUnit(rs.getString("Property_Unit"));

            property.setTestID(rs.getInt("Test_ID"));

            property.setTestMethod(rs.getString("test_method"));

                return property;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, Double> getPropertyAveragesByTest(
            Connection conn,
            int testId
    ) throws SQLException {

        Map<String, Double> propertyAverages =
                new LinkedHashMap<>();

        String sql = """
        SELECT
            p.Property_name,
            AVG(pv.Prop_VAL) AS avg_val
        FROM Property p
        JOIN Property_Values pv
            ON pv.Property_ID = p.Property_ID
        WHERE p.Test_ID = ?
        GROUP BY p.Property_name
    """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, testId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                propertyAverages.put(
                        rs.getString("Property_name"),
                        rs.getDouble("avg_val")
                );
            }
        }

        return propertyAverages;
    }

    // In PropertyDAO
    public Map<String, Map<String, Double>> getPropertyAveragesByBatchWithUnits(Connection conn, int batchCode) throws SQLException {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();

        String sql = """
        SELECT
            p.Property_name,
            u.Unit,
            AVG(pv.Prop_VAL) AS avg_val
        FROM Property p
        JOIN Property_Values pv ON pv.Property_ID = p.Property_ID
        JOIN Units u            ON p.Unit_ID       = u.Unit_ID
        JOIN Batch_Test bt      ON p.Test_ID       = bt.Test_ID
        WHERE bt.Batch_CODE = ?
        GROUP BY p.Property_name, u.Unit
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String propName = rs.getString("Property_name");
                String unit     = rs.getString("Unit");
                double avg      = rs.getDouble("avg_val");
                result.computeIfAbsent(propName, k -> new LinkedHashMap<>())
                        .put(unit, avg);
            }
        }

        return result;
    }

    public Map<String, Map<String, Double>> getPropertyAveragesByTestWithUnits(Connection conn, int testId) throws SQLException {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();

        String sql = """
        SELECT
            p.Property_name,
            u.Unit,
            AVG(pv.Prop_VAL) AS avg_val
        FROM Property p
        JOIN Property_Values pv ON pv.Property_ID = p.Property_ID
        JOIN Units u            ON p.Unit_ID       = u.Unit_ID
        WHERE p.Test_ID = ?
        GROUP BY p.Property_name, u.Unit
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String propName = rs.getString("Property_name");
                String unit     = rs.getString("Unit");
                double avg      = rs.getDouble("avg_val");
                result.computeIfAbsent(propName, k -> new LinkedHashMap<>())
                        .put(unit, avg);
            }
        }

        return result;
    }


}

