package com.example.eventec.entities;

import java.util.ArrayList;

public class EventModel {
    private String eventName;
    private String date;
    private String asoName;
    private int capacity;
    private int eventImage;
    private ArrayList<String> categories;
    private String description;
    private String requirements;
    private String startDate;
    private String endDate;
    private int places;
    private ArrayList<ActivityModel> activityModelArrayList;
    private ArrayList<CollabModel> collabModelArrayList;

    public EventModel(String eventName, String asoName, int capacity, int eventImage, ArrayList<String> categories, String description, String requirements, String startDate, String endDate) {
        this.eventName = eventName;
        this.date = "Fecha";
        this.asoName = asoName;
        this.capacity = capacity;
        this.eventImage = eventImage;
        this.categories = categories;
        this.description = description;
        this.requirements = requirements;
        this.startDate = startDate;
        this.endDate = endDate;
        this.places = 0;
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


    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public ArrayList<ActivityModel> getActivityModelArrayList() {
        return activityModelArrayList;
    }

    public void setActivityModelArrayList(ArrayList<ActivityModel> activityModelArrayList) {
        this.activityModelArrayList = activityModelArrayList;
    }

    public ArrayList<CollabModel> getCollabModelArrayList() {
        return collabModelArrayList;
    }

    public void setCollabModelArrayList(ArrayList<CollabModel> collabModelArrayList) {
        this.collabModelArrayList = collabModelArrayList;
    }
}

