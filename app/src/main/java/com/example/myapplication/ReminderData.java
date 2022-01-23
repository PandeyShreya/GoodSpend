package com.example.myapplication;

public class ReminderData {
    String category, id, note, date, time, repeatReminder;
    double amount;

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeatReminder() {
        return repeatReminder;
    }

    public void setRepeatReminder(String repeatReminder) {
        this.repeatReminder = repeatReminder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ReminderData(String id) {
        this.id = id;
    }

    public ReminderData(String category, String id, String note, String date, String time, String repeatReminder, double amount) {
        this.category = category;
        this.note = note;
        this.date = date;
        this.time = time;
        this.repeatReminder = repeatReminder;
        this.amount = amount;
        this.id = id;
    }
    public ReminderData(){

    }

}
