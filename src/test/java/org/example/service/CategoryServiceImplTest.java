package org.example.service;

import org.example.domain.Category;
import org.example.dto.CategoryDTO;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CategoryRepository;
import org.expample.utils.CategoryUtils;
import org.expample.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.expample.utils.CategoryUtils.*;
import static org.expample.utils.Constants.CATEGORY_NOT_FOUND_BY_ID_MESSAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void givenCategoryExistsWhenFindByIdThenReturnsCategory() {

        var expectedCategory = CategoryUtils.buildCategory();
        var expectedCategoryDTO = CategoryUtils.buildCategoryDTO();

        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.of(expectedCategory));

        var category = categoryService.findById(TEST_CATEGORY_ID);

        assertThat(category).isEqualTo(expectedCategoryDTO);

        verify(categoryRepository).findById(TEST_CATEGORY_ID);
    }

    @Test
    public void givenCategoryNotExistsWhenFindByIdThenThrowsException() {

        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(TEST_CATEGORY_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID_MESSAGE, TEST_CATEGORY_ID));

        verify(categoryRepository).findById(TEST_CATEGORY_ID);
    }

    @Test
    public void givenNoCategoriesWhenFindAllThenReturnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        var categories = categoryService.findAll();

        assertThat(categories).isEmpty();

        verify(categoryRepository).findAll();
    }

    @Test
    public void givenCategoriesExistWhenFindAllThenReturnsCategoriesList() {

        var savedCategory = buildCategory();
        var savedCategoryDTO = buildCategoryDTO();
        var savedCategories = List.of(savedCategory);
        var savedCategoriesDTOs = List.of(savedCategoryDTO);

        when(categoryRepository.findAll()).thenReturn(savedCategories);

        var foundCategories = categoryService.findAll();

        assertThat(foundCategories).isEqualTo(savedCategoriesDTOs);

        verify(categoryRepository).findAll();
    }

    @Test
    public void whenCreateNewCatalogThenReturnsCreatedCategory() {

        var createCategoryDTO = buildCategoryDTO();
        var newCategory = buildCategory(null, TEST_CATEGORY_NAME);
        var savedCategory = buildCategory();

        when(categoryRepository.save(newCategory)).thenReturn(savedCategory);

        var createdCategoryDTO = categoryService.create(createCategoryDTO);

        assertThat(createdCategoryDTO).isEqualTo(createCategoryDTO);

        verify(categoryRepository).save(newCategory);
    }

    @Test
    public void whenCategoryExistsWhenUpdateCategoryThenReturnsUpdatedCategory() {

        var updateCategoryDTO = buildCategoryDTO(TEST_CATEGORY_ID, NEW_TEST_CATEGORY_NAME);
        var updateCategory = buildCategory(TEST_CATEGORY_ID, NEW_TEST_CATEGORY_NAME);
        var savedCategory = buildCategory();

        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save(updateCategory)).thenReturn(updateCategory);

        var updatedCategory = categoryService.update(TEST_CATEGORY_ID, updateCategoryDTO);

        assertThat(updatedCategory).isEqualTo(updatedCategory);

        verify(categoryRepository).findById(TEST_CATEGORY_ID);
        verify(categoryRepository).save(updateCategory);

    }

    @Test
    public void givenCategoryDoesNotExistWhenUpdateCategoryThenThrowsException() {

        var updateCategoryDTO = buildCategoryDTO();

        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(TEST_CATEGORY_ID, updateCategoryDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID_MESSAGE, TEST_CATEGORY_ID));
    }

    @Test
    public void givenCategoryExistsWhenDeleteByIdThenDeletesCategory() {

        categoryService.delete(TEST_CATEGORY_ID);

        verify(categoryRepository).deleteById(TEST_CATEGORY_ID);

    }

}