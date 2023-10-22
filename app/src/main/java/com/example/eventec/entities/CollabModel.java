package com.example.eventec.entities;

public class CollabModel {
    private String name;
    private String job;
    private int profileImage;

    public CollabModel(String name, String job, int profileImage) {
        this.name = name;
        this.job = job;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
