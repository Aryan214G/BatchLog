package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // INSERT
    public void insertCategory(Category category) {

        String sql = "INSERT INTO Category(Category_name) VALUES (?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SELECT ONE
    public Category getCategory(int categoryId) {

        String sql = "SELECT * FROM Category WHERE Category_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Category(
                        rs.getInt("Category_ID"),
                        rs.getString("Category_name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // SELECT ALL
    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM Category";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Category c = new Category(
                        rs.getInt("Category_ID"),
                        rs.getString("Category_name")
                );

                categories.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // DELETE
    public void deleteCategory(int categoryId) {

        String sql = "DELETE FROM Category WHERE Category_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}