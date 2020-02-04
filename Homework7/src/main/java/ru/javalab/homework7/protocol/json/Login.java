package ru.javalab.homework7.protocol.json;

public class Login extends Payload {
    String login;
    String password;
  
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
}
