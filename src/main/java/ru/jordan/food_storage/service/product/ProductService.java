package ru.jordan.food_storage.service.product;


import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.model.Product;

import java.util.List;

public interface ProductService {

    /**
     * Создание нового продукта.
     * @param productDto данные для создания продукта
     * @return созданная продукт
     */
    Product createProduct(ProductDto productDto);

    /**
     * Получение продукта по ID.
     * @param id ID продукта
     * @return Product с найденным продуктом, или пустой, если продукт не найдена
     */
    Product getProductById(Long id);

    /**
     * Обновление существующей продукта.
     * @param productDto данные для обновления продукта
     * @return обновленная продукт
     * @throws IllegalArgumentException если продукт с указанным ID не найдена
     */
    Product updateProduct(ProductDto productDto);

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
    List<Product> getAllProducts();

    /**
     * Поиск продуктов по имени.
     * @param name часть имени для поиска продуктов
     * @return список продуктов, найденных по имени
     */
    List<Product> findProductsByName(String name);
}
