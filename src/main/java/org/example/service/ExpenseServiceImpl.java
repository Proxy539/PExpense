package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Expense;
import org.example.dto.CategoryDTO;
import org.example.dto.ExpenseDTO;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CategoryRepository;
import org.example.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.utils.Constants.CATEGORY_NOT_FOUND_BY_NAME_MESSAGE;
import static org.example.utils.Constants.EXPENSE_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseDTO findById(Long id) {

        log.info("Finding expense by id {}", id);

        var expense = expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(EXPENSE_NOT_FOUND_BY_ID_MESSAGE, id)));

        return mapToDTO(expense);
    }

    @Override
    public List<ExpenseDTO> findAll() {

        log.info("Finding all expenses");

        var expenses = expenseRepository.findAll();

        return expenses.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ExpenseDTO create(ExpenseDTO expense) {
        log.info("Creating new expense {}", expense);

        var categoryName = expense.getCategory().getName();

        var category = categoryRepository.findByName(categoryName).orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_NAME_MESSAGE, categoryName)));

        var newExpense = Expense.builder()
                            .amount(expense.getAmount())
                            .description(expense.getDescription())
                            .date(expense.getDate())
                            .category(category)
                            .build();

        var savedExpense = expenseRepository.save(newExpense);

        return mapToDTO(savedExpense);
    }

    @Override
    public ExpenseDTO update(Long id, ExpenseDTO expense) {
        log.info("Updating expense {}", expense);

        Expense savedExpense = expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(EXPENSE_NOT_FOUND_BY_ID_MESSAGE, id)));

        savedExpense.setAmount(expense.getAmount());
        savedExpense.setDate(expense.getDate());
        savedExpense.setDescription(expense.getDescription());

        var updatedExpense = expenseRepository.save(savedExpense);

        return mapToDTO(updatedExpense);
    }

    @Override
    public void delete(Long id) {

        log.info("Deleting expense by id {}", id);

        expenseRepository.deleteById(id);
    }

    private ExpenseDTO mapToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .category(CategoryDTO.builder()
                        .id(expense.getCategory().getId())
                        .name(expense.getCategory().getName())
                        .build())
                .build();
    }
}
