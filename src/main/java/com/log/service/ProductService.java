package com.log.service;

import com.log.dao.ProductDAO;
import com.log.model.Product;
import com.log.core.basePropertiesState;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;
    private final basePropertiesState bpropState = basePropertiesState.getInstance();

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public void createProduct(int productId, String productName) {

        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        int projectId = bpropState.getProjectId();

        Product product = new Product(productId, productName.trim(), projectId);

        productDAO.insertProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public void deleteProduct(int productCode) {
        productDAO.deleteProduct(productCode);
    }
}