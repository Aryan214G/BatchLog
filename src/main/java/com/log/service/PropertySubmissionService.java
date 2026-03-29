package com.log.service;

import com.log.model.*;

import java.util.List;

public class PropertySubmissionService {

    private TemperatureService temperatureService;
    private DirectionService directionService;

    private int tempId;
    private int directionId;
    public PropertySubmissionService(TemperatureService temperatureService,
                                     DirectionService directionService) {
        this.temperatureService = temperatureService;
        this.directionService = directionService;
    }

    public void submit(PropertyState propertyState, String propertyName) {
        handleTemperature(propertyState.getTemperature());
        handleDirection(propertyState.getDirection());
        handleProperty(propertyState.getReadings(), propertyName);
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

    private void handleProperty(List<Reading> readings, String propertyName){
        Property property = new Property(propertyName, );
    }
}