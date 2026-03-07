package com.log.model;

public class Direction {

    private int dirId;
    private String dirVal;

    public Direction(int dirId, String dirVal) {
        this.dirId = dirId;
        this.dirVal = dirVal;
    }

    public int getDirId() {
        return dirId;
    }

    public void setDirId(int dirId) {
        this.dirId = dirId;
    }

    public String getDirVal() {
        return dirVal;
    }

    public void setDirVal(String dirVal) {
        this.dirVal = dirVal;
    }
}