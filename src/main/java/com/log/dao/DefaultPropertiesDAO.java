package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.PropertyView;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultPropertiesDAO {

    public List<PropertyView> getDefaultProperties(int categoryId){

        List<PropertyView> properties = new ArrayList<>();
        String sql = """
                SELECT
                    p.Def_PropID,
                    p.Def_PropName,
                    c.Category_name
                FROM Default_Properties p
                JOIN Category c ON p.Category_ID = c.Category_ID
                """;

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                properties.add(new PropertyView(rs.getInt("Def_PropID"),
                        rs.getString("Def_PropName"), rs.getString("Category_name")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
