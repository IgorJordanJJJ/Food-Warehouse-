package ru.jordan.food_storage.mapper;

import org.mapstruct.Mapper;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.model.Product;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product Product);
    Product productDtoToProduct(ProductDto ProductDto);
}
