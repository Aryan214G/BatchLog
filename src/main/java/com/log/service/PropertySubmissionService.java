package com.log.service;

import com.log.model.Direction;
import com.log.model.PropertyState;
import com.log.model.Temperature;
import com.log.service.DirectionService;
import com.log.service.TemperatureService;

public class PropertySubmissionService {

    private TemperatureService temperatureService;
    private DirectionService directionService;

    private int tempId;
    public PropertySubmissionService(TemperatureService temperatureService,
                                     DirectionService directionService) {
        this.temperatureService = temperatureService;
        this.directionService = directionService;
    }

    public void submit(PropertyState propertyState) {
        handleTemperature(propertyState.getTemperature());
        handleDirection(propertyState.getDirection());
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
            directionService.createDirectionValue(dir);
        } else {
            directionService.updateDirectionValue(dir);
        }
    }
}