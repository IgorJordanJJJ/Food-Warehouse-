package ru.jordan.food_storage.controller.graphql;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.facade.product.ProductFacadeImpl;
import ru.jordan.food_storage.model.Product;

import java.util.List;

@Controller
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductGraphQLController {

    private final ProductFacadeImpl productFacadeImpl;
    @QueryMapping
    public Product getProductById(@Argument Long id) {
        return productFacadeImpl.getProductById(id);
    }

    @QueryMapping
    public List<Product> findProductsByName(@Argument String name) {
        return productFacadeImpl.findProductsByName(name);
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productFacadeImpl.getAllProducts();
    }

    @MutationMapping
    public Product createProduct(@Argument @Valid ProductDto input) {
        return productFacadeImpl.saveProduct(input);
    }

    @MutationMapping
    public Product updateProduct(@Argument @Valid ProductDto input) {
        return productFacadeImpl.updateProduct(input);
    }

    @MutationMapping
    public void deleteProduct(@Argument Long id) {
        productFacadeImpl.deleteProduct(id);
    }
}
