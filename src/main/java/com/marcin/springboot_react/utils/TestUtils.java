package com.marcin.springboot_react.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TestUtils {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthTestUtilityModel getAuthModel(UserDTO userDTO) {
        AuthTestUtilityModel model = new AuthTestUtilityModel();

        try {
            model.setJsonValue(this.objectMapper.writeValueAsString(userDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        model.setUser(userDTO);

        User user = new User();
        user.setUsername(userDTO.getUsername());

        model.setToken(this.jwtUtils.generateToken(user));

        return model;
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
}
