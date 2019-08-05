package com.example.simpleretailpos.model;

public class InventoryData {

    private Integer id;
    private Integer category_id;
    private String category_name;
    private String barcode;
    private String name;
    private String quantity;
    private String price;
    private String cost;
    private Integer sold_times;
    private String created_at;

    public InventoryData() {
    }

    public InventoryData(Integer id, Integer category_id, String category_name, String barcode, String name, String quantity, String price, String cost, Integer sold_times, String created_at) {
        this.id = id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
        this.sold_times = sold_times;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getSold_times() {
        return sold_times;
    }

    public void setSold_times(Integer sold_times) {
        this.sold_times = sold_times;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
