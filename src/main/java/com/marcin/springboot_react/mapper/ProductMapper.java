package com.marcin.springboot_react.mapper;

import com.marcin.springboot_react.model.Product;
import com.marcin.springboot_react.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "user", target = "user")
    ProductDTO mapProductToProductDTO(Product product);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "user", target = "user")
    Product mapProductDTOToProduct(ProductDTO productDTO);
}
