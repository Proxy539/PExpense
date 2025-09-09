package org.expample.utils;

import org.example.domain.Category;
import org.example.dto.CategoryDTO;

public class CategoryUtils {

    public static final Long TEST_CATEGORY_ID = 1L;
    public static final String TEST_CATEGORY_NAME = "test category";
    public static final String NEW_TEST_CATEGORY_NAME = "new test category";

    public static Category buildCategory() {
        return buildCategory(TEST_CATEGORY_ID, TEST_CATEGORY_NAME);
    }

    public static CategoryDTO buildCategoryDTO() {
        return buildCategoryDTO(TEST_CATEGORY_ID, TEST_CATEGORY_NAME);
    }

    public static Category buildCategory(Long id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static CategoryDTO buildCategoryDTO(Long id, String name) {
        return CategoryDTO.builder()
                .id(id)
                .name(name)
                .build();
    }


}
