package com.example.eventec.entities;

// Model class for recycler view items
public class ActivityModel {
    private String date;
    private String time;
    private String title;
    private String moder;

    public ActivityModel(String date, String time, String title, String moder) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.moder = moder;
    }

    // Getter and setter methods
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }
}
