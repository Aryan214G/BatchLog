package com.log.service;

import com.log.dao.DirectionDAO;
import com.log.model.Direction;

import java.util.List;

public class DirectionService {

    private DirectionDAO directionDAO;

    public DirectionService() {
        this.directionDAO = new DirectionDAO();
    }

    public void createDirection(Direction direction) {

        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }

        if (direction.getDirVal() == null || direction.getDirVal().isBlank()) {
            throw new IllegalArgumentException("Direction value cannot be empty");
        }

        directionDAO.insertDirection(direction);
    }

    public Direction getDirectionById(int dirId) {

        if (dirId <= 0) {
            throw new IllegalArgumentException("Invalid direction ID");
        }

        return directionDAO.getDirection(dirId);
    }

    public List<Direction> getAllDirections() {
        return directionDAO.getAllDirections();
    }

    public void updateDirection(Direction direction) {

        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }

        if (direction.getDirVal() == null || direction.getDirVal().isBlank()) {
            throw new IllegalArgumentException("Direction value cannot be empty");
        }

        directionDAO.updateDirection(direction);
    }

    public void deleteDirection(int dirId) {

        if (dirId <= 0) {
            throw new IllegalArgumentException("Invalid direction ID");
        }

        directionDAO.deleteDirection(dirId);
    }
}