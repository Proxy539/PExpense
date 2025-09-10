package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ExpenseDTO;
import org.example.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/{id}")
    public ExpenseDTO findById(@PathVariable Long id) {
        return expenseService.findById(id);
    }

    @GetMapping
    public List<ExpenseDTO> findAll() {
        return expenseService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseDTO create(@RequestBody ExpenseDTO expense) {
        return expenseService.create(expense);
    }

    @PutMapping("/{id}")
    public ExpenseDTO update(@PathVariable Long id, @RequestBody ExpenseDTO expense) {
        return expenseService.update(id, expense);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }

}
