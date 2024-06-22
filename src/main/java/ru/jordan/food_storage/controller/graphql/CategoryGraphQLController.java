package ru.jordan.food_storage.controller.graphql;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.facade.category.CategoryFacadeImpl;

import java.util.List;

@Controller
@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryGraphQLController {

    private final CategoryFacadeImpl categoryFacadeImpl;
    @QueryMapping
    public CategoryDto getCategoryById(@Argument Long id) {
        return categoryFacadeImpl.getCategoryById(id);
    }

    @QueryMapping
    public List<CategoryDto> findCategoriesByName(@Argument String name) {
        return categoryFacadeImpl.findCategoriesByName(name);
    }

    @QueryMapping
    public List<CategoryDto> getAllCategories() {
        return categoryFacadeImpl.getAllCategories();
    }

    @MutationMapping
    public CategoryDto createCategory(@Argument @Valid CategoryDto input) {
        return categoryFacadeImpl.saveCategory(input);
    }

    @MutationMapping
    public CategoryDto updateCategory(@Argument @Valid CategoryDto input) {
        return categoryFacadeImpl.updateCategory(input);
    }

    @MutationMapping
    public void deleteCategory(@Argument Long id) {
        categoryFacadeImpl.deleteCategory(id);
    }

}
