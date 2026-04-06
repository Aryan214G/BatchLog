package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.dao.PropertyValuesDAO;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.model.PropertyView;
import com.log.model.Reading;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PropertyService {
    private final PropertyDAO propertyDAO;
    private final DefaultPropertiesDAO defaultPropertiesDAO;
    private final PropertyValuesDAO propertyValuesDAO;

    public PropertyService(){
        this.propertyDAO = new PropertyDAO();
        this.defaultPropertiesDAO = new DefaultPropertiesDAO();
        this.propertyValuesDAO = new PropertyValuesDAO();
    }

    // TODO: uncoment after enabling the method in the DAO class
//    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
//        return propertyDAO.getPropertiesByBatch(batchCode);
//    }

    public ObservableList<PropertyView> getPropertiesByCategory(String categoryName){
        return defaultPropertiesDAO.getDefaultProperties(categoryName);
    }

    public int insertProperty(Connection conn, Property property){
        return propertyDAO.insertProperty(conn, property);
    }

    public void insertPropertyValues(Connection conn, int propertyID, List<Reading> readings){
        for(Reading input : readings){
            PropertyValue propertyValue = new PropertyValue(Integer.parseInt(input.getValue()), propertyID);
            propertyValuesDAO.insertValue(conn, propertyValue);
        }
    }

    public int getOrCreateProperty(Connection conn, Property property) throws SQLException {

        String findSql = """
        SELECT Property_ID FROM Property
        WHERE Property_name = ?
        AND Category_ID = ?
        AND Temp_ID = ?
        AND Dir_ID = ?
        AND Unit_ID = ?
        AND Batch_CODE = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(findSql)) {
            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategoryID());
            stmt.setInt(3, property.getTempID());
            stmt.setInt(4, property.getDirID());
            stmt.setInt(5, property.getUnitID());
            stmt.setInt(6, property.getBatchCode());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Property_ID"); // ✅ reuse existing property
            }
        }

        // ❗ Not found → insert new property
        return insertProperty(conn, property);
    }

    public void insertPropertyValuesIfNotExists(Connection conn, int propertyID,
                                                List<Reading> readings) throws SQLException {

        String checkSql = "SELECT 1 FROM Property_Values WHERE Property_ID = ? AND Prop_VAL = ?";
        String insertSql = "INSERT INTO Property_Values (Property_ID, Prop_VAL) VALUES (?, ?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            for (Reading reading : readings) {

                String value = reading.getValue();

                // check
                checkStmt.setInt(1, propertyID);
                checkStmt.setString(2, value);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    // insert only if not exists
                    insertStmt.setInt(1, propertyID);
                    insertStmt.setString(2, value);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
}