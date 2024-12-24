package com.jonichi.peridot.budget.controller;

import com.jonichi.peridot.budget.dto.CreateBudgetDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @PostMapping("")
    public ResponseEntity<?> createBudget(
            @RequestBody @Valid CreateBudgetDTO createBudgetDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
