package ru.jordan.food_storage.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@Schema(description = "Категория продукта")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор категории", example = "1", required = true)
    @Hidden
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(max = 100, message = "Название категории не должно превышать 100 символов")
    @Schema(description = "Название категории", example = "Молочные продукты", required = true)
    private String name;

    @Size(max = 500, message = "Описание категории не должно превышать 500 символов")
    @Schema(description = "Описание категории", example = "Категория для всех молочных продуктов")
    private String description;

    @Hidden
    @CreatedDate
    @Schema(description = "Дата создания категории", example = "2024-01-01T12:00:00")
    private LocalDateTime createdDate;

    @Hidden
    @LastModifiedDate
    @Schema(description = "Дата последнего обновления категории", example = "2024-01-01T12:00:00")
    private LocalDateTime lastUpdatedDate;

    @Schema(description = "Статус активности категории", example = "true")
    private boolean active;
}
