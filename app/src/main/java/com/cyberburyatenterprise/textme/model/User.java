package com.cyberburyatenterprise.textme.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class User extends BaseObservable {

    private String avatar;

    private String name;

    private String email;

    private String password;

    public User(String avatar, String name, String email, String password) {
        this.avatar = avatar;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
