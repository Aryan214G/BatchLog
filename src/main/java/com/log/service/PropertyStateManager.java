package com.log.service;

import com.log.model.PropertyState;
import com.log.model.Reading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyStateManager {

    private final Map<String, PropertyState> propertyStates = new HashMap<>();

    public void saveState(
            String property,
            List<Reading> readings,
            String temperature,
            String tempUnit,
            String direction
    ) {
        if (property == null) return;

        PropertyState state = new PropertyState();
        state.setTemperature(temperature);
        state.setTemperatureUnit(tempUnit);
        state.setDirection(direction);
        state.setReadings(readings);

        propertyStates.put(property, state);
    }

    public PropertyState getState(String property) {
        return propertyStates.get(property);
    }

    public void clearState(String property) {
        propertyStates.remove(property);
    }
}