package com.example.eventec.entities;

import java.util.ArrayList;

public class EventModel {
    private String eventName;
    private String date;
    private String asoName;
    private int capacity;
    private int eventImage;
    private ArrayList<ActivityModel> activityModelArrayList;
    private ArrayList<CollabModel> collabModelArrayList;


    public EventModel(String eventName, String date, String asoName, int capacity, int eventImage) {
        this.eventName = eventName;
        this.date = date;
        this.asoName = asoName;
        this.capacity = capacity;
        this.eventImage = eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAsoName() {
        return asoName;
    }

    public void setAsoName(String asoName) {
        this.asoName = asoName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEventImage() {
        return eventImage;
    }

    public void setEventImage(int eventImage) {
        this.eventImage = eventImage;
    }



}

