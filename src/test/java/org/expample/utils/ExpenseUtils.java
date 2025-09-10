package org.expample.utils;

import org.example.domain.Category;
import org.example.domain.Expense;
import org.example.dto.CategoryDTO;
import org.example.dto.ExpenseDTO;

import java.time.LocalDate;
import java.time.Month;

public class ExpenseUtils {

    public static final String EXPENSE_NOT_FOUND_BY_ID_MESSAGE = "Expense not found by id %d";
    public static final String CATEGORY_NOT_FOUND_BY_NAME_MESSAGE = "Category not found by name %s";
    public static final Long TEST_EXPENSE_ID = 1L;
    public static final String TEST_EXPENSE_DESCRIPTION = "test description";
    public static final Double TEST_EXPENSE_AMOUNT = 1.1;
    public static final LocalDate TEST_EXPENSE_DATE = LocalDate.of(2025, Month.SEPTEMBER, 10);

    public static Expense buildExpense(Long id, String description, Double amount, LocalDate date, Category category) {
        return Expense.builder()
                .id(id)
                .description(description)
                .amount(amount)
                .date(date)
                .category(category)
                .build();
    }

    public static ExpenseDTO buildExpenseDTO(Long id, String description, Double amount, LocalDate date, CategoryDTO categoryDTO) {
        return ExpenseDTO.builder()
                .id(id)
                .description(description)
                .amount(amount)
                .date(date)
                .category(categoryDTO)
                .build();
    }

    public static Expense buildExpense() {
        return Expense.builder()
                .id(TEST_EXPENSE_ID)
                .description(TEST_EXPENSE_DESCRIPTION)
                .amount(TEST_EXPENSE_AMOUNT)
                .date(TEST_EXPENSE_DATE)
                .category(CategoryUtils.buildCategory())
                .build();
    }

    public static ExpenseDTO buildExpenseDTO() {
        return ExpenseDTO.builder()
                .id(TEST_EXPENSE_ID)
                .description(TEST_EXPENSE_DESCRIPTION)
                .amount(TEST_EXPENSE_AMOUNT)
                .date(TEST_EXPENSE_DATE)
                .category(CategoryUtils.buildCategoryDTO())
                .build();
    }


}
