package com.log.service;

import com.log.model.DefaultProperty;
import com.log.dao.DefaultPropertiesDAO;
import com.log.service.PropertyUnitsService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPropertyService {
    private DefaultPropertiesDAO DPdao=new DefaultPropertiesDAO();
    private PropertyUnitsService psu=new PropertyUnitsService();


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

    public int createDefaultProperty(
            Connection conn,
            String propertyName,
            int categoryId,
            int unitId,
            int rows
    ) {

        DefaultProperty property = new DefaultProperty();

        property.setPropertyName(propertyName);
        property.setCategoryId(categoryId);
        property.setUnitId(unitId);
        property.setRows(rows);
        property.setHasComponentNumber(0);

        return DPdao.insertDefaultProperty(
                conn,
                property
        );
    }

    public boolean propertyExists(
            Connection conn,
            String propertyName,
            int categoryId
    ) {

        return DPdao.propertyExists(
                conn,
                propertyName,
                categoryId
        );
    }

    public int createPropertyWithDefaultUnit(
            Connection conn,
            String propertyName,
            int categoryId,
            int unitId,
            int rows
    ) {

        try {

            conn.setAutoCommit(false);

            int propertyId =
                    createDefaultProperty(
                            conn,
                            propertyName,
                            categoryId,
                            unitId,
                            rows
                    );
            if (propertyId == -1) {
                throw new RuntimeException(
                        "Failed to create property"
                );
            }
            psu.addUnitToProperty(
                    conn,
                    propertyId,
                    unitId
            );

            conn.commit();

            return propertyId;

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e);

        } finally {

            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
