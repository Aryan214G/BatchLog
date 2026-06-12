package com.log.service;

import com.log.core.AppState;
import com.log.dao.CategoryDAO;
import com.log.model.Category;
import com.log.model.DefaultProperty;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;

public class CategoryService {

    private final AppState state = AppState.getInstance();

    private final PropertyService propertyService =
            new PropertyService();

    private final DefaultPropertyService defaultPropertyService =
            new DefaultPropertyService();

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public void createCategory(Connection conn, String categoryName) {

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Category name cannot be empty"
            );
        }

        int existingId =
                categoryDAO.getCategoryIdByName(conn, categoryName.trim());

        if (existingId != -1) {
            throw new IllegalArgumentException(
                    "Category already exists"
            );
        }

        categoryDAO.insertCategory(
                new Category(categoryName.trim())
        );
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public void refreshCategoriesState() {

        List<Category> dbCategories =
                categoryDAO.getAllCategories();

        state.getCategories().clear();
        state.getCategoriesMap().clear();

        // Refresh categories + properties
        for (Category c : dbCategories) {

            state.getCategories().add(
                    c.getCategoryName()
            );

            ObservableList<DefaultProperty> properties =
                    defaultPropertyService.getPropertiesByCategory(
                            c.getCategoryId()                    );

            state.getCategoriesMap().put(
                    c.getCategoryName(),
                    properties
            );
        }

        // Refresh default properties map too
        state.setDefaultPropertiesMap(
                defaultPropertyService.getDefaultsGrouped()
        );
    }

    public Category getCategory(
            Connection conn,
            String category
    ) {
        return categoryDAO.getCategoryByName(
                conn,
                category
        );
    }

    public int getCategoryId(Connection conn, String categoryName) {
        return categoryDAO.getCategoryIdByName(conn, categoryName);
    }

    public void renamecategory(int catid, String newcategoryName)
    {
        categoryDAO.renamecategory(catid, newcategoryName);
    }
}