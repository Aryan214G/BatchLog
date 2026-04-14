package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.dao.PropertyValuesDAO;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.model.PropertyView;
import com.log.ui.InputRow;
import javafx.collections.ObservableList;

import java.sql.Connection;
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

    public int getPropertyId(Connection conn, Property property){
        return propertyDAO.getPropertyId(conn, property);
    }

    public void insertPropertyValue(Connection conn, int propertyID, InputRow row){

            row.getPropertyValue().setPropertyID(propertyID);

            PropertyValue propertyValue = row.getPropertyValue();
            int propertyValId = propertyValuesDAO.insertValue(conn, propertyValue);
            row.getPropertyValue().setPropertyValID(propertyValId);
    }

    public void updatePropertyValue(Connection conn, int propertyID, InputRow row) {

        row.getPropertyValue().setPropertyID(propertyID);
        PropertyValue propertyValue = row.getPropertyValue();
        propertyValuesDAO.updatePropertyValue(conn, propertyValue);
    }
}
