package ru.jordan.food_storage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Schema(description = "Категория продукта")
public class CategoryDto implements Serializable {

    @Schema(description = "Идентификатор категории", example = "1")
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(min = 1, max = 100, message = "Название категории не должно превышать 100 символов")
    @Schema(description = "Название категории", example = "Молочные продукты")
    private String name;

    @Size(min = 1, max = 500, message = "Описание категории не должно превышать 500 символов")
    @Schema(description = "Описание категории", example = "Категория для всех молочных продуктов")
    private String description;

    @Schema(description = "Дата создания категории", example = "2024-01-01T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @Schema(description = "Дата последнего обновления категории", example = "2024-01-01T12:00:00")
    private LocalDateTime lastUpdatedDate;

    @NotNull(message = "Статус активности категории обязателен")
    @Schema(description = "Статус активности категории", example = "true")
    private Boolean active;
}
