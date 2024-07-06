package ru.jordan.food_storage.controller.beans;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestBeans {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry){
        var containers = new PostgreSQLContainer<>("postgres:16").withEnv("TZ", "UTC");
        registry.add("postgresql.driver", containers::getDriverClassName);
        return containers;
    }
}
