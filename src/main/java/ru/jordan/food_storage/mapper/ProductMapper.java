package ru.jordan.food_storage.mapper;

import org.mapstruct.Mapper;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.model.Product;


@Mapper
public interface ProductMapper {
    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);
}
