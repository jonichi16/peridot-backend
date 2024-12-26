package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.DirectoryResourceAccessor;
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

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:14-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    private static Connection connection;

    @BeforeAll
    static void beforeAll() throws Exception {
        POSTGRES_CONTAINER.start();
        connection = DriverManager.getConnection(
                POSTGRES_CONTAINER.getJdbcUrl(),
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword()
        );

        applyDatabaseMigrations();
    }

    private static void applyDatabaseMigrations() throws Exception {
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new liquibase.database.jvm.JdbcConnection(connection));
        Liquibase liquibase = new Liquibase(
                "changelog/changelog-root.yml",
                new DirectoryResourceAccessor(new File("../../config/liquibase/")),
                database
        );
        liquibase.update("");
    }

    @AfterAll
    static void afterAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        POSTGRES_CONTAINER.stop();
    }

    @DynamicPropertySource
    static void configureDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @BeforeEach
    public void setUpTestData() {
        Budget budget = createTestBudget();
        budgetRepository.saveAndFlush(budget);
    }

    private Budget createTestBudget() {
        return Budget.builder()
                .userId(1)
                .amount(new BigDecimal("1000"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        budgetRepository.deleteAll();
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

    @Test
    public void updateCurrentBudget_shouldUpdateCurrentBudgetWithCorrectAmount() throws Exception {
        // given
        Integer userId = 1;
        LocalDate currentPeriod = LocalDate.of(2024, 12, 1);
        BigDecimal amount = new BigDecimal("5000.00");

        // when
        Budget currentBudget = budgetRepository.getCurrentBudget(userId, currentPeriod).get();
        assertThat(currentBudget.getCreatedDate()).isNotNull();
        assertThat(currentBudget.getUpdatedDate()).isNull();

        budgetRepository.updateCurrentBudget(userId, currentPeriod, amount);
        entityManager.flush();

        Budget updatedBudget = budgetRepository.getCurrentBudget(userId, currentPeriod).get();
        entityManager.refresh(updatedBudget);

        // then
        assertThat(updatedBudget.getUpdatedDate()).isNotNull();
        assertThat(updatedBudget.getAmount()).isEqualTo(amount);
    }

}
