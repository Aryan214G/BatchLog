package com.log.core;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState(){};

    public static AppState getInstance(){
        return instance;
    }

}
