package com.log.service;

import com.log.model.DefaultProperty;
import com.log.dao.DefaultPropertiesDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPropertyService {
    private DefaultPropertiesDAO DPdao=new DefaultPropertiesDAO();


    public Map<Integer, Map<String, DefaultProperty>> getDefaultsGrouped() {

        List<DefaultProperty> list = DPdao.fetchFromDB();


        Map<Integer, Map<String, DefaultProperty>> result = new HashMap<>();

        for (DefaultProperty dp : list) {

            int categoryId = dp.getCategoryId();

            result.computeIfAbsent(categoryId, k -> new HashMap<>())
                    .put(dp.getPropertyName(), dp);
        }
        return result;
    }

    public List<DefaultProperty> getDefaults() {
        return DPdao.fetchFromDB();
    }

    public void updateDefaultProperty(
            int propertyId,
            int unitId,
            int rows,
            int hasComponentNumber) {

        DefaultProperty property =
                new DefaultProperty();

        property.setPropertyId(propertyId);
        property.setUnitId(unitId);
        property.setRows(rows);
        property.setHasComponentNumber(hasComponentNumber);

        DPdao.updateDefaultProperty(property);
    }

    public int getPropertyId(Connection conn, String propertyName){
        return DPdao.getPropertyId(conn, propertyName);
    }

}
