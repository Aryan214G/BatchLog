package com.log.core;

import com.log.model.PropertyView;

public class SelectedState {
    private static final SelectedState instance = new SelectedState();
    SelectedState(){}

    public static SelectedState getInstance() {
        return instance;
    }

    private String selectedCategory;
    private int selectedCategoryId;
    private PropertyView selectedProperty;

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public int getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(int selectedCategoryId) { this.selectedCategoryId = selectedCategoryId; }

    public PropertyView getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(PropertyView selectedProperty) { this.selectedProperty = selectedProperty; }
}
