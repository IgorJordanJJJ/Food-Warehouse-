package ru.jordan.food_storage.facade.category;

import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.model.Category;

import java.util.List;

public interface CategoryFacade {

    /**
     * Получить все категории.
     * @return список всех категорий
     */
    List<Category> getAllCategories();

    /**
     * Получить категорию по ID.
     * @param id ID категории
     * @return Optional с категорией, или пустой, если категория не найдена
     */
    Category getCategoryById(Long id);

    /**
     * Сохранить новую категорию.
     * @param categoryDto данные для создания категории
     * @return сохраненная категория
     */
    Category saveCategory(CategoryDto categoryDto);

    /**
     * Удалить категорию по ID.
     * @param id ID категории для удаления
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    void deleteCategory(Long id);

    /**
     * Найти категории по имени.
     * @param name часть имени для поиска категорий
     * @return список категорий, найденных по имени
     */
    List<Category> findCategoriesByName(String name);

    /**
     * Обновление существующей категории.
     * @param category данные для обновления категории
     * @return обновленная категория
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    Category updateCategory(CategoryDto category);
}
