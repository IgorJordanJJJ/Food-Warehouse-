package ru.jordan.food_storage.controller.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.jordan.food_storage.controller.beans.TestBeans;
import ru.jordan.food_storage.dto.ProductDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(classes = TestBeans.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = "classpath:db/data/product/data_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:db/data/product/cleanup_data_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ProductControllerTest {

    private ObjectMapper objectMapper;
    private static final String MINIO_IMAGE = "minio/minio";
    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        minioContainer.start();
    }

    @Container
    public static GenericContainer<?> minioContainer = new GenericContainer<>(MINIO_IMAGE)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
            .withCommand("server /data")
            .withExposedPorts(9000);

    @DynamicPropertySource
    static void registerMinioProperties(DynamicPropertyRegistry registry) {
        String minioUrl = "http://" + minioContainer.getHost() + ":" + minioContainer.getFirstMappedPort();
        registry.add("minio.url", () -> minioUrl);
        registry.add("minio.access-key", () -> "minioadmin");
        registry.add("minio.secret-key", () -> "minioadmin");
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return a non-empty list of products")
    @Order(1)
    void getAllProducts() throws Exception {

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Milk"))
                .andExpect(jsonPath("$[0].description").value("Fresh and natural product"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[0].weight").value(1.0))
                .andExpect(jsonPath("$[0].category").doesNotExist()) // Check for absence of category or adjust to actual category values
                .andExpect(jsonPath("$[0].manufactureDate").value("2024-01-01"))
                .andExpect(jsonPath("$[0].expirationDate").value("2025-01-01"))
                .andExpect(jsonPath("$[0].manufacturer").value("Company A"))
                .andExpect(jsonPath("$[0].stockQuantity").value(100))
                .andExpect(jsonPath("$[0].sku").value("SKU12345"))
                .andExpect(jsonPath("$[0].brand").value("Brand A"))
                .andExpect(jsonPath("$[0].countryOfOrigin").value("Russia"))
                .andExpect(jsonPath("$[0].discount").value(0.1))
                .andExpect(jsonPath("$[0].available").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Apple"))
                .andExpect(jsonPath("$[1].description").value("Juicy and ripe fruits"))
                .andExpect(jsonPath("$[1].price").value(80.0))
                .andExpect(jsonPath("$[1].weight").value(2.5))
                .andExpect(jsonPath("$[1].category").doesNotExist()) // Check for absence of category or adjust to actual category values
                .andExpect(jsonPath("$[1].manufactureDate").value("2024-02-15"))
                .andExpect(jsonPath("$[1].expirationDate").value("2024-03-15"))
                .andExpect(jsonPath("$[1].manufacturer").value("Horticulture B"))
                .andExpect(jsonPath("$[1].stockQuantity").value(150))
                .andExpect(jsonPath("$[1].sku").value("SKU67890"))
                .andExpect(jsonPath("$[1].brand").value("Farmers Brand"))
                .andExpect(jsonPath("$[1].countryOfOrigin").value("Russia"))
                .andExpect(jsonPath("$[1].discount").value(0.2)) // Adjust discount check if necessary
                .andExpect(jsonPath("$[1].available").value(true));

    }

    @Test
    @DisplayName("Should return product by id")
    @Order(1)
    void getProductById() throws Exception {
        mockMvc.perform(get("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Milk"))
                .andExpect(jsonPath("$.description").value("Fresh and natural product"))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.weight").value(1.0))
                .andExpect(jsonPath("$.category").doesNotExist()) // Check for absence of category or adjust to actual category values
                .andExpect(jsonPath("$.manufactureDate").value("2024-01-01"))
                .andExpect(jsonPath("$.expirationDate").value("2025-01-01"))
                .andExpect(jsonPath("$.manufacturer").value("Company A"))
                .andExpect(jsonPath("$.stockQuantity").value(100))
                .andExpect(jsonPath("$.sku").value("SKU12345"))
                .andExpect(jsonPath("$.brand").value("Brand A"))
                .andExpect(jsonPath("$.countryOfOrigin").value("Russia"))
                .andExpect(jsonPath("$.discount").value(0.1))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("Should create products")
    @Order(2)
    void createProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("Sausage")
                .description("Delicious smoked sausage")
                .price(150.0)
                .weight(0.5)
                .manufactureDate(LocalDate.of(2024, 6, 15))
                .expirationDate(LocalDate.of(2024, 12, 15))
                .manufacturer("Meat Company B")
                .stockQuantity(50)
                .sku("SAU123")
                .brand("Meat Masters")
                .countryOfOrigin("Germany")
                .discount(0.05)
                .available(true)
                .build();

        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sausage"))
                .andExpect(jsonPath("$.description").value("Delicious smoked sausage"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.weight").value(0.5))
                .andExpect(jsonPath("$.category").doesNotExist()) // Adjust based on your actual implementation
                .andExpect(jsonPath("$.manufactureDate").value("2024-06-15"))
                .andExpect(jsonPath("$.expirationDate").value("2024-12-15"))
                .andExpect(jsonPath("$.manufacturer").value("Meat Company B"))
                .andExpect(jsonPath("$.stockQuantity").value(50))
                .andExpect(jsonPath("$.sku").value("SAU123"))
                .andExpect(jsonPath("$.brand").value("Meat Masters"))
                .andExpect(jsonPath("$.countryOfOrigin").value("Germany"))
                .andExpect(jsonPath("$.discount").value(0.05))
                .andExpect(jsonPath("$.available").value(true));;
    }

    @Test
    @DisplayName("Should update product")
    @Order(2)
    void updateProduct() throws Exception {

        long productId = 1L;

        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name("Milk")
                .description("Fresh and natural product")
                .price(50.0)
                .weight(1.0)
                .manufactureDate(LocalDate.of(2024, 1, 1))
                .expirationDate(LocalDate.of(2025, 1, 1))
                .manufacturer("Company A")
                .stockQuantity(100)
                .sku("SKU12345")
                .brand("Brand A")
                .countryOfOrigin("Russia")
                .discount(0.1)
                .available(true)
                .build();

        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Milk"))
                .andExpect(jsonPath("$.description").value("Fresh and natural product"))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.weight").value(1.0))
                .andExpect(jsonPath("$.category").doesNotExist()) // Check for absence of category or adjust to actual category values
                .andExpect(jsonPath("$.manufactureDate").value("2024-01-01"))
                .andExpect(jsonPath("$.expirationDate").value("2025-01-01"))
                .andExpect(jsonPath("$.manufacturer").value("Company A"))
                .andExpect(jsonPath("$.stockQuantity").value(100))
                .andExpect(jsonPath("$.sku").value("SKU12345"))
                .andExpect(jsonPath("$.brand").value("Brand A"))
                .andExpect(jsonPath("$.countryOfOrigin").value("Russia"))
                .andExpect(jsonPath("$.discount").value(0.1))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("Should delete products")
    @Order(3)
    void deleteProduct() throws Exception {
        long productId = 1L;

        mockMvc.perform(delete("/product/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a non-empty list of products bu name")
    @Order(1)
    void findProductsByName() throws Exception {

        String productName = "Milk";

        mockMvc.perform(get("/product/search")
                        .param("name", productName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Milk"))
                .andExpect(jsonPath("$[0].description").value("Fresh and natural product"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[0].weight").value(1.0))
                .andExpect(jsonPath("$[0].category").doesNotExist()) // Check for absence of category or adjust to actual category values
                .andExpect(jsonPath("$[0].manufactureDate").value("2024-01-01"))
                .andExpect(jsonPath("$[0].expirationDate").value("2025-01-01"))
                .andExpect(jsonPath("$[0].manufacturer").value("Company A"))
                .andExpect(jsonPath("$[0].stockQuantity").value(100))
                .andExpect(jsonPath("$[0].sku").value("SKU12345"))
                .andExpect(jsonPath("$[0].brand").value("Brand A"))
                .andExpect(jsonPath("$[0].countryOfOrigin").value("Russia"))
                .andExpect(jsonPath("$[0].discount").value(0.1))
                .andExpect(jsonPath("$[0].available").value(true));
    }
}