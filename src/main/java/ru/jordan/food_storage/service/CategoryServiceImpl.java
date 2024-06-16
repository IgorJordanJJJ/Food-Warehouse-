package ru.jordan.food_storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.model.Category;
import ru.jordan.food_storage.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    @Override
    @Transactional
    public Category createCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException("CategoryDto cannot be null");
        }

        Category category = modelMapper.map(categoryDto, Category.class);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();

            modelMapper.map(categoryDto, existingCategory);

            return categoryRepository.save(existingCategory);
        } else {
            throw new IllegalArgumentException("Category with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            categoryRepository.delete(category);
        } else {
            throw new IllegalArgumentException("Category with id " + id + " not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findCategoriesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findByNameContainingNative(name);
        }
    }
}
