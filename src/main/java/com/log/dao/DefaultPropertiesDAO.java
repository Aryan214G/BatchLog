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

    public boolean propertyExists(
            Connection conn,
            String propertyName,
            int categoryId
    ) {

        String sql = """
        SELECT 1
        FROM Default_Properties
        WHERE Def_PropName = ?
        AND Category_ID = ?
        """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, propertyName);
            stmt.setInt(2, categoryId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public ObservableList<DefaultProperty> getDefaultProperties(String categoryName){


        ObservableList<DefaultProperty> properties = FXCollections.observableArrayList();

        String sql = """
                    SELECT
                        p.Def_PropID,
                        p.Def_PropName,
                        c.Category_name,
                        p.rows,
                        p.Has_Component_Number
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

                    DefaultProperty defaultProperty = new DefaultProperty(id, propName, catName, rows);
                    defaultProperty.setHasComponentNumber(rs.getInt("Has_Component_Number"));

                    properties.add(defaultProperty);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    public List<DefaultProperty> fetchFromDB() {

        List<DefaultProperty> list = new ArrayList<>();

        String query = """
SELECT
    Default_Properties.Def_PropID,
    Default_Properties.Def_PropName,
    Default_Properties.Unit_ID,
    Units.Unit,
    Default_Properties.Rows,
    Default_Properties.Category_ID,
    Default_Properties.Has_Component_Number

FROM Default_Properties

JOIN Units
    ON Default_Properties.Unit_ID = Units.Unit_ID
""";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DefaultProperty dp = new DefaultProperty(
                        rs.getString("Def_PropName"),
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit"),
                        rs.getInt("Rows"),
                        rs.getInt("Has_Component_Number")
                );

                dp.setPropertyId(
                        rs.getInt("Def_PropID")
                );

                dp.setCategoryId(
                        rs.getInt("Category_ID")
                );

                list.add(dp);
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

    public void updateDefaultProperty(DefaultProperty property) {

        String query = """
            UPDATE Default_Properties
            SET
                Unit_ID = ?,
                Rows = ?,
                Has_Component_Number = ?
            WHERE Def_PropID = ?
            """;

        try (
                Connection conn = DBUtil.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(query)
        ) {

            stmt.setInt(1, property.getUnitId());
            stmt.setInt(2, property.getRows());
            stmt.setInt(3, property.getHasComponentNumber());
            stmt.setInt(4, property.getPropertyId());


            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {

                System.out.println(
                        "Default property updated successfully."
                );

            } else {

                System.out.println(
                        "No property found to update."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public int insertDefaultProperty(
            Connection conn,
            DefaultProperty property
    ) {

        String sql = """
        INSERT INTO Default_Properties
        (
            Def_PropName,
            Unit_ID,
            Rows,
            Category_ID,
            Has_Component_Number
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            stmt.setString(
                    1,
                    property.getPropertyName()
            );

            stmt.setInt(
                    2,
                    property.getUnitId()
            );

            stmt.setInt(
                    3,
                    property.getRows()
            );

            stmt.setInt(
                    4,
                    property.getCategoryId()
            );

            stmt.setInt(
                    5,
                    property.getHasComponentNumber()
            );

            stmt.executeUpdate();

            ResultSet rs =
                    stmt.getGeneratedKeys();

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

        return -1;
    }
}
