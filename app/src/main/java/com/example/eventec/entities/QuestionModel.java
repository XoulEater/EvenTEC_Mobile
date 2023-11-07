package com.example.eventec.entities;

public class QuestionModel {

    private String title;

    private String question;

    private int value;

    public QuestionModel(String title, String question) {
        this.title = title;
        this.question = question;
        value = 3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
