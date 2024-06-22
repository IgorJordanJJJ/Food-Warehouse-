package ru.jordan.food_storage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@JsonInclude(NON_DEFAULT)
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Детали продукта")
@Table(name = "product",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "unique_category_name")}
)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор продукта", example = "1")
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Schema(description = "Название продукта", example = "Молоко")
    @Column(unique = true) // Можно добавить unique = true для уникальности в рамках одной таблицы
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Schema(description = "Описание продукта", example = "Свежий и натуральный продукт")
    private String description;

//    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    @Schema(description = "Цена продукта", example = "50.0")
    private BigDecimal price;

//    @NotNull(message = "Масса обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Масса должна быть больше 0")
    @Schema(description = "Масса продукта", example = "1.0")
    private BigDecimal weight;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @Schema(description = "Категория продукта")
    private Category category;

//    @NotNull(message = "Дата изготовления обязательна")
    @Schema(description = "Дата изготовления продукта", example = "2024-01-01")
    private LocalDate manufactureDate;

    @Schema(description = "Срок годности продукта", example = "2025-01-01")
    private LocalDate expirationDate;

    @Size(max = 100, message = "Производитель не должен превышать 100 символов")
    @Schema(description = "Производитель продукта", example = "Компания А")
    private String manufacturer;

//    @NotNull(message = "Количество на складе обязательно")
    @Min(value = 0, message = "Количество на складе должно быть больше или равно 0")
    @Schema(description = "Количество продукта на складе", example = "100")
    private int stockQuantity;

//    @NotBlank(message = "Артикул обязателен")
    @Size(max = 50, message = "Артикул не должен превышать 50 символов")
    @Schema(description = "Артикул продукта", example = "SKU12345")
    private String sku;

//    @Size(max = 200, message = "URL изображения не должен превышать 200 символов")
//    @Schema(description = "URL изображения продукта", example = "http://example.com/image.jpg")
//    private String imageUrl;

//    @Size(max = 100, message = "Бренд не должен превышать 100 символов")
    @Schema(description = "Бренд продукта", example = "Бренд А")
    private String brand;

    @Size(max = 100, message = "Страна происхождения не должна превышать 100 символов")
    @Schema(description = "Страна происхождения продукта", example = "Россия")
    private String countryOfOrigin;

//    @DecimalMin(value = "0.0", message = "Скидка не должна быть меньше 0")
//    @DecimalMax(value = "1.0", message = "Скидка не должна быть больше 1")
    @Schema(description = "Скидка на продукт", example = "0.1")
    private BigDecimal discount;

    @Schema(description = "Доступность продукта", example = "true")
    private boolean available;

    @CreatedDate
    @Schema(description = "Дата создания продукта", example = "2024-01-01T12:00:00")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Schema(description = "Дата последнего обновления продукта", example = "2024-01-01T12:00:00")
    private LocalDateTime lastUpdatedDate;
}
