package com.example.eventec.entities;

public class AlertModel {
    private String subject;
    private String body;
    private String postdate;
    private int alertImage;

    public AlertModel(String subject, String body, String postdate, int alertImage) {
        this.subject = subject;
        this.body = body;
        this.postdate = postdate;
        this.alertImage = alertImage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public int getAlertImage() {
        return alertImage;
    }

    public void setAlertImage(int alertImage) {
        this.alertImage = alertImage;
    }
}
