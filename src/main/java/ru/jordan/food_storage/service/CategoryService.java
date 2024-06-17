package ru.jordan.food_storage.service;

import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     * Создание новой категории.
     * @param category данные для создания категории
     * @return созданная категория
     */
    Category createCategory(CategoryDto category);

    /**
     * Получение категории по ID.
     * @param id ID категории
     * @return category с найденной категорией, или пустой, если категория не найдена
     */
    Category getCategoryById(Long id);

    /**
     * Обновление существующей категории.
     * @param category данные для обновления категории
     * @return обновленная категория
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    Category updateCategory(CategoryDto category);

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
    List<Category> getAllCategories();

    /**
     * Поиск категорий по имени.
     * @param name часть имени для поиска категорий
     * @return список категорий, найденных по имени
     */
    List<Category> findCategoriesByName(String name);
}
