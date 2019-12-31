package com.skkily.aishoterclient.LoginUtil;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private int code;

    public User() {
    }

    public User(int code, String username, String password, String email) {
        this.code=code;
        this.username = username;
        this.password = password;
        this.email=email;
    }

    public void setCode(int code){ //code为0时代表示账号登录，为1时代表QQ登录
        this.code=code;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email){
        this.email=email;
    }


    public int getCode(){
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }
}
