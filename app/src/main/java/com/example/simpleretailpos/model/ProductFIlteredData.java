package com.example.simpleretailpos.model;

public class ProductFIlteredData {
    private Integer id;
    private String name;
    private String catName;
    private String price;

    public ProductFIlteredData() {
    }

    public ProductFIlteredData(Integer id, String name, String catName, String price) {
        this.id = id;
        this.name = name;
        this.catName = catName;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
