package org.example.service;

import org.example.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(Long id);
    List<CategoryDTO> findAll();
    CategoryDTO create(CategoryDTO expense);
    CategoryDTO update(Long id, CategoryDTO expense);
    void delete(Long id);

}
