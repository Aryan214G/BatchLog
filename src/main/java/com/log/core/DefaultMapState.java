package com.log.core;

import java.util.HashMap;

public class DefaultMapState {
    private static final DefaultMapState instance = new DefaultMapState();
    DefaultMapState(){}

    public static DefaultMapState getInstance() {
        return instance;
    }

    private HashMap<String, Integer> defaultRowsMap = new HashMap<>();

    public HashMap<String, Integer> getDefaultRowsMap() {
        return defaultRowsMap;
    }

    public void setDefaultRowsMap(HashMap<String, Integer> defaultRowsMap) {
        this.defaultRowsMap = defaultRowsMap;
    }

    //======= default units for properties ========

    private HashMap<String, String> defaultUnitsMap = new HashMap<>();

    public HashMap<String, String> getDefaultUnitsMap() {
        return defaultUnitsMap;
    }

    public void setDefaultUnitsMap(HashMap<String, String> defaultUnitsMap) {
        this.defaultUnitsMap = defaultUnitsMap;
    }


}
