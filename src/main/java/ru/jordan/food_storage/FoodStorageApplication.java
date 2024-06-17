package ru.jordan.food_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodStorageApplication.class, args);
    }

}
