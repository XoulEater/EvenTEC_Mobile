package com.example.eventec.entities;

public class Comment {

    private String comment;
    private String eventId;
    private String timestamp;
    private String userInfo;

    public Comment(String comment, String eventId, String timestamp, String userInfo) {
        this.comment = comment;
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.userInfo = userInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
