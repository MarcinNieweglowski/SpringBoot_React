package com.marcin.springboot_react.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcin.springboot_react.utils.AuthTestUtilityModel;
import com.marcin.springboot_react.utils.TestUtils;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserDTO;
import com.marcin.springboot_react.model.UserLevel;
import com.marcin.springboot_react.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestUtils testUtils;

    @Test
    public void createAccountShouldAddANewAccountToTheDatabase() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("Test");
        user.setPassword("Test");
        user.setUserLevel(UserLevel.USER);

        AuthTestUtilityModel model = this.testUtils.getAuthModel(user);

        this.mvc.perform(buildRequest("/auth/create-account", model))
                .andExpect(status().isOk());

        User foundUser = this.userService.findByUsername(user.getUsername());
        Assertions.assertEquals(foundUser.getUserLevel(), user.getUserLevel());
        Assertions.assertEquals(foundUser.getPassword(), user.getPassword());
        Assertions.assertTrue(foundUser.getProductList().isEmpty());

        this.userService.deleteUser(foundUser);
    }

    @Test
    public void loginShouldLogTheUserInIfTheUserPassedInCorrectCredentials() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("Marcin");
        user.setPassword("marcin");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(user);

        this.mvc.perform(buildRequest("/auth/login", model))
                .andExpect(status().isOk());

        User foundUser = this.userService.findByUsername(user.getUsername());
        Assertions.assertTrue(foundUser.getUserLevel().equals(UserLevel.ADMIN));
        Assertions.assertEquals(foundUser.getPassword(), user.getPassword());
        Assertions.assertTrue(!foundUser.getProductList().isEmpty());
    }

    @Test
    public void loginShouldNotLogTheUserInIfTheUserPassedInCredentialsInWrongCase() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("Marcin");
        user.setPassword("MARCIN");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(user);

        this.mvc.perform(buildRequest("/auth/login", model))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void loginShouldNotLogTheUserInIfTheUserPassedInIncorrectCredentials() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("wrong");
        user.setPassword("wrong");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(user);

        this.mvc.perform(buildRequest("/auth/login", model))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void loginShouldNotLogTheUserInIfTheUserPassedInNoUsernameAndPassword() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("");
        user.setPassword("");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(user);

        this.mvc.perform(buildRequest("/auth/login", model))
                .andExpect(status().is4xxClientError());
    }

    private MockHttpServletRequestBuilder buildRequest(String urlTemplate, AuthTestUtilityModel model) {
        return post(urlTemplate, model.getUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(model.getJsonValue());
    }
}