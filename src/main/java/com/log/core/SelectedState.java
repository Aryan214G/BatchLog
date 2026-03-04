package com.log.core;

public class SelectedState {
    private static final SelectedState instance = new SelectedState();
    SelectedState(){}

    public static SelectedState getInstance() {
        return instance;
    }

    private String selectedCategory;
    private String selectedProperty;

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public String getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(String selectedProperty) { this.selectedProperty = selectedProperty; }
}
