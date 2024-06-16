package ru.jordan.food_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordan.food_storage.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);

    @Query(value = "SELECT * FROM categories WHERE name LIKE %:name%", nativeQuery = true)
    List<Category> findByNameContainingNative(String name);
}
