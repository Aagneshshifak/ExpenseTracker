package com.model;

import java.time.LocalDate;

public class Expense {
    private int expense_id;
    private int category_id;
    private PaymentMethod paymentMethod;
    private int amount;
    private String description;
    private LocalDate expense_date;
    private LocalDate created_at;

    public Expense(){
        this.created_at = LocalDate.now();
    }

    public Expense(int expense_id, int category_id, PaymentMethod paymentMethod, int amount) {
        this.expense_id = expense_id;
        this.category_id = category_id;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(LocalDate expense_date) {
        this.expense_date = expense_date;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }
}
