package com.example.quickshop.model;

public class User_Detail {

    private String email,phone,name;

    public User_Detail() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User_Detail(String email, String phone, String name) {
        this.email = email;
        this.phone = phone;
        this.name = name;
    }
}
