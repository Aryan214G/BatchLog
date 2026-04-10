package com.log.service;

import com.log.dao.ProductDAO;
import com.log.model.Product;
import com.log.core.BasePropertiesState;

import java.sql.Connection;
import java.util.List;

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
}