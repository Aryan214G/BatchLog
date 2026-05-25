package com.log.service;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;
import com.log.dao.DefaultPropertiesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPropertyService {
    private DefaultPropertiesDAO DPdao=new DefaultPropertiesDAO();
    private CategoryService categoryService = new CategoryService();

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

    public int getPropertyId(Connection conn, String propertyName){
        return DPdao.getPropertyId(conn, propertyName);
    }

}
