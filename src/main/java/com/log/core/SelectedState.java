package com.log.core;

import com.log.model.DefaultProperty;
import com.log.model.Property;
import com.log.model.PropertyView;

public class SelectedState {
    private static final SelectedState instance = new SelectedState();
    SelectedState(){}

    public static SelectedState getInstance() {
        return instance;
    }

    private String selectedCategory;
    private int selectedCategoryId;
    private DefaultProperty selectedProperty;

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public int getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(int selectedCategoryId) { this.selectedCategoryId = selectedCategoryId; }

    public DefaultProperty getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(DefaultProperty selectedProperty) { this.selectedProperty = selectedProperty; }

    public void clear() {

        selectedCategory = null;

        selectedCategoryId = 0;

        selectedProperty = null;

        System.out.println("SelectedState cleared");
    }

}
