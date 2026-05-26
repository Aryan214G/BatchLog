package com.log.model;

public class Category_Group {

    private int Category_ID;
    private int Batch_CODE;

    private Category_Group(int category_ID, int batch_CODE){
    this.Category_ID = category_ID;
    this.Batch_CODE=batch_CODE;
    }

    public int getCategory_ID() {return Category_ID;}

    public int getBatch_CODE() {return Batch_CODE;}

    public void setCategory_ID(int category_ID) {this.Category_ID = category_ID;}

    public void setBatch_CODE(int batch_CODE) {Batch_CODE = batch_CODE;}
}
