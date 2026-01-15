package com.FinanceTrackerLLMService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FinanceTrackerLLMService.DTO.ExpenseDTO;
import com.FinanceTrackerLLMService.Service.ExpenseService;

import lombok.NonNull;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ----------------------
    // GET expenses by userId
    // ----------------------
    @GetMapping("/get")
    public ResponseEntity<List<ExpenseDTO>> getExpense(
            @RequestParam("user_id") @NonNull String userId) {

        try {
            List<ExpenseDTO> expenseDtoList = expenseService.getExpenses(userId);
            return ResponseEntity.ok(expenseDtoList);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ----------------------
    // ADD expense
    // ----------------------
    @PostMapping("/add")
    public ResponseEntity<Boolean> addExpense(
            @RequestHeader("X-User-Id") @NonNull String userId,
            @RequestBody ExpenseDTO expenseDto) {

        try {
            expenseDto.setUserId(userId);
            boolean added = expenseService.createExpense(expenseDto);
            return ResponseEntity.ok(added);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
}
