package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Category;
import org.example.dto.CategoryDTO;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.utils.Constants.CATEGORY_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO findById(Long id) {

        log.info("Finding category by id {}", id);

        var category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID_MESSAGE, id)));

        return mapToDTO(category);

    }

    @Override
    public List<CategoryDTO> findAll() {

        log.info("Finding all categories");

        var categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::mapToDTO)
                .toList();

    }

    @Override
    public CategoryDTO create(CategoryDTO category) {

        log.info("Creating new category {}", category);

        var newCategory = Category.builder()
                        .name(category.getName())
                        .build();

        var savedCategory = categoryRepository.save(newCategory);

        return mapToDTO(savedCategory);

    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO category) {

        log.info("Updating category {}", category);

        Category savedCategory = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID_MESSAGE, id)));

        savedCategory.setName(category.getName());

        var updatedCategory = categoryRepository.save(savedCategory);

        return mapToDTO(updatedCategory);
    }

    @Override
    public void delete(Long id) {

        log.info("Deleting category by id {}", id);

        categoryRepository.deleteById(id);

    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
