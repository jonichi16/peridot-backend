package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
}
