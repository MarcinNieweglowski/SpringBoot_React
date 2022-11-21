package com.marcin.springboot_react.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String password;
    private UserLevel userLevel = UserLevel.USER;
    private String token;
}
