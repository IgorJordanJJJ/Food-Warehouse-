package ru.jordan.food_storage.controller.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.jordan.food_storage.controller.beans.TestBeans;
import ru.jordan.food_storage.dto.FileDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest(classes = TestBeans.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MinioControllerTest {

    private static ObjectMapper objectMapper;
    private static final String MINIO_IMAGE = "minio/minio";
    private static final String BUCKET_NAME = "your-bucket";
    @Autowired
    private MockMvc mockMvc;

    private static MinioClient minioClient;


    @Container
    public static GenericContainer<?> minioContainer = new GenericContainer<>(MINIO_IMAGE)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
            .withCommand("server /data")
            .withExposedPorts(9000);

    @BeforeAll
    static void setUp() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        minioContainer.start();

        // Initialize MinIO client
        String minioUrl = "http://" + minioContainer.getHost() + ":" + minioContainer.getFirstMappedPort();
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials("minioadmin", "minioadmin")
                .build();

        // Constants for test data
        final String FILE_NAME = "file.pdf";
        final String FILE_CONTENT = "Test file content";

        // Create a bucket for testing
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());


        // Upload a test file using PutObjectArgs.builder()
        InputStream stream = new ByteArrayInputStream(FILE_CONTENT.getBytes());
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(FILE_NAME)
                        .stream(stream, FILE_CONTENT.length(), -1)
                        .build()
        );
    }


    @DynamicPropertySource
    static void registerMinioProperties(DynamicPropertyRegistry registry) {
        String minioUrl = "http://" + minioContainer.getHost() + ":" + minioContainer.getFirstMappedPort();
        registry.add("minio.url", () -> minioUrl);
        registry.add("minio.access-key", () -> "minioadmin");
        registry.add("minio.secret-key", () -> "minioadmin");
    }


    @Test
    @DisplayName("Test upload file")
    @Order(1)
    void uploadFileTest() throws Exception {
        // Constants for test data
        final String FILE_NAME = "file_test.pdf";
        final String FILE_CONTENT = "Test file content";
        final String TITLE = "Document";
        final String DESCRIPTION = "Document for review";

        // Create a mock file to upload
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                FILE_NAME,
                MediaType.APPLICATION_PDF_VALUE,
                FILE_CONTENT.getBytes()
        );

        // Create a FileDto object
        FileDto fileDto = FileDto.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .file(mockFile)
                .build();

        // Perform the file upload request
        MvcResult result = mockMvc.perform(multipart("/minio/upload")
                        .file(mockFile)
                        .param("title", TITLE)
                        .param("description", DESCRIPTION)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        // Convert the response to FileDto object
        String responseBody = result.getResponse().getContentAsString();
        FileDto responseFileDto = objectMapper.readValue(responseBody, FileDto.class);

        // Assert the response
        assertEquals(fileDto.getTitle(), responseFileDto.getTitle(), "Title should match");
        assertEquals(fileDto.getDescription(), responseFileDto.getDescription(), "Description should match");
    }

    @Test
    @DisplayName("Test file exists")
    @Order(1)
    void testFileExists() throws Exception {
        // Perform GET request to check file existence
        mockMvc.perform(get("/minio/exists/file.pdf"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("true"));
    }

    @Test
    @DisplayName("Test file not exists")
    @Order(1)
    void testFileNotExists() throws Exception {
        // Perform GET request to check file existence
        mockMvc.perform(get("/minio/exists/nonexistent-file.pdf"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("false"));
    }

    @Test
    @DisplayName("Test delete file")
    @Order(2)
    void deleteFileTest() throws Exception {
        mockMvc.perform(delete("/minio/delete/file.pdf"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test download file")
    @Order(1)
    void downloadFileTest() throws Exception {
        String filename = "file.pdf";

        mockMvc.perform(get("/minio/download/{filename}", filename))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("Test generate presigned url")
    @Order(1)
    void generatePresignedUrlTest() throws Exception {
        String filename = "file.pdf";

        mockMvc.perform(get("/minio/presigned-url/{filename}", filename))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test list files")
    @Order(1)
    void listFilesTest() throws Exception {
        mockMvc.perform(get("/minio/list"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test only download file")
    @Order(1)
    void onlyDownloadFileTest() throws Exception {
        String filename = "file.pdf";

        mockMvc.perform(get("/minio/download/file/{filename}", filename))
                .andExpect(status().isOk())
                .andDo(print());
    }

}