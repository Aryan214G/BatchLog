package com.log.dao;

import com.log.model.Product;
import com.log.database.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Connection conn;

    public ProductDAO() {
        try {
            this.conn = DBUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT
    public void insertProduct(Product product) {

        String sql = "INSERT INTO Product(Product_ID, Product_name, Project_ID) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getProjectId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SELECT ONE
    public Product getProduct(int productCode) {

        String sql = "SELECT * FROM Product WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("Product_code"),
                        rs.getInt("Product_ID"),
                        rs.getString("Product_name"),
                        rs.getInt("Project_ID")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // SELECT ALL
    public List<Product> getAllProducts() {

        String sql = "SELECT * FROM Product";

        List<Product> products = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Product p = new Product(
                        rs.getInt("Product_code"),
                        rs.getInt("Product_ID"),
                        rs.getString("Product_name"),
                        rs.getInt("Project_ID")
                );

                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // UPDATE
    public void updateProduct(Product product) {

        String sql = "UPDATE Product SET Product_ID=?, Product_name=?, Project_ID=? WHERE Product_code=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getProjectId());
            stmt.setInt(4, product.getProductCode());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteProduct(int productCode) {

        String sql = "DELETE FROM Product WHERE Product_code=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productCode);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}