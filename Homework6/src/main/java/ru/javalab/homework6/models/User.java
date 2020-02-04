package ru.javalab.homework6.models;

public class User {
    private String login;
    private String password;
    private int id;
    private boolean isAdmin;

    public User(String login, int id, boolean isAdmin) {
        this.login = login;
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public User(String login, String password, int id) {
        this(login, password);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
