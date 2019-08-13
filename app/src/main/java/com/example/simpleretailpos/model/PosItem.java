package com.example.simpleretailpos.model;

public class PosItem {

    private Integer itemId;
    private String itemName;
    private String itemPrice;
    private String cateGoryName;
    private Integer itemQuantity;

    public PosItem() {  }

    public PosItem(Integer itemId, String itemName, String itemPrice, String cateGoryName, Integer itemQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.cateGoryName = cateGoryName;
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getCateGoryName() {
        return cateGoryName;
    }

    public void setCateGoryName(String cateGoryName) {
        this.cateGoryName = cateGoryName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
