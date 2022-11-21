package com.marcin.springboot_react.controller.rest;

import com.marcin.springboot_react.exception.UserNotFoundException;
import com.marcin.springboot_react.model.AuthenticationResponse;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserDTO;
import com.marcin.springboot_react.repository.UserRepository;
import com.marcin.springboot_react.service.UserService;
import com.marcin.springboot_react.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private JwtUtils jwtUtils;

    private UserService userService;

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody UserDTO userDTO) {
        User newUser = new User();
        newUser.setPassword(userDTO.getPassword());
        newUser.setUsername(userDTO.getUsername());

        newUser = this.userService.createUser(newUser);

        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserDTO userDTO) {
        User user;
        try {
            user = this.userService.findByUsername(userDTO.getUsername());
        } catch (UserNotFoundException e) {
            log.error("User with name: '{}' does not exist", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Found the user: '{}' in the database, access level: '{}'", user.getUsername(), user.getUserLevel());

        if (user.getPassword().equals(userDTO.getPassword())) {
            AuthenticationResponse responseUserData = prepareResponseUserData(user);
            return ResponseEntity.ok().body(responseUserData);
        } else {
            log.info("Invalid credentials for the given user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    private AuthenticationResponse prepareResponseUserData(User user) {
        String token = this.jwtUtils.generateToken(user);
        log.info("Token generated: '{}'", token);

        AuthenticationResponse responseUserData = new AuthenticationResponse();
        responseUserData.setUserLevel(user.getUserLevel());
        responseUserData.setUsername(user.getUsername());
        responseUserData.setToken(String.format("Bearer %s", token));
        return responseUserData;
    }
}
