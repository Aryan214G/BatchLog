package com.log.service;

import com.log.core.AppState;
import com.log.dao.CategoryDAO;
import com.log.model.Category;
import com.log.model.DefaultProperty;
import com.log.model.Property;
import com.log.model.PropertyView;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;

    public class CategoryService {

        private final AppState state = AppState.getInstance();

        private PropertyService propertyService = new PropertyService();
        private final CategoryDAO categoryDAO;

        public CategoryService() {
            this.categoryDAO = new CategoryDAO();
        }

        public void createCategory(String categoryName) {

            if (categoryName == null || categoryName.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }

            Category category = new Category(categoryName.trim());

            categoryDAO.insertCategory(category);
        }

        public List<Category> getAllCategories() {
            return categoryDAO.getAllCategories();
        }

        public void refreshCategoriesState() {



            List<Category> dbCategories = categoryDAO.getAllCategories();

            state.getCategories().clear();
            state.getCategoriesMap().clear();

            for (Category c : dbCategories) {

                state.getCategories().add(c.getCategoryName());

                ObservableList<DefaultProperty> properties = propertyService.getPropertiesByCategory(c.getCategoryName());

                if (!state.getCategoriesMap().containsKey(c.getCategoryName())) {
                    state.getCategoriesMap().put(
                            c.getCategoryName(),
                            properties
                    );
                }
            }
        }

        public Category getCategory(Connection conn, String category) {
            return categoryDAO.getCategoryByName(conn, category);
        }

        public void deleteCategory(int categoryId) {
            categoryDAO.deleteCategory(categoryId);
        }
    }

