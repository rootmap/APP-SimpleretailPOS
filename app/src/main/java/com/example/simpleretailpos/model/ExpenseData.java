package com.example.simpleretailpos.model;

public class ExpenseData {

    private Integer id;
    private Integer expense_id;
    private String expense_name;
    private String expense_description;
    private String expense_date;
    private String expense_amount;
    private String created_at;

    public ExpenseData() {
    }

    public ExpenseData(Integer id, Integer expense_id, String expense_name, String expense_description, String expense_date, String expense_amount, String created_at) {
        this.id = id;
        this.expense_id = expense_id;
        this.expense_name = expense_name;
        this.expense_description = expense_description;
        this.expense_date = expense_date;
        this.expense_amount = expense_amount;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(Integer expense_id) {
        this.expense_id = expense_id;
    }

    public String getExpense_name() {
        return expense_name;
    }

    public void setExpense_name(String expense_name) {
        this.expense_name = expense_name;
    }

    public String getExpense_description() {
        return expense_description;
    }

    public void setExpense_description(String expense_description) {
        this.expense_description = expense_description;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public String getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(String expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
