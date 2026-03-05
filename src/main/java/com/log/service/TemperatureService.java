package com.log.service;

import com.log.dao.TemperatureDAO;
import com.log.model.Temperature;

import java.util.List;

public class TemperatureService {

    private TemperatureDAO temperatureDAO;

    public TemperatureService() {
        this.temperatureDAO = new TemperatureDAO();
    }

    public void createTemperature(Temperature temperature) {

        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        temperatureDAO.insertTemperature(temperature);
    }

    public Temperature getTemperatureById(int tempId) {

        if (tempId <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        return temperatureDAO.getTemperature(tempId);
    }

    public List<Temperature> getAllTemperatures() {
        return temperatureDAO.getAllTemperatures();
    }

    public void updateTemperature(Temperature temperature) {

        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        if (temperature.getTempId() <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        temperatureDAO.updateTemperature(temperature);
    }

    public void deleteTemperature(int tempId) {

        if (tempId <= 0) {
            throw new IllegalArgumentException("Invalid temperature ID");
        }

        temperatureDAO.deleteTemperature(tempId);
    }
}