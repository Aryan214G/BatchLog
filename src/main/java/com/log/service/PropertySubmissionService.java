package com.log.service;

import com.log.model.*;

import java.util.List;

public class PropertySubmissionService {

    private TemperatureService temperatureService;
    private DirectionService directionService;
    private CategoryService categoryService;

    private int tempId;
    private int directionId;
    public PropertySubmissionService(TemperatureService temperatureService,
                                     DirectionService directionService,
                                     CategoryService categoryService) {
        this.temperatureService = temperatureService;
        this.directionService = directionService;
        this.categoryService = categoryService;
    }

    public void submit(PropertyState propertyState, String propertyName, String selectedCategory) {
        handleTemperature(propertyState.getTemperature());
        handleDirection(propertyState.getDirection());
        handleProperty(propertyState.getReadings(), propertyName, selectedCategory);
    }

    private void handleTemperature(Temperature temp) {
        if (temp.getTempId() == null) {
            temperatureService.createTemperature(temp);
        } else {
            temperatureService.updateTemperature(temp);
        }
        this.tempId = temperatureService.getTempId();
    }

    private void handleDirection(Direction dir) {
        if (dir.getDirId() == null) {
            this.directionId = directionService.getDirectionByName(dir.getDirVal()).getDirId();
        }
    }

    private void handleProperty(List<Reading> readings, String propertyName, String selectedCategory){
        Property property = new Property(
                propertyName,
                categoryService.getCategory(selectedCategory).getCategoryId(),
                tempId,
                directionId,

        );
    }
}