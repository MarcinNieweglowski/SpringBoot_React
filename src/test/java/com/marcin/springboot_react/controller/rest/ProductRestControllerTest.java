package com.marcin.springboot_react.controller.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.service.ProductService;
import com.marcin.springboot_react.service.UserService;
import com.marcin.springboot_react.utils.JwtUtils;
import com.marcin.springboot_react.utils.TestUtils;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductRestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ProductService productService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllProductsForUserShouldReturnAllProductsForThatUser() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        MvcResult result = this.mvc.perform(buildRequest(get("/products"), token)
        ).andReturn();

        List<Product> products = this.mapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {});

        Assertions.assertTrue(!products.isEmpty());
    }

    @Test
    public void addNewProductShouldAddANewProductToGivenUsersListOfProducts() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        ProductDTO product = new ProductDTO();
        product.setQuantity(1);
        product.setPrice(12.34);
        product.setName("Some Dummy Test Product");
        product.setUser(user);

        MvcResult result = this.mvc.perform(buildRequest(post("/products/new"), token)
                .content(this.mapper.writeValueAsString(product))
        ).andReturn();

        Product fetchedProduct = this.mapper.readValue(result.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product.getName(), fetchedProduct.getName());
        Assertions.assertEquals(product.getQuantity(), fetchedProduct.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(product.getPrice()), fetchedProduct.getPrice());
        Assertions.assertEquals(product.getUser(), user);
        Assertions.assertTrue(fetchedProduct.getProductId() != null);

        deleteProduct(user, fetchedProduct);
    }

    @Test
    public void addNewProductShouldFailIfTheUserWantsToAddANewProductWithAnAlreadyTakenProductName() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        Assertions.assertTrue(!user.getProductList().isEmpty());

        ProductDTO product = new ProductDTO();
        product.setQuantity(1);
        product.setPrice(99999.99);
        product.setName(user.getProductList().get(0).getName());
        product.setUser(user);

        this.mvc.perform(buildRequest(post("/products/new"), token)
                .content(this.mapper.writeValueAsString(product))
        ).andExpect(status().is4xxClientError());

        MvcResult result = this.mvc.perform(buildRequest(get("/products"), token)
        ).andReturn();

        List<Product> products = this.mapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {});

        Optional<BigDecimal> oldProductPrice = products.stream()
                .filter(p -> p.getName().equals(product.getName()))
                .map(Product::getPrice)
                .findFirst();

        Assertions.assertNotEquals(oldProductPrice.get(), BigDecimal.valueOf(product.getPrice()));
    }

    @Test
    public void modifyProductShouldReturnEmptyStringIfThePassedInProductDoesNotContainTheId() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        ProductDTO product = new ProductDTO();
        product.setQuantity(1);
        product.setPrice(99999.99);
        product.setName("Milk");
        product.setUser(user);

        MvcResult result = this.mvc.perform(buildRequest(put("/products/edit/{id}", 123), token)
                .content(this.mapper.writeValueAsString(product))
        ).andReturn();

        Assertions.assertEquals(result.getResponse().getContentAsString(), "");
    }

    @Test
    public void modifyProductShouldFailIfTheProductPassedInHasIdButIsNotInTheDatabase() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        ProductDTO product = new ProductDTO();
        product.setQuantity(1);
        product.setPrice(99999.99);
        product.setName("Milk");
        product.setUser(user);
        product.setProductId("123");

        this.mvc.perform(buildRequest(put("/products/edit/{id}", 123), token)
                .content(this.mapper.writeValueAsString(product))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductShouldDeleteTheGivenProduct() throws Exception {
        User user = this.userService.findByUsername("Marcin");
        String token = this.jwtUtils.generateToken(user);

        ProductDTO product = new ProductDTO();
        product.setQuantity(1);
        product.setPrice(12.34);
        product.setName("Some Dummy Test Product");
        product.setUser(user);

        Product addedProduct = this.productService.addNewProduct(product, user);

        this.mvc.perform(buildRequest(delete("/products/{id}", addedProduct.getProductId()), token)
        ).andExpect(status().isOk());

        List<Product> allProductsForUser = this.productService.getAllProductsForUser(user);

        Optional<Product> shouldBeEmpty = allProductsForUser.stream()
                .filter(p -> p.getProductId().equals(addedProduct.getProductId()))
                .findAny();

        Assertions.assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    public void downloadForUserShould() {

    }

    private MockHttpServletRequestBuilder buildRequest(MockHttpServletRequestBuilder request, String token) {
        return request
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestUtils.AUTHORIZATION_HEADER, String.format("Bearer %s", token));
    }

    private void deleteProduct(User user, Product fetchedProduct) {
        this.productService.delete(fetchedProduct.getProductId(), user);
    }

}