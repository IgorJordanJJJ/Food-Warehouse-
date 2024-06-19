package ru.jordan.food_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordan.food_storage.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Проверка наличия категории с заданным именем
    boolean existsByName(String name);

    // Поиск категории по имени (если нужно)
    Optional<Product> findByName(String name);
    @Query(value = "SELECT * FROM product WHERE name LIKE %:name%", nativeQuery = true)
    List<Product> findByNameContainingNative(String name);
}
