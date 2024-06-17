package ru.jordan.food_storage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@JsonInclude(NON_DEFAULT)
@Schema(description = "Категория продукта")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "category",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "unique_category_name")}
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор категории", example = "1", required = true)
    @Hidden
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(min = 1, max = 100, message = "Название категории не должно превышать 100 символов")
    @Schema(description = "Название категории", example = "Молочные продукты", required = true)
    @Column(unique = true) // Можно добавить unique = true для уникальности в рамках одной таблицы
    private String name;

    @Size(min = 1, max = 500, message = "Описание категории не должно превышать 500 символов")
    @Schema(description = "Описание категории", example = "Категория для всех молочных продуктов")
    private String description;

    @Hidden
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата создания категории", example = "2024-01-01T12:00:00")
    private LocalDateTime createdDate;

    @Hidden
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата последнего обновления категории", example = "2024-01-01T12:00:00")
    private LocalDateTime lastUpdatedDate;

    @NotNull(message = "Статус активности категории обязателен")
    @Schema(description = "Статус активности категории", example = "true")
    private boolean active;
}
