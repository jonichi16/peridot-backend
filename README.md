# Peridot Backend

A simple budget tracking application using the **envelope system** to manage expenses. Built with **Spring Boot**, **Lombok**, **PostgreSQL**, and **Docker**.

---

## Features

- User Authentication
- Envelope-based budget allocation for categories (e.g., Food, Rent, etc.)
- Expense tracking with automatic deduction from corresponding envelopes
- Monthly summary and budget analysis (to be added)
- REST API for interaction with the backend

---

## Tech Stack

- **Java** (JDK 21)
- **Spring Boot** for backend development
- **PostgreSQL** as the database
- **Docker** for containerization
- **Lombok** for boilerplate code reduction

---

## Prerequisites

- Docker and Docker Compose installed
- Java 21 installed
- Maven installed

---

## Folder Structure

```text
peridot-backend/
├── .github/                  # GitHub workflows and configurations
├── .mvn/                     # Maven wrapper files
├── config/                   # Configuration files
├── lib/                      # Shared libraries and utilities
│   └── common/               # Common utilities for the project
├── service/                  # Main application services
│   ├── auth/                 # User authentication service
│   ├── budget/               # Budget and envelope management service
│   ├── core/                 # Core functionality
│   └── envelope/             # Envelope-specific logic
├── .dockerignore             # Files and directories to ignore in Docker builds
├── .env                      # Environment variables for local development
├── .env-sample               # Sample environment file
├── .gitattributes            # Git attributes for repository settings
├── .gitignore                # Git ignored files
├── docker-compose.build.yml  # Docker Compose file for building the application
├── docker-compose.yml        # Main Docker Compose file for the application
├── Dockerfile                # Docker image definition
├── HELP.md                   # Additional help documentation
├── mvnw                      # Maven wrapper for Linux/Mac
├── mvnw.cmd                  # Maven wrapper for Windows
├── pom.xml                   # Maven project descriptor
├── README.md                 # Project README file
└── run                       # Custom script for managing the project
```

---

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/jonichi16/peridot-backend.git
cd peridot-backend
```

### 2. Start server
```bash
./run build
```

---

## Development workflow

### 1. Run Test
```bash
mvn clean test
```

### 2. Run Lint
```bash
./run lint
```

---

## Documentations

### 1. Test coverage
```bash
mvn clean test
```

View test coverage in /target/site/jacoco/index.html

### 2. API Documentation
```bash
./run docs
```

View test coverage in /target/generated-docs/index.html

---
