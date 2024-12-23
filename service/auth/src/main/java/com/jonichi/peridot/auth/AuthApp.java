package com.jonichi.peridot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point for the Authentication Service application.
 *
 * <p>This class initializes and runs the Spring Boot application dedicated to handling
 * authentication-related functionalities. It leverages the {@link SpringBootApplication}
 * annotation to configure and bootstrap the application.</p>
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jonichi.peridot"})
public class AuthApp {

    /**
     * Main method to launch the Authentication Service application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }

}