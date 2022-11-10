package com.marcin.springboot_react.service;

import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import com.marcin.springboot_react.model.User;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts(User user);

    Product addNewProduct(ProductDTO product, User user);

    Product modifyProduct(Long id, ProductDTO product, User user);

    void delete(Long id, User user);

    List<Product> getAll(User user);
}
