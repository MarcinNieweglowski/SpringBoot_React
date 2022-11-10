package com.marcin.springboot_react.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "user_table")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private UserLevel userLevel = UserLevel.USER;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
//    @JsonIgnore
    private List<Product> productList;

    @Override
    public String toString() {
        return "User[username=" + this.getUsername() + ", userLevel=" + this.getUserLevel() + "]";
    }
}
