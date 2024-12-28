package com.jonichi.peridot.envelope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point for the Envelope Service application.
 *
 * <p>This class initializes and runs the Spring Boot application dedicated to handling
 * envelope-related functionalities. It leverages the {@link SpringBootApplication}
 * annotation to configure and bootstrap the application.</p>
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jonichi.peridot"})
public class EnvelopeApp {

    /**
     * Main method to launch the Envelope Service application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EnvelopeApp.class, args);
    }
}
