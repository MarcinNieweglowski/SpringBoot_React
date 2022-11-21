package com.marcin.springboot_react.service;

import com.marcin.springboot_react.exception.UserNotFoundException;
import com.marcin.springboot_react.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers(User user);

    User findByUsername(String username) throws UserNotFoundException;

    User updateUser(User modifiedUser) throws UserNotFoundException;

    void deleteUser(User userToDelete);
}
