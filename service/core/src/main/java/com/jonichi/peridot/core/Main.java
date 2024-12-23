package com.jonichi.peridot.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the application.
 *
 * <p>This class initializes and runs the Spring Boot application. It scans for components,
 * entities, and JPA repositories in the specified base packages, setting up the necessary
 * configurations to bootstrap the application.</p>
 *
 * <p>The annotations applied to this class perform the following:</p>
 * <ul>
 *   <li>{@link SpringBootApplication} - Combines the functionality of {@code @Configuration},
 *   {@code @EnableAutoConfiguration}, and {@code @ComponentScan} for a streamlined application
 *   setup.</li>
 *   <li>{@link ComponentScan} - Specifies the base packages to scan for Spring-managed
 *   components.</li>
 *   <li>{@link EnableJpaRepositories} - Enables Spring Data JPA repositories in the defined base
 *  *   packages.</li>
 *   <li>{@link EntityScan} - Configures the scanning of JPA entity classes within the specified
 *   packages.</li>
 * </ul>
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jonichi.peridot"})
@EnableJpaRepositories(basePackages = {"com.jonichi.peridot"})
@EntityScan(basePackages = {"com.jonichi.peridot"})
public class Main {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
