package com.marcin.springboot_react.service;

import com.marcin.springboot_react.exception.ProductAlreadyExistsException;
import com.marcin.springboot_react.exception.ProductNotFoundException;
import com.marcin.springboot_react.mapper.ProductMapper;
import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProductsForUser(User user) {
        return this.productRepository.findProductsByUser(user);
    }

    @Override
    public Product addNewProduct(ProductDTO productDTO, User user) {
        productDTO.setUser(user);
        Product product = ProductMapper.PRODUCT_MAPPER.mapProductDTOToProduct(productDTO);

        Optional<Product> maybeProductWithGivenName = this.productRepository.findProductsByUser(user).stream()
                .filter(p -> p.getName().equalsIgnoreCase(product.getName()))
                .findAny();

        if (maybeProductWithGivenName.isPresent()) {
            log.info("Product with name: '{}' already exists", product.getName());
            throw new ProductAlreadyExistsException(String.format("Product with name: '%s' already exists in users: '%s' cart",
                    product.getName(), user.getUsername()));
        }

        log.info("Adding new product: {} to list", product);
        return this.productRepository.save(product);
    }

    @Override
    public Product modifyProduct(Long id, ProductDTO productDTO, User user) {
        log.info("Modifying product: {}", productDTO);
        if (productDTO.getProductId() == null) {
            log.error("Product has no ID");
            return null;
        }

        Product mappedFromDTO = ProductMapper.PRODUCT_MAPPER.mapProductDTOToProduct(productDTO);

        log.info("Data to update: {}", mappedFromDTO);
        Product product = this.productRepository.findProductsByUser(user).stream()
                .filter(prod -> prod.getProductId().equals(id))
                .findFirst().orElseThrow(() -> new ProductNotFoundException(String.format("Product with given ID: %d not found", id)));

        product.setQuantity(mappedFromDTO.getQuantity());
        product.setPrice(mappedFromDTO.getPrice());

        product = this.productRepository.save(product);
        log.info("Updated product: {}", product);

        return product;
    }

    @Override
    public void delete(Long id, User user) {
        log.info("Removing product with ID: {} from users: '{}' cart", id, user.getUsername());
        this.productRepository.deleteByProductIdAndUser(id, user);
    }

    @Override
    public List<Product> getAll(User user) {
        log.info("Fetching all products from the database");
        return this.productRepository.findAll();
    }
}
