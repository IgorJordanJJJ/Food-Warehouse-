package ru.jordan.food_storage.facade.category;

import ru.jordan.food_storage.dto.CategoryDto;

import java.util.List;

public interface CategoryFacade {

    /**
     * Получить все категории.
     * @return список всех категорий
     */
    List<CategoryDto> getAllCategories();

    /**
     * Получить категорию по ID.
     * @param id ID категории
     * @return Optional с категорией, или пустой, если категория не найдена
     */
    CategoryDto getCategoryById(Long id);

    /**
     * Сохранить новую категорию.
     * @param categoryDto данные для создания категории
     * @return сохраненная категория
     */
    CategoryDto saveCategory(CategoryDto categoryDto);

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
    List<CategoryDto> findCategoriesByName(String name);

    /**
     * Обновление существующей категории.
     * @param category данные для обновления категории
     * @return обновленная категория
     * @throws IllegalArgumentException если категория с указанным ID не найдена
     */
    CategoryDto updateCategory(CategoryDto category);
}
