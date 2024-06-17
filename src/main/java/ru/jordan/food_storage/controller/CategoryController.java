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
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.facade.CategoryFacadeImpl;
import ru.jordan.food_storage.model.Category;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(value = "/category", produces = "application/json")
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
@Tag(name = "Контроллер кантигорий продукта", description = "Контроллер для взаимодействия с кантегориями продукта")
public class CategoryController {

    private final CategoryFacadeImpl categoryFacadeImpl;

    @Operation(summary = "Получить все категории", description = "Получить список всех категорий.")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryFacadeImpl.getAllCategories();
    }

    @Operation(summary = "Получить категорию по ID", description = "Получить категорию по её ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@Parameter(description = "Идентификатор категории") @Min(0) @PathVariable Long id) {
        Category category = categoryFacadeImpl.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Создать новую категорию", description = "Создать новую категорию с указанными данными.")
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @Parameter(description = "Категоря продукта") @RequestBody CategoryDto categoryDto) {
        Category category = categoryFacadeImpl.saveCategory(categoryDto);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Обновить существующую категорию", description = "Обновить данные существующей категории по её ID.")
    @PutMapping
    public ResponseEntity<Category> updateCategory(@Valid @Parameter(description = "Категоря продукта") @RequestBody CategoryDto categoryDetails) {
        Category updatedCategory = categoryFacadeImpl.updateCategory(categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Удалить категорию", description = "Удалить существующую категорию по её ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@Parameter(description = "Идентификатор категории") @Min(value = 0, message = "ID must be greater than or equal to 0") @PathVariable Long id) {
        categoryFacadeImpl.deleteCategory(id);
        return ResponseEntity.ok("Category with ID " + id + " deleted successfully.");
    }

    @Operation(summary = "Поиск категорий по имени", description = "Поиск категорий по указанному имени.")
    @GetMapping("/search")
    public ResponseEntity<List<Category>> findCategoriesByName(@Parameter(description = "Имя категории") @RequestParam String name) {
        List<Category> categories = categoryFacadeImpl.findCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }
}
