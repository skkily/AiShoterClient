package com.skkily.aishoterclient.LoginUtil;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String userid;
    private String password;
    private String email;
    private int code;

    public User(int code,String userid,String password,  String email ,String username) {
        this.username = username;
        this.userid = userid;
        this.password = password;
        this.email = email;
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", code=" + code +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
