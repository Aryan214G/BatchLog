package com.log.service;

import com.log.model.Direction;
import com.log.model.PropertyState;
import com.log.model.Reading;
import com.log.model.Temperature;
import com.log.ui.InputRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyStateManager {

    private final Map<String, PropertyState> propertyStates = new HashMap<>();

    public void saveState(
            String property,
            List<InputRow> inputRows,
            Temperature temperature,
            Direction direction
    ) {
        if (property == null) return;

        PropertyState state = new PropertyState();
        state.setTemperature(temperature);
        state.setDirection(direction);
        state.setInputRows(inputRows);

        propertyStates.put(property, state);
    }

    public PropertyState getState(String property) {
        return propertyStates.get(property);
    }

    public void clearState(String property) {
        propertyStates.remove(property);
    }
}