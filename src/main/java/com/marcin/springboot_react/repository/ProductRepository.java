package com.marcin.springboot_react.repository;

import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByUser(User user);

    void deleteByProductIdAndUser(Long id, User user);
}
