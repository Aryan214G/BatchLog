package com.log.service;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPropertyService {
    private CategoryService categoryService = new CategoryService();

    public Map<Integer, Map<String, DefaultProperty>> getDefaultsGrouped() {

        List<DefaultProperty> list = fetchFromDB();

        Map<Integer, Map<String, DefaultProperty>> result = new HashMap<>();

        for (DefaultProperty dp : list) {

            int categoryId = dp.getCategoryId();

            result.computeIfAbsent(categoryId, k -> new HashMap<>())
                    .put(dp.getPropertyName(), dp);
        }
        return result;
    }

    private List<DefaultProperty> fetchFromDB() {

        List<DefaultProperty> list = new ArrayList<>();

        String query = "SELECT Def_PropName, Unit_ID, Category_ID, Rows FROM Default_Properties";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DefaultProperty(
                        rs.getString("Def_PropName"),
                        rs.getInt("Unit_ID"),
                        rs.getInt("Category_ID"),
                        rs.getInt("Rows")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
