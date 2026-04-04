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

    public Property getProperty(Connection conn, int id){
        return propertyDAO.getProperty(conn, id);
    }
    public void insertPropertyValues(Connection conn, int propertyID, List<Reading> readings){
        for(Reading input : readings){
            PropertyValue propertyValue = new PropertyValue(Integer.parseInt(input.getValue()), propertyID);
            propertyValuesDAO.insertValue(conn, propertyValue);
        }
    }
}
