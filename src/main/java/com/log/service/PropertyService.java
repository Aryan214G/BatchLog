package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.model.Property;
import com.log.model.PropertyView;
import javafx.collections.ObservableList;

import java.sql.Connection;

public class PropertyService {
    private final PropertyDAO propertyDAO;
    private final DefaultPropertiesDAO defaultPropertiesDAO;

    public PropertyService(){
        this.propertyDAO = new PropertyDAO();
        this.defaultPropertiesDAO = new DefaultPropertiesDAO();
    }

    // TODO: uncoment after enabling the method in the DAO class
//    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
//        return propertyDAO.getPropertiesByBatch(batchCode);
//    }

    public ObservableList<PropertyView> getPropertiesByCategory(String categoryName){
        return defaultPropertiesDAO.getDefaultProperties(categoryName);
    }

    public void insertProperty(Connection conn, Property property){
        propertyDAO.insertProperty(conn, property);
    }
}
