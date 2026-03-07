package com.log.service;

import com.log.dao.PropertyDAO;
import com.log.model.PropertyView;

import java.sql.SQLException;
import java.util.List;

public class PropertyService {
    private final PropertyDAO propertyDAO;

    public PropertyService(){
        this.propertyDAO = new PropertyDAO();
    }

    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
        return propertyDAO.getPropertiesByBatch(batchCode);
    }
}
