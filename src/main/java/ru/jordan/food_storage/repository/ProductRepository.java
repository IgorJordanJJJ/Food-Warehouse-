package ru.jordan.food_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jordan.food_storage.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
