package org.example.service;

import org.example.dto.ExpenseDTO;
import org.example.exception.BadRequestException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CategoryRepository;
import org.example.repository.ExpenseRepository;
import org.expample.utils.CategoryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.expample.utils.CategoryUtils.buildCategory;
import static org.expample.utils.ExpenseUtils.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Test
    public void givenExpenseExistsWhenFindByIdThenReturnsExpense() {

        var savedExpense = buildExpense();
        var savedExpenseDTO = buildExpenseDTO();

        when(expenseRepository.findById(TEST_EXPENSE_ID))
                .thenReturn(Optional.of(savedExpense));

        var response = expenseService.findById(TEST_EXPENSE_ID);

        assertThat(response).isEqualTo(savedExpenseDTO);

        verify(expenseRepository).findById(TEST_EXPENSE_ID);
    }

    @Test
    public void givenExpenseDoNotExistsWhenFindByIdThenThrowsException() {

        when(expenseRepository.findById(TEST_EXPENSE_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.findById(TEST_EXPENSE_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(EXPENSE_NOT_FOUND_BY_ID_MESSAGE, TEST_EXPENSE_ID));

        verify(expenseRepository).findById(TEST_EXPENSE_ID);
    }

    @Test
    public void givenNoExpensesWhenFindAllThenReturnsEmptyList() {

        when(expenseRepository.findAll()).thenReturn(List.of());

        var expenses = expenseService.findAll();

        assertThat(expenses).isEmpty();

        verify(expenseRepository).findAll();
    }

    @Test
    public void givenExpenseExistsWhenFindAllThenReturnsExpensesList() {

        var expense = buildExpense();
        var expenses = List.of(expense);
        var expenseDTO = buildExpenseDTO();
        var expensesDTOs = List.of(expenseDTO);

        when(expenseRepository.findAll()).thenReturn(expenses);

        var response = expenseService.findAll();

        assertThat(response).isEqualTo(expensesDTOs);

        verify(expenseRepository).findAll();

    }

    @Test
    public void givenCategoryDoesNotExistsWhenCreateExpenseThenThrowsException() {

        var expenseDTO = buildExpenseDTO();

        when(categoryRepository.findByName(expenseDTO.getCategory().getName()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.create(expenseDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_NAME_MESSAGE, expenseDTO.getCategory().getName()));

        verify(categoryRepository).findByName(expenseDTO.getCategory().getName());
    }

    @Test
    public void givenCategoryExistsWhenCreateExpenseThenReturnsCreatedExpense() {

        var expenseDTO = buildExpenseDTO();
        var category = buildCategory();
        var expense = buildExpense();
        expense.setId(null);
        var savedExpense = buildExpense();

        when(categoryRepository.findByName(expenseDTO.getCategory().getName()))
                .thenReturn(Optional.of(category));
        when(expenseRepository.save(expense)).thenReturn(savedExpense);

        var response = expenseService.create(expenseDTO);

        assertThat(response).isEqualTo(expenseDTO);

        verify(categoryRepository).findByName(expenseDTO.getCategory().getName());
        verify(expenseRepository).save(expense);

    }

    @Test
    public void givenExpenseDoesNotExistWhenUpdateThenThrowsException() {

        var expenseDTO = buildExpenseDTO();

        when(expenseRepository.findById(TEST_EXPENSE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.update(TEST_EXPENSE_ID, expenseDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(EXPENSE_NOT_FOUND_BY_ID_MESSAGE, TEST_EXPENSE_ID));

        verify(expenseRepository).findById(TEST_EXPENSE_ID);
    }

    @Test
    public void givenExpenseExistsWhenUpdateThenReturnsUpdatedExpense() {

        var expenseDTO = buildExpenseDTO();
        var expense = buildExpense();
        expense.setId(null);
        var savedExpense = buildExpense();

        when(expenseRepository.findById(TEST_EXPENSE_ID)).thenReturn(Optional.of(expense));
        when(expenseRepository.save(expense)).thenReturn(savedExpense);

        var updatedExpense = expenseService.update(TEST_EXPENSE_ID, expenseDTO);

        assertThat(updatedExpense).isEqualTo(expenseDTO);

        verify(expenseRepository).findById(TEST_EXPENSE_ID);
        verify(expenseRepository).save(expense);
    }

    @Test
    public void givenExpenseExistsWhenDeleteByIdThenReletsExpense() {

        expenseService.delete(TEST_EXPENSE_ID);

        verify(expenseRepository).deleteById(TEST_EXPENSE_ID);
    }

}