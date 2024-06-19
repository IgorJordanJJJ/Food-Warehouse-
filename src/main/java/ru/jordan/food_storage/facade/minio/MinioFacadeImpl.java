package ru.jordan.food_storage.facade.minio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.jordan.food_storage.dto.FileDto;
import ru.jordan.food_storage.service.minio.MinioService;
import ru.jordan.food_storage.service.minio.MinioServiceImpl;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioFacadeImpl implements MinioService {

    private final MinioServiceImpl minioService;
    @Override
    public FileDto uploadFile(FileDto fileDto) {
        return minioService.uploadFile(fileDto);
    }

    @Override
    public InputStream downloadFile(String filename) {
        return minioService.downloadFile(filename);
    }

    @Override
    public void deleteFile(String filename) {
        minioService.deleteFile(filename);
    }

    @Override
    public boolean fileExists(String filename) {
        return minioService.fileExists(filename);
    }

    @Override
    public String generatePresignedUrl(String filename) {
        return minioService.generatePresignedUrl(filename);
    }

    @Override
    public List<FileDto> listFiles() {
        return minioService.listFiles();
    }
}
