package ru.jordan.food_storage.config.app;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jordan.food_storage.mapper.CategoryMapper;
import ru.jordan.food_storage.mapper.ProductMapper;

@Configuration
public class MapperConfig {
    @Bean
    public CategoryMapper categoryMapper() {
        return Mappers.getMapper(CategoryMapper.class);
    }
    @Bean
    public ProductMapper productMapper(){
        return Mappers.getMapper(ProductMapper.class);
    }
}
