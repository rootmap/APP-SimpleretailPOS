package com.example.simpleretailpos.model;

public class PaymentModel {
    private Integer paymentID;
    private String paymentDate;
    private String customerName;
    private String tenderName;
    private String paidAmount;
    private String invoiceID;

    public PaymentModel() {
    }

    public PaymentModel(Integer paymentID, String paymentDate, String customerName, String tenderName, String paidAmount, String invoiceID) {
        this.paymentID = paymentID;
        this.paymentDate = paymentDate;
        this.customerName = customerName;
        this.tenderName = tenderName;
        this.paidAmount = paidAmount;
        this.invoiceID = invoiceID;
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }
}
