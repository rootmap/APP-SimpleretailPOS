package com.example.simpleretailpos.model;

public class ExpenseModel {
    private Integer expenseID;
    private String expenseHead;
    private String expenseDate;
    private String expenseDescription;
    private String expenseAmount;
    private String expenseCreatedAt;

    public ExpenseModel() {
    }

    public ExpenseModel(Integer expenseID, String expenseHead, String expenseDate, String expenseDescription, String expenseAmount, String expenseCreatedAt) {
        this.expenseID = expenseID;
        this.expenseHead = expenseHead;
        this.expenseDate = expenseDate;
        this.expenseDescription = expenseDescription;
        this.expenseAmount = expenseAmount;
        this.expenseCreatedAt = expenseCreatedAt;
    }

    public Integer getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(Integer expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseHead() {
        return expenseHead;
    }

    public void setExpenseHead(String expenseHead) {
        this.expenseHead = expenseHead;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCreatedAt() {
        return expenseCreatedAt;
    }

    public void setExpenseCreatedAt(String expenseCreatedAt) {
        this.expenseCreatedAt = expenseCreatedAt;
    }
}
