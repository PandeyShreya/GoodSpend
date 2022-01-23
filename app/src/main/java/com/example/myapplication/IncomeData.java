package com.example.myapplication;

public class IncomeData {
    String date,id, note, income_category;
    int amount;

    public IncomeData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return income_category;
    }

    public void setCategory(String category) {
        this.income_category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public IncomeData(String date, String id, String note, String category, int amount) {
        this.date = date;
        this.id = id;
        this.note = note;
        this.income_category = category;
        this.amount = amount;
    }

}
