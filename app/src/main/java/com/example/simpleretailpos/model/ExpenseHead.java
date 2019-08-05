package com.example.simpleretailpos.model;

public class ExpenseHead {
    private Integer id;
    private String name;

    public ExpenseHead() {
    }

    public ExpenseHead(Integer id, String name) {
        this.id = id;
        this.name = name;
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
}
