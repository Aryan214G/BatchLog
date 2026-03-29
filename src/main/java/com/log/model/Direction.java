package com.log.model;

public class Direction {

    private Integer dirId;
    private String dirVal;

    public Direction(int dirId, String dirVal) {
        this.dirId = dirId;
        this.dirVal = dirVal;
    }

    public Direction(String dirVal) {
        this.dirVal = dirVal;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getDirVal() {
        return dirVal;
    }

    public void setDirVal(String dirVal) {
        this.dirVal = dirVal;
    }
}