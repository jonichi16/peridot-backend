package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @PersistenceContext
    private EntityManager entityManager;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14-alpine"
    );

    @BeforeAll
    static void beforeAll()  {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void setUp() {
        Budget budget = Budget.builder()
                .userId(1)
                .amount(new BigDecimal("1000"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        budgetRepository.saveAndFlush(budget);
    }

    @AfterEach
    public void cleanUp() {
        budgetRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void getCurrentBudget_shouldReturnTheCurrentBudget() throws Exception {
        // given
        Integer userId = 1;
        LocalDate currentPeriod = LocalDate.of(2024, 12, 1);

        // when
        Optional<Budget> budgetDataDTO = budgetRepository.getCurrentBudget(userId, currentPeriod);

        // then
        assertThat(budgetDataDTO.isPresent()).isTrue();
    }

}
