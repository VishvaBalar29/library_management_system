package com.library_management.library_management.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserLogin {

    @NotNull(message = "Please Enter Email Id")
    @Email(message = "Please Enter valid Email Id")
    private String email;

    @NotNull(message =  "Please Enter Password")
    private String password;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
