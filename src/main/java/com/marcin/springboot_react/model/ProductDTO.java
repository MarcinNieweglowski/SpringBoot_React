package com.marcin.springboot_react.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDTO {

    private String productId;
    private String name;
    private Double price;
    private int quantity;
    private User user;
}
