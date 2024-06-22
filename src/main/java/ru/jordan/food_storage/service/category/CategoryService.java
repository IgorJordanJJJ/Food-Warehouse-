package ru.jordan.food_storage.service.category;

import ru.jordan.food_storage.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * Создание новой категории.
     * @param categoryDto данные для создания категории
     * @return созданная категория
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Получение категории по ID.
     * @param id ID категории
     * @return category с найденной категорией, или пустой, если категория не найдена
     */
    CategoryDto getCategoryById(Long id);

    /**
     * Обновление существующей категории.
     * @param categoryDto данные для обновления категории
     * @return обновленная категория
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    CategoryDto updateCategory(CategoryDto categoryDto);

    /**
     * Удаление категории по ID.
     * @param id ID категории для удаления
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    void deleteCategory(Long id);

    /**
     * Получение всех категорий.
     * @return список всех категорий
     */
    List<CategoryDto> getAllCategories();

    /**
     * Поиск категорий по имени.
     * @param name часть имени для поиска категорий
     * @return список категорий, найденных по имени
     */
    List<CategoryDto> findCategoriesByName(String name);
}
