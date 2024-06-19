package ru.jordan.food_storage.facade.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.facade.category.CategoryFacade;
import ru.jordan.food_storage.model.Category;
import ru.jordan.food_storage.service.category.CategoryServiceImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // Транзакция только для чтения
public class CategoryFacadeImpl implements CategoryFacade {

    private final CategoryServiceImpl categoryService;

    @Override
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryService.getCategoryById(id);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public Category saveCategory(CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    public List<Category> findCategoriesByName(String name) {
        return categoryService.findCategoriesByName(name);
    }

    @Override
    @Transactional // Транзакция с поддержкой чтения и записи
    public Category updateCategory(CategoryDto category) {
        return categoryService.updateCategory(category);
    }
}
