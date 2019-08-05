package com.example.simpleretailpos.model;

public class CustomerLeadData {

    private Integer id;
    private Integer customer_id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String created_at;
    private String lead_info;

    public CustomerLeadData() {
    }

    public CustomerLeadData(Integer id, Integer customer_id, String name, String address, String phone, String email, String created_at, String lead_info) {
        this.id = id;
        this.customer_id = customer_id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.created_at = created_at;
        this.lead_info = lead_info;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLead_info() {
        return lead_info;
    }

    public void setLead_info(String lead_info) {
        this.lead_info = lead_info;
    }
}
