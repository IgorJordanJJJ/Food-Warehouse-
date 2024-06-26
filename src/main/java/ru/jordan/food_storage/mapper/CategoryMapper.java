package ru.jordan.food_storage.mapper;


import org.mapstruct.Mapper;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto categoryToCategoryDto(Category category);
    Category categoryDtoToCategory(CategoryDto categoryDto);
}
