package com.example.simpleretailpos.model;

public class ProfitModel {
    private Integer invoiceID;
    private String invoiceDate;
    private String soldTo;
    private String invoiceTotal;
    private String invoiceCost;
    private String invoiceProfit;

    public ProfitModel() {
    }

    public ProfitModel(Integer invoiceID, String invoiceDate, String soldTo, String invoiceTotal, String invoiceCost, String invoiceProfit) {
        this.invoiceID = invoiceID;
        this.invoiceDate = invoiceDate;
        this.soldTo = soldTo;
        this.invoiceTotal = invoiceTotal;
        this.invoiceCost = invoiceCost;
        this.invoiceProfit = invoiceProfit;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public String getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(String invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public String getInvoiceCost() {
        return invoiceCost;
    }

    public void setInvoiceCost(String invoiceCost) {
        this.invoiceCost = invoiceCost;
    }

    public String getInvoiceProfit() {
        return invoiceProfit;
    }

    public void setInvoiceProfit(String invoiceProfit) {
        this.invoiceProfit = invoiceProfit;
    }
}
