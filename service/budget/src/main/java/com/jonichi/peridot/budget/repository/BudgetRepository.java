package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Budget} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing built-in methods
 * for standard CRUD operations on the {@code Budget} entity, such as saving, finding,
 * updating, and deleting records.</p>
 *
 */
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
}
