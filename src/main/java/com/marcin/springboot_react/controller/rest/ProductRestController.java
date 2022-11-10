package com.marcin.springboot_react.controller.rest;

import com.marcin.springboot_react.exception.InsufficientUserLevelException;
import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.model.UserLevel;
import com.marcin.springboot_react.service.PrinterService;
import com.marcin.springboot_react.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductRestController {

    private ProductService productService;

    private PrinterService printerService;

    @GetMapping
    public List<Product> getAllProductsForUser(@RequestAttribute("user") User user) {
        log.info("Getting all products for user: '{}'", user.getUsername());
        List<Product> products = this.productService.getAllProducts(user);
        log.info("Returning list of all({}) products: {}", products.size(), products);
        return products;
    }

    @GetMapping("/get-all")
    public List<Product> getAllProducts(@RequestAttribute("user") User user) throws InsufficientUserLevelException {
        if (!user.getUserLevel().equals(UserLevel.ADMIN)) {
            throw new InsufficientUserLevelException(
                    String.format("The user: '%s' does not have sufficient user level", user.getUsername()));
        }

        return this.productService.getAll(user);
    }

    @PostMapping("/new")
    public Product addNewProduct(@RequestBody ProductDTO product, @RequestAttribute("user") User user) {
        log.info("Controller - received product: {}", product);
        return this.productService.addNewProduct(product, user);
    }

    @PutMapping("/edit/{id}")
    public Product modifyProduct(@PathVariable("id") Long id, @RequestBody ProductDTO modifiedProduct,
                                 @RequestAttribute("user") User user) {
        return this.productService.modifyProduct(id, modifiedProduct, user);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id, @RequestAttribute("user") User user) {
        this.productService.delete(id, user);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadForUser(@RequestAttribute("user") User user) throws IOException {
        List<Product> products = this.productService.getAllProducts(user);
        File file = this.printerService.download(products);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity
                .ok()
                .headers(getHttpHeaders(file))
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private HttpHeaders getHttpHeaders(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", file.getName()));
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        return headers;
    }
}
