package ru.javalab.homework4.models;

import java.util.Date;

public class Message {
    private String date;
    private String time;
    private User user;
    private String message;

    public String getDate() {
        return date;
    }


    public void setDate(Date date) {
        String[] strings = date.toLocaleString().split(" ")[0].split("\\.");
        String result = strings[0] + "-" + strings[1] + "-" + strings[2];
        this.date = result;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(Date time) {
        String[] strings = time.toLocaleString().split(" ")[1].split(":");
        String result = strings[0] + ":" + strings[1] + ":" + strings[2];
        this.time = result;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(Date date, User user, String message) {
        this.setDate(date);
        this.setTime(date);
        this.user = user;
        this.message = message;
    }

}
