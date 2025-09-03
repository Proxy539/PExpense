package org.example.service;

import org.example.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {

    ExpenseDTO findById(Long id);
    List<ExpenseDTO> findAll();
    void create(ExpenseDTO expense);
    void update(Long id, ExpenseDTO expense);
    void delete(Long id);
}
