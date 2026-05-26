package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;
import com.log.model.Property;
import com.log.model.PropertyView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultPropertiesDAO {

    public ObservableList<DefaultProperty> getDefaultProperties(String categoryName){


        ObservableList<DefaultProperty> properties = FXCollections.observableArrayList();

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


                    properties.add(new DefaultProperty(id, propName, catName, rows));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    public List<DefaultProperty> fetchFromDB() {

        List<DefaultProperty> list = new ArrayList<>();

        String query = "SELECT Def_PropName, Unit_ID, Category_ID, Rows FROM Default_Properties ";

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

    public int getPropertyId(Connection conn, String propertyName)
    {
        String query = """
                SELECT Def_PropID
                FROM Default_Properties
                WHERE Def_PropName = ?""";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, propertyName);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("Def_PropID");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // not found
    }
}
