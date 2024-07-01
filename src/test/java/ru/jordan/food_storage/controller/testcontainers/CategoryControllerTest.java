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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.jordan.food_storage.dto.CategoryDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:db/data/category/data_category.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:db/data/category/cleanup_data_categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerTest {

    private ObjectMapper objectMapper;
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16").withEnv("TZ", "UTC");

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return a non-empty list of categories")
    @Order(1)
    void shouldGetAllCategories() throws Exception {

//        MvcResult result = mockMvc.perform(get("/category")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//// Получение JSON-строки из ответа
//        String jsonResponse = result.getResponse().getContentAsString();
//
//        // Маппинг JSON-строки в список объектов CategoryDto
//        List<CategoryDto> expectedCategories = objectMapper.readValue(jsonResponse,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class));
//
//        // Проверка ожидаемых значений
//        Assertions.assertThat(expectedCategories).hasSize(2); // Предполагая, что в ответе две категории
//        Assertions.assertThat(expectedCategories.get(0).getId()).isEqualTo(1L);
//        Assertions.assertThat(expectedCategories.get(0).getName()).isEqualTo("Category1");
//        Assertions.assertThat(expectedCategories.get(0).getDescription()).isEqualTo("Description1");
//        Assertions.assertThat(expectedCategories.get(0).getCreatedDate()).isEqualTo("2024-01-01T19:00:00");
//        Assertions.assertThat(expectedCategories.get(0).getLastUpdatedDate()).isEqualTo("2024-01-01T19:00:00");
//        Assertions.assertThat(expectedCategories.get(0).getActive()).isTrue();
//
//        Assertions.assertThat(expectedCategories.get(1).getId()).isEqualTo(2L);
//        Assertions.assertThat(expectedCategories.get(1).getName()).isEqualTo("Category2");
//        Assertions.assertThat(expectedCategories.get(1).getDescription()).isEqualTo("Description2");
//        Assertions.assertThat(expectedCategories.get(1).getCreatedDate()).isEqualTo("2024-01-02T19:00:00");
//        Assertions.assertThat(expectedCategories.get(1).getLastUpdatedDate()).isEqualTo("2024-01-02T19:00:00");
//        Assertions.assertThat(expectedCategories.get(1).getActive()).isFalse();

        mockMvc.perform(get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].createdDate").value("2024-01-01T19:00:00"))
                .andExpect(jsonPath("$[0].lastUpdatedDate").value("2024-01-01T19:00:00"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Category2"))
                .andExpect(jsonPath("$[1].description").value("Description2"))
                .andExpect(jsonPath("$[1].createdDate").value("2024-01-02T19:00:00"))
                .andExpect(jsonPath("$[1].lastUpdatedDate").value("2024-01-02T19:00:00"))
                .andExpect(jsonPath("$[1].active").value(false));

    }

    @Test
    @DisplayName("Should return categories by id")
    @Order(1)
    void shouldGetCategoryById() throws Exception {

        mockMvc.perform(get("/category/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category1"))
                .andExpect(jsonPath("$.description").value("Description1"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should create category")
    @Order(2)
    void shouldCreateCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Category3")
                .description("Description3")
                .active(true)
                .build();


        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryDtoJson));
    }

    @Test
    @DisplayName("Should update Category")
    @Order(2)
    void shouldUpdateCategory() throws Exception {

        Long categoryId = 1L;

        CategoryDto categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Category1")
                .description("Description1")
                .active(true)
                .build();

        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryDtoJson));
    }

    @Test
    @DisplayName("Should delete Category")
    @Order(3)
    void shouldDeleteCategory() throws Exception {
        long categoryId = 1L;

        mockMvc.perform(delete("/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should return a non-empty list of categories bu name")
    @Order(1)
    void shouldFindCategoriesByName() throws Exception {

        String categoryName = "Category1";

        mockMvc.perform(get("/category/search")
                        .param("name", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @DisplayName("Should download excel report")
    @Order(1)
    void shouldExportIntoExcelFile() throws Exception {

        mockMvc.perform(get("/category/export-to-excel"))
                .andExpect(status().isOk());

    }
}
