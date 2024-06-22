package ru.jordan.food_storage.service.category;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.mapper.CategoryMapper;
import ru.jordan.food_storage.model.Category;
import ru.jordan.food_storage.repository.CategoryRepository;
import ru.jordan.food_storage.service.excel.CategoryExcelGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames={"category"})
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CategoryExcelGenerator categoryExcelGenerator;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new RuntimeException("Категория с таким именем уже существует");
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(categoryMapper::categoryToCategoryDto).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getId());

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();

            modelMapper.map(categoryDto, existingCategory);
            Category updatedCategory = categoryRepository.save(existingCategory);
            return categoryMapper.categoryToCategoryDto(updatedCategory);
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
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<CategoryDto> findCategoriesByName(String name) {
        List<Category> categories;
        if (name == null || name.trim().isEmpty()) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findByNameContainingNative(name);
        }
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void getCategoriesExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        categoryExcelGenerator.exportToExcel(response, categoryRepository.findAll());
    }
}
