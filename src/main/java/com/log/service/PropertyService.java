package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.dao.PropertyValuesDAO;
import com.log.database.DBUtil;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.model.PropertyView;
import com.log.ui.InputRow;
import javafx.collections.ObservableList;

import java.sql.Connection;
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


    public List<Property> getPropertiesByTest(int testId) throws SQLException {
        return propertyDAO.getPropertiesByTest(testId);
    }

    public ObservableList<PropertyView> getPropertiesByCategory(String categoryName){
        return defaultPropertiesDAO.getDefaultProperties(categoryName);
    }

    public int insertProperty(Connection conn, Property property){

        int propertyId = getPropertyId(conn, property);

        if (propertyId != -1) {
            // already exists
            System.out.println("Property already exists in DB. Returning id");
            return propertyId;
        } else {
            // insert new
            return propertyDAO.insertProperty(conn, property);
        }

    }


    public int getPropertyId(Connection conn, Property property){
        return propertyDAO.getPropertyId(conn, property);
    }

    public void insertPropertyValue(Connection conn, InputRow row){

            PropertyValue propertyValue = row.getPropertyValue();
            int propertyValId = propertyValuesDAO.insertValue(conn, propertyValue);
            row.getPropertyValue().setPropertyValID(propertyValId);
    }

    public void updatePropertyValue(Connection conn, InputRow row) {

        PropertyValue propertyValue = row.getPropertyValue();
        propertyValuesDAO.updatePropertyValue(conn, propertyValue);
    }

    public int getPropertyValueId(Connection conn, int propertyID){
        return propertyValuesDAO.getPropertyValueId(conn, propertyID);
    }
    public void updateProperty(Connection conn, Property property) {
        propertyDAO.updateProperty(conn, property);
    }

    public List<PropertyValue> getValuesByProperty(Connection conn, int propertyID){
        return propertyValuesDAO.getValuesByProperty(conn, propertyID);
    }
}
