package ru.javalab.homework5.models;

import java.util.Date;

public class Message extends Payload {
    private String date;
    private String time;
    private int id;

    public Message(String date, String time,String message, User user) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Message(Date date, String message) {
        this.setDate(date);
        this.setTime(date);
        this.message = message;
    }

    public Message(Date date, User user, String message) {
        this.setDate(date);
        this.setTime(date);
        this.user = user;
        this.message = message;
    }

    public Message(String date, String time, User user, String message) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.message = message;
    }

    @Override
    public String toString() {
        if (id == 0) {
            return "[" + this.user.getLogin() + "] " + date + " " + time + " : " + message;
        } else {
            return "[" + id + "] " + date + " " + time + " : " + message;
        }
    }
}
