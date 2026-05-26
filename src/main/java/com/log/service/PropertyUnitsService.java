package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.dao.PropertyUnitsDAO;
import com.log.dao.PropertyValuesDAO;

import java.sql.Connection;
import java.util.List;

public class PropertyUnitsService {

    private final PropertyUnitsDAO propertyUnitsDAO;

    public PropertyUnitsService(){
        this.propertyUnitsDAO = new PropertyUnitsDAO();
    }

    public List<String> getUnitsByProperty(Connection conn, int defPropId) {
    return propertyUnitsDAO
            .getUnitsByProperty(conn, defPropId);
    }
}
