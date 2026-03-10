package com.log.core;

import com.log.model.PropertyView;

public class SelectedState {
    private static final SelectedState instance = new SelectedState();
    SelectedState(){}

    public static SelectedState getInstance() {
        return instance;
    }

    private String selectedCategory;
    private PropertyView selectedProperty;

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public PropertyView getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(PropertyView selectedProperty) { this.selectedProperty = selectedProperty; }
}
