package com.marcin.springboot_react.utils;

import com.marcin.springboot_react.model.UserDTO;

public class AuthTestUtilityModel {
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    private UserDTO user;
    private String token;
    private String jsonValue;
}

