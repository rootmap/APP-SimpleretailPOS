package com.example.simpleretailpos.model;

public class TenderModel {
    private Integer tenderID;
    private String tenderName;

    public TenderModel() {
    }

    public TenderModel(Integer tenderID, String tenderName) {
        this.tenderID = tenderID;
        this.tenderName = tenderName;
    }

    public Integer getTenderID() {
        return tenderID;
    }

    public void setTenderID(Integer tenderID) {
        this.tenderID = tenderID;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }
}
