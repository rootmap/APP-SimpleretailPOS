package com.example.simpleretailpos.model;

public class CustomerData {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String last_invoice_no;
    private String created_at;

    public CustomerData() {
    }

    public CustomerData(String name, String address, String phone, String email, String last_invoice_no, String created_at) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.last_invoice_no = last_invoice_no;
        this.created_at = created_at;

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_invoice_no() {
        return last_invoice_no;
    }

    public void setLast_invoice_no(String last_invoice_no) {
        this.last_invoice_no = last_invoice_no;
    }
}
