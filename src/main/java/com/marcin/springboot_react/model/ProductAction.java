package com.marcin.springboot_react.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductAction {
    private ProductDTO productDTO;
    private ProductActionType type;
}
