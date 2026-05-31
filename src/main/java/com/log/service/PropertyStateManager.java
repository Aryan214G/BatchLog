package com.log.service;

import com.log.model.*;
import com.log.ui.InputRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyStateManager {

    private final Map<String, PropertyState> propertyStates = new HashMap<>();

    public void saveState(
            String property,
            String testMethod,
            List<InputRow> inputRows,
            Temperature temperature,
            Direction direction,
            Unit unit
    ) {
        if (property == null) return;

        PropertyState state = new PropertyState();
        state.setTemperature(temperature);
        state.setDirection(direction);
        state.setInputRows(inputRows);
        state.setUnit(unit);
        state.setTestMethod(testMethod);
        propertyStates.put(property, state);
    }

    public PropertyState getState(String property) {
        return propertyStates.get(property);
    }

    public void clearState(String property) {
        propertyStates.remove(property);
    }
}