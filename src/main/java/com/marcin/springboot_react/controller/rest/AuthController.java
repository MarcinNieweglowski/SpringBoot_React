package com.marcin.springboot_react.controller.rest;

import com.marcin.springboot_react.model.AuthenticationResponse;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserDTO;
import com.marcin.springboot_react.repository.UserRepository;
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

    private UserRepository userRepository;

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody UserDTO userDTO) {
        User newUser = new User();
        newUser.setPassword(userDTO.getPassword());
        newUser.setUsername(userDTO.getUsername());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserDTO userDTO) throws Exception {
        Optional<User> maybeUser = userRepository.findByUsername(userDTO.getUsername());

        if (maybeUser.isEmpty()) {
            log.error("User with name: '{}' does not exist", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = maybeUser.get();
        log.info("Found the user: '{}' in the database, access level: '{}'", user.getUsername(), user.getUserLevel());

        if (user.getPassword().equals(userDTO.getPassword())) {
            String token = this.jwtUtils.generateToken(user);
            log.info("Token generated: '{}'", token);

            AuthenticationResponse responseUserData = new AuthenticationResponse();
            responseUserData.setUserLevel(user.getUserLevel());
            responseUserData.setUsername(user.getUsername());
            responseUserData.setToken(String.format("Bearer %s", token));

            return ResponseEntity.ok().body(responseUserData);
        } else {
            log.info("Invalid credentials for the given user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
