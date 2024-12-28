package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
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
public class BudgetEnvelopeRepositoryTest {

    @Autowired
    private BudgetEnvelopeRepository budgetEnvelopeRepository;

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
        BudgetEnvelope budgetEnvelope1 = createTestBudgetEnvelope(1, new BigDecimal("1000"));
        BudgetEnvelope budgetEnvelope2 = createTestBudgetEnvelope(2, new BigDecimal("500"));
        budgetEnvelopeRepository.saveAllAndFlush(List.of(budgetEnvelope1, budgetEnvelope2));
    }

    private BudgetEnvelope createTestBudgetEnvelope(
            Integer envelopeId,
            BigDecimal amount
    ) {
        return BudgetEnvelope.builder()
                .budgetId(1)
                .envelopeId(envelopeId)
                .amount(amount)
                .recurring(true)
                .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        budgetEnvelopeRepository.deleteAll();
    }

    @Test
    public void getTotalExpenses_shouldReturnCorrectTotalExpenses() throws Exception {
        // given
        Integer budgetId = 1;

        // when
        BigDecimal totalExpenses = budgetEnvelopeRepository.getTotalExpenses(budgetId);

        // then
        assertThat(totalExpenses).isEqualTo(new BigDecimal("1500.00"));
    }

}
