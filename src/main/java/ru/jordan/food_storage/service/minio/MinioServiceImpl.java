package ru.jordan.food_storage.service.minio;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jordan.food_storage.dto.FileDto;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @PostConstruct
    private void createBucketIfNotExists() {
        try {
            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isBucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking/creating bucket in Minio", e);
        }
    }

    @Override
    public FileDto uploadFile(FileDto fileDto) {
        try {
            MultipartFile file = fileDto.getFile();
            String filename = fileDto.getFilename() != null ? fileDto.getFilename() : file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            fileDto.setFilename(filename);
            fileDto.setSize(file.getSize());
            fileDto.setUrl(generatePresignedUrl(filename));

            return fileDto;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading file to Minio", e);
        }
    }

    @Override
    public InputStream downloadFile(String filename) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(filename).build());
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file from Minio", e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(filename).build());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting file from Minio", e);
        }
    }

    @Override
    public boolean fileExists(String filename) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(filename).build());
            return true;
        } catch (Exception e) {
            if (e instanceof ErrorResponseException &&
                    ((ErrorResponseException) e).errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new RuntimeException("Error while checking file existence in Minio", e);
        }
    }

    @Override
    public String generatePresignedUrl(String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(filename)
                            .expiry(60 * 60 * 24) // 24 hours
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Error while generating presigned URL", e);
        }
    }

    @Override
    public List<FileDto> listFiles() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
            List<FileDto> fileDtos = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                FileDto fileDto = FileDto.builder()
                        .filename(item.objectName())
                        .size(item.size())
                        .url(generatePresignedUrl(item.objectName()))
                        .build();
                fileDtos.add(fileDto);
            }
            return fileDtos;
        } catch (Exception e) {
            throw new RuntimeException("Error while listing files in Minio", e);
        }
    }

}
