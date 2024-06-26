package ru.jordan.food_storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Файл DTO")
public class FileDto implements Serializable {

    private static final long serialVersionUID = 232836038145089522L;

    @NotBlank(message = "Заголовок файла обязателен")
    @Size(min = 1, max = 100, message = "Заголовок файла не должен превышать 100 символов")
    @Schema(description = "Заголовок файла", example = "Документ")
    private String title;

    @Size(max = 500, message = "Описание файла не должно превышать 500 символов")
    @Schema(description = "Описание файла", example = "Документ для ознакомления")
    private String description;

    @SuppressWarnings("java:S1948")
    @NotNull(message = "Файл обязателен")
    @Schema(description = "Файл", implementation = MultipartFile.class)
    private MultipartFile file;

    @Size(max = 2048, message = "URL файла не должен превышать 2048 символов")
    @Schema(description = "URL файла", example = "http://example.com/file.pdf")
    private String url;

    @Schema(description = "Размер файла в байтах", example = "204800")
    private Long size;

    @Size(max = 255, message = "Имя файла не должно превышать 255 символов")
    @Schema(description = "Имя файла", example = "file.pdf")
    private String filename;

}
