package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.PropertyView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultPropertiesDAO {

    public ObservableList<PropertyView> getDefaultProperties(String categoryName){


        ObservableList<PropertyView> properties = FXCollections.observableArrayList();

        String sql = """
                    SELECT
                        p.Def_PropID,
                        p.Def_PropName,
                        c.Category_name,
                        p.rows
                    FROM Default_Properties p
                    JOIN Category c ON p.Category_ID = c.Category_ID
                    WHERE c.Category_name = ?
                    """;


        try(Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryName);

            try(ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("No rows returned");
                }

                while (rs.next()) {

                    int id = rs.getInt("Def_PropID");
                    String propName = rs.getString("Def_PropName");
                    String catName = rs.getString("Category_name");
                    int rows = rs.getInt("rows");
                    properties.add(new PropertyView(id, propName, catName, rows));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
