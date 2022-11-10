package com.marcin.springboot_react.mapper;

import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-10T13:57:29+0100",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.1.jar, environment: Java 11 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO mapProductToProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        if ( product.getProductId() != null ) {
            productDTO.setProductId( String.valueOf( product.getProductId() ) );
        }
        if ( product.getPrice() != null ) {
            productDTO.setPrice( product.getPrice().doubleValue() );
        }
        productDTO.setQuantity( product.getQuantity() );
        productDTO.setName( product.getName() );
        productDTO.setUser( product.getUser() );

        return productDTO;
    }

    @Override
    public Product mapProductDTOToProduct(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        if ( productDTO.getProductId() != null ) {
            product.setProductId( Long.parseLong( productDTO.getProductId() ) );
        }
        if ( productDTO.getPrice() != null ) {
            product.setPrice( BigDecimal.valueOf( productDTO.getPrice() ) );
        }
        product.setQuantity( productDTO.getQuantity() );
        product.setName( productDTO.getName() );
        product.setUser( productDTO.getUser() );

        return product;
    }
}
