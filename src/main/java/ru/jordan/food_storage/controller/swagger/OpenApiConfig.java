package ru.jordan.food_storage.controller.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Food storage",
                description = "It is one of the few microservices to create an app to systematize nutrition and track food intake.", version = "1.0.0",
                contact = @Contact(
                        name = "Igor Jordan",
                        email = "igorjordan210@gmail.com",
                        url = "null"
                )
        )
)
public class OpenApiConfig {

}
