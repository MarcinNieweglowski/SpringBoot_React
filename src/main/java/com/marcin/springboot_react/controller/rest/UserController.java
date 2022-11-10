package com.marcin.springboot_react.controller.rest;

import com.marcin.springboot_react.exception.InsufficientUserLevelException;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserLevel;
import com.marcin.springboot_react.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(@RequestAttribute("user") User user) throws InsufficientUserLevelException {
        validateUser(user);
        return this.userService.getAllUsers(user);
    }

    @PutMapping
    public User updateUser(@RequestAttribute("user") User user, @RequestBody User modifiedUser) throws InsufficientUserLevelException {
        validateUser(user);
        return this.userService.updateUser(modifiedUser);
    }

    @DeleteMapping
    public void deleteUser(@RequestAttribute("user") User user, User userToDelete) throws InsufficientUserLevelException {
        validateUser(user);
        this.userService.deleteUser(userToDelete);
    }

    private void validateUser(User user) throws InsufficientUserLevelException {
        if (user.getUserLevel() != UserLevel.ADMIN) {
            throw new InsufficientUserLevelException(
                    String.format("User: '%s' has insufficient rights to perform this operation", user));
        }
    }
}
