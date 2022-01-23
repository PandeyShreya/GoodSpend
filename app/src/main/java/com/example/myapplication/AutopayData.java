package com.example.myapplication;

public class AutopayData {
    String category, id, note, date_limit, paymentType, repeatTransaction, time_limit;
    double amount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDate_limit() {
        return date_limit;
    }

    public void setDate_limit(String date_limit) {
        this.date_limit = date_limit;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String date_limit) {
        this.time_limit = time_limit;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRepeatTransaction() {
        return repeatTransaction;
    }

    public void setRepeatTransaction(String repeatTransaction) {
        this.repeatTransaction = repeatTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AutopayData(String category, String id, String note, String date_limit, String time_limit, String paymentType, String repeatTransaction, double amount) {
        this.category = category;
        this.id = id;
        this.note = note;
        this.date_limit = date_limit;
        this.paymentType = paymentType;
        this.repeatTransaction = repeatTransaction;
        this.amount = amount;
        this.time_limit = time_limit;
    }

    public AutopayData() {
    }
}
