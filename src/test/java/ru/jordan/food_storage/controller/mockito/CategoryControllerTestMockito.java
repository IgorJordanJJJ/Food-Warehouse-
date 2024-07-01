package ru.jordan.food_storage.controller.mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.jordan.food_storage.controller.CategoryController;
import ru.jordan.food_storage.dto.CategoryDto;
import ru.jordan.food_storage.facade.category.CategoryFacadeImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CategoryControllerTestMockito {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

//    @Mock
//    private CategoryFacadeImpl categoryFacadeImpl;

    @MockBean
    private CategoryFacadeImpl categoryFacadeImpl;

//    @InjectMocks
//    private CategoryController categoryController;

    @BeforeEach
    void setup() {
        //mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should return a non-empty list of categories")
    void shouldGetAllCategories() throws Exception {
        List<CategoryDto> categories = Arrays.asList(
                CategoryDto.builder()
                        .id(1L)
                        .name("Category1")
                        .description("Description1")
                        .createdDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                        .lastUpdatedDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                        .active(true)
                        .build(),
                CategoryDto.builder()
                        .id(2L)
                        .name("Category2")
                        .description("Description2")
                        .createdDate(LocalDateTime.of(2024, 1, 2, 12, 0))
                        .lastUpdatedDate(LocalDateTime.of(2024, 1, 2, 12, 0))
                        .active(false)
                        .build()
        );

        when(categoryFacadeImpl.getAllCategories()).thenReturn(categories);

//        MvcResult result = mockMvc.perform(get("/category"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        System.out.println("Response from /category endpoint: " + content);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].createdDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$[0].lastUpdatedDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Category2"))
                .andExpect(jsonPath("$[1].description").value("Description2"))
                .andExpect(jsonPath("$[1].createdDate").value("2024-01-02T12:00:00"))
                .andExpect(jsonPath("$[1].lastUpdatedDate").value("2024-01-02T12:00:00"))
                .andExpect(jsonPath("$[1].active").value(false));
    }

    @Test
    @DisplayName("Should return categories by id")
    void shouldGetCategoryById() throws Exception {

        Long categoryId = 1L;

        CategoryDto category = CategoryDto.builder()
                .id(categoryId)
                .name("Category1")
                .description("Description1")
                .createdDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .lastUpdatedDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .active(true)
                .build();

        when(categoryFacadeImpl.getCategoryById(categoryId)).thenReturn(category);

        mockMvc.perform(get("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category1"))
                .andExpect(jsonPath("$.description").value("Description1"))
                .andExpect(jsonPath("$.createdDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$.lastUpdatedDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should create category")
    void shouldCreateCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Category1")
                .description("Description1")
                .createdDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .lastUpdatedDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .active(true)
                .build();

        when(categoryFacadeImpl.saveCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryDtoJson));
    }

    @Test
    @DisplayName("Should update Category")
    void shouldUpdateCategory() throws Exception {

        Long categoryId = 1L;

        CategoryDto categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Category1")
                .description("Description1")
                .createdDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .lastUpdatedDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .active(true)
                .build();

        when(categoryFacadeImpl.updateCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryDtoJson));
    }

    @Test
    @DisplayName("Should delete Category")
    void shouldDeleteCategory() throws Exception {
        Long categoryId = 1L;

        doNothing().when(categoryFacadeImpl).deleteCategory(categoryId);

        mockMvc.perform(delete("/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryFacadeImpl).deleteCategory(categoryId);
    }

    @Test
    @DisplayName("Should return a non-empty list of categories bu name")
    void shouldFindCategoriesByName() throws Exception {

        String categoryName = "Category";
        List<CategoryDto> categories = Arrays.asList(
                CategoryDto.builder()
                        .id(1L)
                        .name("Category1")
                        .description("Description1")
                        .createdDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                        .lastUpdatedDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                        .active(true)
                        .build(),
                CategoryDto.builder()
                        .id(2L)
                        .name("Category2")
                        .description("Description2")
                        .createdDate(LocalDateTime.of(2024, 1, 2, 12, 0))
                        .lastUpdatedDate(LocalDateTime.of(2024, 1, 2, 12, 0))
                        .active(false)
                        .build()
        );

        when(categoryFacadeImpl.findCategoriesByName(categoryName)).thenReturn(categories);

        mockMvc.perform(get("/category/search")
                        .param("name", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].createdDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$[0].lastUpdatedDate").value("2024-01-01T12:00:00"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Category2"))
                .andExpect(jsonPath("$[1].description").value("Description2"))
                .andExpect(jsonPath("$[1].createdDate").value("2024-01-02T12:00:00"))
                .andExpect(jsonPath("$[1].lastUpdatedDate").value("2024-01-02T12:00:00"))
                .andExpect(jsonPath("$[1].active").value(false));
    }

    @Test
    @DisplayName("Should download excel report")
    void shouldExportIntoExcelFile() throws Exception{
        doNothing().when(categoryFacadeImpl).getCategoriesExcel(any(HttpServletResponse.class));

        mockMvc.perform(get("/category/export-to-excel"))
                .andExpect(status().isOk());

        verify(categoryFacadeImpl, times(1)).getCategoriesExcel(any(HttpServletResponse.class));
    }
}