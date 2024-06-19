package ru.jordan.food_storage.facade.product;

import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.model.Product;

import java.util.List;

public interface ProductFacade {

    /**
     * Получение продукта по ID.
     * @param id ID продукта
     * @return Product с найденным продуктом, или пустой, если продукт не найдена
     */
    Product getProductById(Long id);

    /**
     * Сохранить новый продукт.
     * @param productDto данные для создания продукта
     * @return сохраненный продукт
     */
    Product saveProduct(ProductDto productDto);

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
