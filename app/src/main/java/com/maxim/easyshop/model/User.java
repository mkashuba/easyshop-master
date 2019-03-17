package com.maxim.easyshop.model;

public class User {
    private String email;
    private String username;
    private String password;
    private String uId;

    public User() {
    }

    public User(String email, String username, String password, String uId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uId='" + uId + '\'' +
                '}';
    }
}
