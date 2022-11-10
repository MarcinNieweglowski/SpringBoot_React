package com.marcin.springboot_react.configuration;

import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserLevel;
import com.marcin.springboot_react.repository.ProductRepository;
import com.marcin.springboot_react.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DatabasePopulator implements CommandLineRunner {

    private UserRepository userRepository;

    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!this.userRepository.findAll().isEmpty()) {
            log.info("Found users in the database. Skipping initial DB population.");
            return;
        }

        log.info("Populating DB with dummy data...");

        addUsers();
        addProducts();

        log.info("DB data populated");
    }

    private void addProducts() {
        Product p1 = new Product();
        p1.setProductId(1L);
        p1.setName("Bread");
        p1.setPrice(BigDecimal.valueOf(3.11));
        p1.setQuantity(1);

        Product p2 = new Product();
        p2.setProductId(2L);
        p2.setName("Milk");
        p2.setPrice(BigDecimal.valueOf(3.59));
        p2.setQuantity(12);

        Product p3 = new Product();
        p2.setProductId(3L);
        p2.setName("Milk");
        p2.setPrice(BigDecimal.valueOf(3.59));
        p2.setQuantity(2);

        User marcin = this.userRepository.findByUsername("Marcin").get();
        User user = this.userRepository.findByUsername("user").get();

        p1.setUser(marcin);
        p2.setUser(marcin);
        p3.setUser(user);

        this.productRepository.save(p1);
        this.productRepository.save(p2);
        this.productRepository.save(p3);
    }

    private void addUsers() {
        User marcin = new User();
        marcin.setUsername("Marcin");
        marcin.setPassword("marcin");
        marcin.setUserLevel(UserLevel.ADMIN);

        this.userRepository.save(marcin);

        User user = new User();
        user.setUsername("user");
        user.setPassword("dupa");

        this.userRepository.save(user);
        log.info("Added 2 users to database");
    }
}
