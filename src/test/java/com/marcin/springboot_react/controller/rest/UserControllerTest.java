package com.marcin.springboot_react.controller.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcin.springboot_react.utils.AuthTestUtilityModel;
import com.marcin.springboot_react.utils.TestUtils;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserDTO;
import com.marcin.springboot_react.model.UserLevel;
import com.marcin.springboot_react.service.UserService;
import com.marcin.springboot_react.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserDTO existingAdmin = getExistingAdminUser();

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllUsersShouldReturnAListOfAllUsersInTheSystem() throws Exception {
        User existingAdminAsUser = new User();
        existingAdminAsUser.setUsername(this.existingAdmin.getUsername());

        String token = this.jwtUtils.generateToken(existingAdminAsUser);

        MvcResult result = this.mvc.perform(buildRequest(get("/user"), token)).andReturn();

        List<User> users = this.mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>() {});

        Assertions.assertTrue(!users.isEmpty());
    }

    @Test
    public void getAllUsersShouldFailIfTheTheUserIsNotAdminLevel() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(userDTO);

        this.mvc.perform(buildRequest(get("/user"), model.getToken())
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void getAllUsersShouldFailIfTheTheUserDoesNotExist() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("does not exist");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(userDTO);

        this.mvc.perform(buildRequest(get("/user"), model.getToken())).andExpect(status().isNotFound());
    }

    @Test
    public void getAllUsersShouldFailIfTheThereIsNoValidBearerTokenAttachedToRequest() throws Exception {
        this.mvc.perform(buildRequest(get("/user"), ""))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAllUsersShouldFailIfTheThereIsNoTokenAttachedToRequest() throws Exception {
        this.mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUserShouldUpdateTheUserPassedIn() throws Exception {
        User modifiedUser = createNewUser("dummy", UserLevel.ADMIN);
        modifiedUser.setUsername("a new one");
        modifiedUser.setPassword("a different one");

        AuthTestUtilityModel model = this.testUtils.getAuthModel(this.existingAdmin);

        MvcResult result = this.mvc.perform(buildRequest(put("/user"), model.getToken())
                .content(this.mapper.writeValueAsString(modifiedUser))
        ).andReturn();

        User user = this.mapper.readValue(result.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals(modifiedUser.getUsername(), user.getUsername());
        Assertions.assertEquals(modifiedUser.getPassword(), user.getPassword());
        Assertions.assertEquals(modifiedUser.getUserLevel(), user.getUserLevel());
        Assertions.assertEquals(modifiedUser.getUserId(), user.getUserId());

        deleteUser(modifiedUser);
    }

    @Test
    public void updateUserShouldFailIfTheUserIsNotAdminLevel() throws Exception {
        User modifiedUser = createNewUser("username_X", UserLevel.USER);
        modifiedUser.setUserId(12345L);

        UserDTO notAdmin = new UserDTO();
        notAdmin.setUsername(modifiedUser.getUsername());
        notAdmin.setUserLevel(modifiedUser.getUserLevel());

        AuthTestUtilityModel model = this.testUtils.getAuthModel(notAdmin);

        this.mvc.perform(buildRequest(put("/user"), model.getToken())
                .content(this.mapper.writeValueAsString(modifiedUser))
        ).andExpect(status().is4xxClientError());

        deleteUser(modifiedUser);
    }

    @Test
    public void deleteUserShouldDeleteTheUser() throws Exception {
        AuthTestUtilityModel model = this.testUtils.getAuthModel(this.existingAdmin);
        User userToDelete = createNewUser("dummy", UserLevel.ADMIN);

        this.mvc.perform(buildRequest(delete("/user"), model.getToken())
                .content(this.mapper.writeValueAsString(userToDelete))
        ).andExpect(status().isOk());

        deleteUser(userToDelete);
    }

    @Test
    public void deleteUserShouldFailIfTheUserIsNotAdminLevel() throws Exception {
        User userToDelete = createNewUser("dummy", UserLevel.ADMIN);

        UserDTO notAdmin = new UserDTO();
        notAdmin.setUsername("not admin here!");
        notAdmin.setUserLevel(UserLevel.USER);

        AuthTestUtilityModel model = this.testUtils.getAuthModel(notAdmin);

        this.mvc.perform(buildRequest(delete("/user"), model.getToken())
                .content(this.mapper.writeValueAsString(userToDelete))
        ).andExpect(status().is4xxClientError());

        deleteUser(userToDelete);
    }

    private MockHttpServletRequestBuilder buildRequest(MockHttpServletRequestBuilder request, String model) {
        return request
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestUtils.AUTHORIZATION_HEADER, String.format("Bearer %s", model));
    }

    private User createNewUser(String username, UserLevel userLevel) {
        User user = new User();
        user.setUserId(999L);
        user.setUsername(username);
        user.setUserLevel(userLevel);
        user.setProductList(new ArrayList<>());
        user.setPassword("dummy");

        return this.userService.createUser(user);
    }

    private void deleteUser(User user) {
        this.userService.deleteUser(user);
    }

    private UserDTO getExistingAdminUser() {
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setUsername("Marcin");
        user.setPassword("marcin");
        user.setUserLevel(UserLevel.ADMIN);
        return user;
    }
}