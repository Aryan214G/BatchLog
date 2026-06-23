package com.log.service;

import com.log.dao.ProductDAO;
import com.log.database.DBUtil;
import com.log.model.Product;
import com.log.core.BasePropertiesState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductDAO productDAO;
    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public void createProduct(Connection conn, String productId, String productName) {

        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        int projectId = bpropState.getProjectId();

        Product product = new Product(productId, productName.trim(), projectId);

        int productCode = getProductCodeFromDB(conn, product);
        if (productCode != -1){
            System.out.println("Product already exists in DB");
            bpropState.setProductCode(productCode);
            return;
        }
        bpropState.setProductCode(productDAO.insertProduct(conn, product));
    }

    public List<Product> getAllProducts(Connection conn) {
        return productDAO.getAllProducts(conn);
    }

    public void deleteProduct(Connection conn, int productCode) {
        productDAO.deleteProduct(conn, productCode);
    }

    public int getProductCodeFromDB(Connection conn, Product product) {
        return productDAO.getProductCode(conn, product);
    }

    public Product getProduct(int productCode){

        try (Connection connection = DBUtil.getConnection()) {

            return productDAO.getProduct(connection, productCode);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Connection conn, Product product){
        productDAO.updateProduct(conn, product);
    }

    public Product getProductByName(String productName) {
        try (Connection connection = DBUtil.getConnection()){
            return productDAO.getProductByName(connection, productName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProductId(int productCode){
            try (Connection connection = DBUtil.getConnection()){

                return productDAO.getProductId(connection, productCode);
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

}

