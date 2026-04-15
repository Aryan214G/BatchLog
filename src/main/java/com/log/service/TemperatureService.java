package com.log.service;

import com.log.dao.TemperatureDAO;
import com.log.model.Temperature;

import java.sql.Connection;
import java.util.List;

public class TemperatureService {

    private TemperatureDAO temperatureDAO;

    public TemperatureService() {
        this.temperatureDAO = new TemperatureDAO();
    }

    public int createTemperature(Connection conn, Temperature temperature) {

        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        return temperatureDAO.insertTemperature(conn, temperature);
    }

    public Temperature getTemperatureById(int tempId) {

        if (tempId <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        return temperatureDAO.getTemperature(tempId);
    }

    // uncomment after enabling the method in DAO
//    public List<Temperature> getAllTemperatures() {
//        return temperatureDAO.getAllTemperatures();
//    }

    public void updateTemperature(Connection conn, Temperature temperature) {

        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        if (temperature.getTempId() <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        temperatureDAO.updateTemperature(conn, temperature);
    }

    public void deleteTemperature(int tempId) {

        if (tempId <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        temperatureDAO.deleteTemperature(tempId);
    }

    public int getTempId(){ return temperatureDAO.getTempId();}
}