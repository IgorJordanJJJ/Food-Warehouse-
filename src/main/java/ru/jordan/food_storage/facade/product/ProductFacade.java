package ru.jordan.food_storage.facade.product;

import ru.jordan.food_storage.dto.ProductDto;

import java.util.List;

public interface ProductFacade {

    /**
     * Получение продукта по ID.
     * @param id ID продукта
     * @return Product с найденным продуктом, или пустой, если продукт не найдена
     */
    ProductDto getProductById(Long id);

    /**
     * Сохранить новый продукт.
     * @param productDto данные для создания продукта
     * @return сохраненный продукт
     */
    ProductDto saveProduct(ProductDto productDto);

    /**
     * Обновление существующей продукта.
     * @param productDto данные для обновления продукта
     * @return обновленная продукт
     * @throws IllegalArgumentException если продукт с указанным ID не найдена
     */
    ProductDto updateProduct(ProductDto productDto);

    /**
     * Удаление продукта по ID.
     * @param id ID продукта для удаления
     * @throws IllegalArgumentException если продукт с указанным ID не найдена
     */
    void deleteProduct(Long id);

    /**
     * Получение всех продуктов.
     * @return список всех продуктов
     */
    List<ProductDto> getAllProducts();

    /**
     * Поиск продуктов по имени.
     * @param name часть имени для поиска продуктов
     * @return список продуктов, найденных по имени
     */
    List<ProductDto> findProductsByName(String name);
}
