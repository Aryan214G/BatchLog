package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.model.PropertyView;

import java.sql.SQLException;
import java.util.List;

public class PropertyService {
    private final PropertyDAO propertyDAO;
    private final DefaultPropertiesDAO defaultPropertiesDAO;

    public PropertyService(){
        this.propertyDAO = new PropertyDAO();
        this.defaultPropertiesDAO = new DefaultPropertiesDAO();
    }

    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
        return propertyDAO.getPropertiesByBatch(batchCode);
    }

    public List<PropertyView> getPropertiesByCategory(int categoryId){
        return defaultPropertiesDAO.getDefaultProperties(categoryId);
    }
}
