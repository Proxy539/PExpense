package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ExpenseDTO;
import org.example.exception.EntityNotFoundException;
import org.example.service.ExpenseService;
import org.expample.utils.ExpenseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.expample.utils.ExpenseUtils.TEST_EXPENSE_ID;
import static org.expample.utils.ExpenseUtils.buildExpenseDTO;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    @Test
    public void givenExpenseExistsWhenFindByIdThenReturnExpense() throws Exception {

        var expenseDTO = buildExpenseDTO();

        when(expenseService.findById(TEST_EXPENSE_ID)).thenReturn(expenseDTO);

        var response = mockMvc.perform(get("/api/v1/expenses/{id}", TEST_EXPENSE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(expenseDTO));

        verify(expenseService).findById(TEST_EXPENSE_ID);
    }

    @Test
    public void givenExpenseDoesNotExistWhenFindByIdThenReturnsNotFound() throws Exception {

        when(expenseService.findById(TEST_EXPENSE_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/expenses/{id}", TEST_EXPENSE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(expenseService).findById(TEST_EXPENSE_ID);
    }
    
    @Test
    public void givenNoExpensesWhenFindAllThenReturnsEmptyList() throws Exception {

        var emptyList = List.<ExpenseDTO>of();
        when(expenseService.findAll()).thenReturn(emptyList);

        var response = mockMvc.perform(get("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(emptyList));

        verify(expenseService).findAll();
    }

    @Test
    public void whenCreateExpenseThenReturnsCreatedExpense() throws Exception {

        var expenseDTO = buildExpenseDTO();

        when(expenseService.create(expenseDTO)).thenReturn(expenseDTO);

        var response = mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(expenseDTO));

        verify(expenseService).create(expenseDTO);

    }

    @Test
    public void whenCategoryDoesNotExistWhenCreateExpenseThenReturnsNotFound() throws Exception {

        var expenseDTO = buildExpenseDTO();

        when(expenseService.create(expenseDTO)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isNotFound());

        verify(expenseService).create(expenseDTO);

    }

    @Test
    public void givenExpenseDoesNotExistWhenUpdateExpenseThenReturnsNotFound() throws Exception {
        var expenseDTO = buildExpenseDTO();

        when(expenseService.update(TEST_EXPENSE_ID, expenseDTO)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/expenses/{id}", TEST_EXPENSE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isNotFound());

        verify(expenseService).update(TEST_EXPENSE_ID, expenseDTO);
    }

    @Test
    public void givenExpenseExistsWhenUpdateExpenseThenReturnsUpdatedExpense() throws Exception {
        var expenseDTO = buildExpenseDTO();

        when(expenseService.update(TEST_EXPENSE_ID, expenseDTO)).thenReturn(expenseDTO);

        var response = mockMvc.perform(put("/api/v1/expenses/{id}", TEST_EXPENSE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(expenseDTO));

        verify(expenseService).update(TEST_EXPENSE_ID, expenseDTO);
    }

    @Test
    public void givenExpenseExistsWhenDeleteExpenseThenReturnsNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/expenses/{id}", TEST_EXPENSE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(expenseService).delete(TEST_EXPENSE_ID);

    }



}