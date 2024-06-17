package ru.jordan.food_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordan.food_storage.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Проверка наличия категории с заданным именем
    boolean existsByName(String name);

    // Поиск категории по имени (если нужно)
    Optional<Category> findByName(String name);
    @Query(value = "SELECT * FROM category WHERE name LIKE %:name%", nativeQuery = true)
    List<Category> findByNameContainingNative(String name);
}
