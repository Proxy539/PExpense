package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CategoryDTO;
import org.example.exception.EntityNotFoundException;
import org.example.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.expample.utils.CategoryUtils.TEST_CATEGORY_ID;
import static org.expample.utils.CategoryUtils.buildCategoryDTO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void givenCategoryExistsWhenFindByIdThenReturnsCategory() throws Exception {

        var categoryDTO = buildCategoryDTO();

        when(categoryService.findById(TEST_CATEGORY_ID)).thenReturn(categoryDTO);

        var response = mockMvc.perform(get("/api/v1/categories/{id}", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(categoryDTO));

        verify(categoryService).findById(TEST_CATEGORY_ID);
    }

    @Test
    public void givenCategoryDoesNotExistsWhenFindByIdThenReturnsNotFound() throws Exception {

        when(categoryService.findById(TEST_CATEGORY_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/categories/{id}", TEST_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService).findById(TEST_CATEGORY_ID);
    }

    @Test
    public void givenNoCategoriesWhenFindAllThenReturnsEmptyList() throws Exception {

        var emptyList = List.<CategoryDTO>of();

        when(categoryService.findAll()).thenReturn(emptyList);

        var response = mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(emptyList));

        verify(categoryService).findAll();
    }

    @Test
    public void givenCategoryExistsWhenFindAllThenReturnsCategoriesList() throws Exception {
        var categoryDTO = buildCategoryDTO();
        var categoriesDTOs = List.of(categoryDTO);

        when(categoryService.findAll()).thenReturn(categoriesDTOs);

        var response = mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(categoriesDTOs));

        verify(categoryService).findAll();
    }

    @Test
    public void whenCreateCategoryThenReturnsCreatedCategory() throws Exception {
        var categoryDTO = buildCategoryDTO();

        when(categoryService.create(categoryDTO)).thenReturn(categoryDTO);

        var response = mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(categoryDTO));

        verify(categoryService).create(categoryDTO);
    }

    @Test
    public void givenCategoryDoesNotExistWhenUpdateCategoryThenThrowsNotFound() throws Exception {
        var categoryDTO = buildCategoryDTO();

        when(categoryService.update(TEST_CATEGORY_ID, categoryDTO)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/categories/{id}", TEST_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isNotFound());

        verify(categoryService).update(TEST_CATEGORY_ID, categoryDTO);
    }

    @Test
    public void givenCategoryExistsWhenUpdateCategoryThenReturnsUpdatedCategory() throws Exception {
        var categoryDTO = buildCategoryDTO();

        when(categoryService.update(TEST_CATEGORY_ID, categoryDTO)).thenReturn(categoryDTO);

        var response = mockMvc.perform(put("/api/v1/categories/{id}", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(categoryDTO));

        verify(categoryService).update(TEST_CATEGORY_ID, categoryDTO);
    }

    @Test
    public void givenCategoryExistsWhenDeleteCategoryThenReturnsNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/categories/{id}", TEST_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(TEST_CATEGORY_ID);
    }


}