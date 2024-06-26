package ru.jordan.food_storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jordan.food_storage.dto.FileDto;
import ru.jordan.food_storage.facade.minio.MinioFacadeImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(value = "/minio", produces = "application/json")
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
@Tag(name = "Контроллер Minio", description = "Контроллер для работы с файлами")
public class MinioController {

    private final MinioFacadeImpl minioFacadeImpl;
    @Operation(summary = "Загрузка файла", description = "Загружает файл в хранилище MinIO.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@Valid @ModelAttribute FileDto fileDto) {
        try {
            FileDto uploadedFile = minioFacadeImpl.uploadFile(fileDto);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Скачивание файла", description = "Скачивает файл из хранилища MinIO по его имени.")
    @GetMapping("/download/{filename}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filename) {
        try (InputStream inputStream = minioFacadeImpl.downloadFile(filename)) {
            byte[] bytes = inputStream.readAllBytes();
            ByteArrayResource resource = new ByteArrayResource(bytes);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } catch (IOException e) {
            throw new ResponseStatusException(500, "Error downloading file", e);
        }
    }

    @Operation(summary = "Скачивание файла", description = "Скачивает файл из хранилища MinIO по его имени.")
    @GetMapping("/download/file/{filename}")
    public ResponseEntity<ByteArrayResource> onlyDownloadFile(@PathVariable String filename) {
        try (InputStream inputStream = minioFacadeImpl.downloadFile(filename)) {
            byte[] bytes = inputStream.readAllBytes();
            ByteArrayResource resource = new ByteArrayResource(bytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new ResponseStatusException(500, "Error downloading file", e);
        }
    }

    @Operation(summary = "Удаление файла", description = "Удаляет файл из хранилища MinIO по его имени.")
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            minioFacadeImpl.deleteFile(filename);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }

    @Operation(summary = "Проверка существования файла", description = "Проверяет, существует ли файл в хранилище MinIO по его имени.")
    @GetMapping("/exists/{filename}")
    public ResponseEntity<Boolean> fileExists(@PathVariable String filename) {
        try {
            boolean exists = minioFacadeImpl.fileExists(filename);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Генерация пресайнед URL", description = "Генерирует пресайнед URL для доступа к файлу в хранилище MinIO.")
    @GetMapping("/presigned-url/{filename}")
    public ResponseEntity<String> generatePresignedUrl(@PathVariable String filename) {
        try {
            String url = minioFacadeImpl.generatePresignedUrl(filename);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating presigned URL: " + e.getMessage());
        }
    }

    @Operation(summary = "Список файлов", description = "Возвращает список всех файлов, хранящихся в хранилище MinIO.")
    @GetMapping("/list")
    public ResponseEntity<List<FileDto>> listFiles() {
        try {
            List<FileDto> fileDtos = minioFacadeImpl.listFiles();
            return ResponseEntity.ok(fileDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
