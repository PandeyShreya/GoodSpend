package com.example.myapplication;

public class ExpenseData {
    String date, id, note, expense_category, payment_type;
    int amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpense_category() {
        return expense_category;
    }

    public void setExpense_category(String expense_category) {
        this.expense_category = expense_category;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_mode) {
        this.payment_type = payment_mode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ExpenseData(String date, String id, String note, String expense_category,String payment_type, int amount) {
        this.date = date;
        this.id = id;
        this.note = note;
        this.expense_category = expense_category;
        this.amount = amount;
        this.payment_type = payment_type;
    }

    public ExpenseData(String date, String id, String note, String expense_category, int amount) {
        this.date = date;
        this.id = id;
        this.note = note;
        this.expense_category = expense_category;
        this.amount = amount;
    }
}
