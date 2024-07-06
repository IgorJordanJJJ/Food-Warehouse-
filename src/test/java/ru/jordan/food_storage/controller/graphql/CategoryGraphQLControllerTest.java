package ru.jordan.food_storage.controller.graphql;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.jordan.food_storage.controller.beans.TestBeans;
import ru.jordan.food_storage.dto.CategoryDto;

import java.util.List;

@Testcontainers
@SpringBootTest(classes = TestBeans.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureGraphQlTester
@Sql(scripts = "classpath:db/data/category/data_category.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:db/data/category/cleanup_data_categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CategoryGraphQLControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    private static final String MINIO_IMAGE = "minio/minio";
    @Container
    public static GenericContainer<?> minioContainer = new GenericContainer<>(MINIO_IMAGE)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
            .withCommand("server /data")
            .withExposedPorts(9000);

    @BeforeEach
    void setup() {
        minioContainer.start();
    }

    @DynamicPropertySource
    static void registerMinioProperties(DynamicPropertyRegistry registry) {
        String minioUrl = "http://" + minioContainer.getHost() + ":" + minioContainer.getFirstMappedPort();
        registry.add("minio.url", () -> minioUrl);
        registry.add("minio.access-key", () -> "minioadmin");
        registry.add("minio.secret-key", () -> "minioadmin");
    }
    @Test
    @Order(1)
    @DisplayName("Should return categories by id")
    void shouldGetCategoryById() {
        Long categoryId = 1L;
        // language=GraphQL
        String document = """
        query getCategoryById($id: ID!) {
             getCategoryById(id: $id){
             id
             name
             description
             active
             }
        }            
        """;

        CategoryDto categoryDto = graphQlTester.document(document)
                .variable("id", categoryId)
                .execute()
                .path("getCategoryById")
                .entity(CategoryDto.class)  // Assuming there is a Category class
                .matches(category -> category.getId().equals(categoryId))
                .get(); // Replace with your assertion
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryId, categoryDto.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Should return a non-empty list of categories bu name")
    void findCategoriesByName() {
        String categoryName = "Category1";
        // language=GraphQL
        String document = """
        query findCategoriesByName($name: String!) {
             findCategoriesByName(name: $name){
             id
             name
             description
             active
             }
        }            
        """;

        graphQlTester.document(document)
                .variable("name", categoryName)
                .execute()
                .path("findCategoriesByName")
                .entityList(CategoryDto.class)  // Assuming there is a Category class
                .matches(category -> category.get(0).getName().equals(categoryName)); // Replace with your assertion

    }

    @Test
    @Order(1)
    @DisplayName("Should return a non-empty list of categories")
    void shouldGetAllCategories() {
        // language=GraphQL
        String document = """
        query {
            getAllCategories {
                id
                name
                description
                active
            }
        }            
        """;

        List<CategoryDto> categoryDtoList = graphQlTester.document(document)
                .execute()
                .path("getAllCategories")
                .entityList(CategoryDto.class)
                .get();
        Assertions.assertTrue(categoryDtoList.size()>1);
        Assertions.assertNotNull(categoryDtoList.get(0).getName());
        Assertions.assertNotNull(categoryDtoList.get(0).getActive());
    }

    @Test
    @Order(2)
    @DisplayName("Should create category")
    void shouldCreateCategory() {
        // language=GraphQL
        String document = """
                mutation {
                createCategory(input: {
                name: "Category3"
                description: "new category"
                active: true
                }){
                    id
                }
                    }          
                """;

        CategoryDto categoryDto = graphQlTester.document(document)
                .execute()
                .path("createCategory")
                .entity(CategoryDto.class)
                .get();  // Assuming there is a Category class

        Assertions.assertNotNull(categoryDto);
    }

    @Test
    @Order(3)
    @DisplayName("Should update Category")
    void shouldUpdateCategory() {
        // language=GraphQL
        String document = """
                mutation {
                updateCategory(input: {
                id: 1 
                name: "Category4"
                active: true
                }){
                    id
                    name
                }
                    }          
                """;

        CategoryDto categoryDto = graphQlTester.document(document)
                .execute()
                .path("updateCategory")
                .entity(CategoryDto.class)
                .get();  // Assuming there is a Category class

        Assertions.assertNotNull(categoryDto);
    }

    @Test
    @Order(4)
    @DisplayName("Should delete Category")
    void shouldDeleteCategory() {
        // language=GraphQL
        String document = """
                mutation {
                deleteCategory(id: 1)
                    }          
                """;

        graphQlTester.document(document)
                .execute()
                .path("deleteCategory");

    }
}