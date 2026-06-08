package com.log.core;

public final class StateManager {

    private StateManager(){

    }

    public static void clearAll() {
        AppState appState = AppState.getInstance();
        appState.clear();
        appState.setEditMode(false);
        BasePropertiesState.getInstance().clear();
        DefaultMapState.getInstance().clear();
        SelectedState.getInstance().clear();

    }

}
