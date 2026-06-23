package com.log.dao;

import com.log.model.Product;
import com.log.database.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // INSERT
    public int insertProduct(Connection conn, Product product) {

        String sql = "INSERT INTO Product(Product_ID, Product_name, Project_ID) VALUES (?, ?, ?)";

        try (PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     );){

            stmt.setString(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getProjectId());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // SELECT ONE
    public Product getProduct(Connection conn, int productCode) {

        String sql = "SELECT * FROM Product WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("Product_code"),
                        rs.getString("Product_ID"),
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
    public List<Product> getAllProducts(Connection conn) {

        String sql = "SELECT * FROM Product";

        List<Product> products = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Product p = new Product(
                        rs.getInt("Product_code"),
                        rs.getString("Product_ID"),
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
    public void updateProduct(Connection conn, Product product) {

        String sql = "UPDATE Product SET Product_ID=?, Product_name=?, Project_ID=? WHERE Product_code=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getProjectId());
            stmt.setInt(4, product.getProductCode());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteProduct(Connection conn, int productCode) {

        String sql = "DELETE FROM Product WHERE Product_code=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productCode);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getProductCode(Connection conn, Product product){
        String sql = "SELECT Product_code FROM Product WHERE Product_ID=? AND Product_name=? AND Project_ID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductId());
            stmt.setString(2,product.getProductName());
            stmt.setInt(3,product.getProjectId());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getInt("Product_code");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;

    }

    public Product getProductByName(Connection conn, String productName) {
        String sql = "SELECT * FROM Product WHERE Product_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("Product_code"),
                        rs.getString("Product_ID"),
                        rs.getString("Product_name"),
                        rs.getInt("Project_ID")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getProductId(Connection conn, int productCode){
        String sql = "SELECT Product_ID FROM Product WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Product_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getProductNameByCode(Connection conn,int productCode){
        String sql = "SELECT Product_name FROM Product WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Product_name");
                return name;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateProductName(Connection conn, int productCode, String newProductName) {
        String sql = "UPDATE Product SET Product_name = ? WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newProductName);
            stmt.setInt(2, productCode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProductId(Connection conn, int productCode, String newProductId) {
        String sql = "UPDATE Product SET Product_ID = ? WHERE Product_code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newProductId);
            stmt.setInt(2, productCode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}