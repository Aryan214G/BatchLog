package com.log.core;

public final class StateManager {

    private StateManager(){

    }

    public static void clearAll() {

        AppState.getInstance().clear();
        BasePropertiesState.getInstance().clear();
        DefaultMapState.getInstance().clear();
        SelectedState.getInstance().clear();
    }

}
