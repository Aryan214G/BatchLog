package com.log.service;

import com.log.dao.DefaultPropertiesDAO;
import com.log.dao.PropertyDAO;
import com.log.dao.PropertyValuesDAO;
import com.log.model.PropertyValue;
import com.log.ui.InputRow;

import java.sql.Connection;
import java.util.List;

public class PropertyValuesService {

    private final PropertyValuesDAO propertyValuesDAO;


    public PropertyValuesService(){
        this.propertyValuesDAO = new PropertyValuesDAO();
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

    public List<PropertyValue> getValuesByProperty(Connection conn, int propertyID){
        return propertyValuesDAO.getValuesByProperty(conn, propertyID);
    }

}
