package com.example.simpleretailpos.model;

public class SalesData {

    private Integer id;
    private Integer invoice_id;
    private String invoice_date;
    private String sold_to;
    private String tender;
    private String payment_status;
    private String invoice_total;


    public SalesData() {
    }

    public SalesData(Integer id, Integer invoice_id, String invoice_date, String sold_to, String tender, String payment_status, String invoice_total) {
        this.id = id;
        this.invoice_id = invoice_id;
        this.invoice_date = invoice_date;
        this.sold_to = sold_to;
        this.tender = tender;
        this.payment_status = payment_status;
        this.invoice_total = invoice_total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(Integer invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getSold_to() {
        return sold_to;
    }

    public void setSold_to(String sold_to) {
        this.sold_to = sold_to;
    }

    public String getTender() {
        return tender;
    }

    public void setTender(String tender) {
        this.tender = tender;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getInvoice_total() {
        return invoice_total;
    }

    public void setInvoice_total(String invoice_total) {
        this.invoice_total = invoice_total;
    }
}
