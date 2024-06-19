package ru.jordan.food_storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordan.food_storage.dto.ProductDto;
import ru.jordan.food_storage.facade.product.ProductFacadeImpl;
import ru.jordan.food_storage.model.Product;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(value = "/product", produces = "application/json")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Некорректный запрос",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Ресурс не найден",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = @Content(schema = @Schema(implementation = ApiError.class)))
})
@Tag(name = "Контроллер продуктов", description = "Контроллер для взаимодействия с продуктами")
public class ProductController {

    private final ProductFacadeImpl productFacadeImpl;

    @Operation(summary = "Получить все продукты", description = "Получить список всех продуктов.")
    @GetMapping
    public List<Product> getAllProducts() {
        return productFacadeImpl.getAllProducts();
    }

    @Operation(summary = "Получить продукт по ID", description = "Получить продукт по его ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Parameter(description = "Идентификатор продукта") @Min(0) @PathVariable Long id) {
        Product product = productFacadeImpl.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Создать новый продукт", description = "Создать новый продукт с указанными данными.")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @Parameter(description = "Продукт") @RequestBody ProductDto productDto) {
        Product product = productFacadeImpl.saveProduct(productDto);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Обновить существующий продукт", description = "Обновить данные существующего продукта по его ID.")
    @PutMapping
    public ResponseEntity<Product> updateProduct(@Valid @Parameter(description = "Продукт") @RequestBody ProductDto productDetails) {
        Product updatedProduct = productFacadeImpl.updateProduct(productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Удалить продукт", description = "Удалить существующий продукт по его ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@Parameter(description = "Идентификатор продукта") @Min(value = 0, message = "ID must be greater than or equal to 0") @PathVariable Long id) {
        productFacadeImpl.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " deleted successfully.");
    }

    @Operation(summary = "Поиск продуктов по имени", description = "Поиск продуктов по указанному имени.")
    @GetMapping("/search")
    public ResponseEntity<List<Product>> findProductsByName(@Parameter(description = "Имя продукта") @RequestParam String name) {
        List<Product> products = productFacadeImpl.findProductsByName(name);
        return ResponseEntity.ok(products);
    }
}
