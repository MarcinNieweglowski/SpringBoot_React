package com.marcin.springboot_react.service;

import com.marcin.springboot_react.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(User user);

    User findByUsername(String username);

    User updateUser(User modifiedUser);

    void deleteUser(User userToDelete);
}
