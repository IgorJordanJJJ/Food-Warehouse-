package ru.jordan.food_storage.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.model.Category;
import ru.jordan.food_storage.repository.CategoryRepository;
import ru.jordan.food_storage.service.category.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames={"category"})
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Category createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new RuntimeException("Категория с таким именем уже существует");
        }

        Category category = modelMapper.map(categoryDto, Category.class);

        return categoryRepository.save(category);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Category updateCategory(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getId());

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();

            modelMapper.map(categoryDto, existingCategory);

            return categoryRepository.save(existingCategory);
        } else {
            throw new IllegalArgumentException("Category with id " + categoryDto.getId() + " not found");
        }
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
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
    @Cacheable
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<Category> findCategoriesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findByNameContainingNative(name);
        }
    }
}
