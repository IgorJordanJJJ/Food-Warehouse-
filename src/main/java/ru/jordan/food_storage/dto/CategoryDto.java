package ru.jordan.food_storage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class CategoryDto {

    @Hidden
    @Schema(description = "Идентификатор категории", example = "1", required = true)
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(max = 100, message = "Название категории не должно превышать 100 символов")
    @Schema(description = "Название категории", example = "Молочные продукты", required = true)
    private String name;

    @Size(max = 500, message = "Описание категории не должно превышать 500 символов")
    @Schema(description = "Описание категории", example = "Категория для всех молочных продуктов")
    private String description;

    @Hidden
    @Schema(description = "Дата создания категории", example = "2024-01-01T12:00:00")
    private LocalDateTime createdDate;

    @Hidden
    @Schema(description = "Дата последнего обновления категории", example = "2024-01-01T12:00:00")
    private LocalDateTime lastUpdatedDate;

    @Schema(description = "Статус активности категории", example = "true")
    private boolean active;
}
