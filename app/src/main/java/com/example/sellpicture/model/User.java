package com.example.sellpicture.model;


public class User {
    private int user_id;
    private String name;
    private String email;
    private String phone;

    public User(int user_id,String name, String email, String phone) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
