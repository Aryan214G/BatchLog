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

        System.out.println("DAO received category: [" + categoryName + "]");
        ObservableList<PropertyView> properties = FXCollections.observableArrayList();

        String sql = """
                    SELECT
                        p.Def_PropID,
                        p.Def_PropName,
                        c.Category_name
                    FROM Default_Properties p
                    JOIN Category c ON p.Category_ID = c.Category_ID
                    WHERE c.Category_name = ?
                    """;

        System.out.println("Executing query for category: " + categoryName);

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            Statement debugStmt = conn.createStatement();
            ResultSet debugRs = debugStmt.executeQuery("SELECT Category_ID, Category_name FROM Category");
            while (debugRs.next()) {
                System.out.println(debugRs.getInt(1) + " : " + debugRs.getString(2));
            }

            if (debugRs.next()) {
                System.out.println("Default_Properties row count = " + debugRs.getInt(1));
            }


            stmt.setString(1, categoryName);

            try(ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("No rows returned");
                }

                while (rs.next()) {

                    int id = rs.getInt("Def_PropID");
                    String propName = rs.getString("Def_PropName");
                    String catName = rs.getString("Category_name");

                    properties.add(new PropertyView(id, propName, catName));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
