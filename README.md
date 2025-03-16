# Vietnamese Puzzle API

This project is a Spring Boot API that solves a Vietnamese puzzle. The puzzle consists of finding the unique permutation of the digits 1 to 9 that satisfies the following equation:

`A + 13 * (B / C) + D + E + 12 * F - G - 11 + (H * I) / G - 10
`

Each valid attempt is stored in an H2 database, and the API provides endpoints to generate solutions as well as perform CRUD operations on the attempts.

---

## Technologies

- **Java:** 17
- **Spring Boot:** 3.x
- **Spring Data JPA**
- **H2 Database**
- **Maven**

---

## Prerequisites

- **JDK 17** must be installed on your system.
- Maven installed (or use the Maven wrapper included in the project).
- An IDE (e.g., IntelliJ IDEA) configured to use JDK 17.

---

## Setup & Build

1. **Clone the repository:**

```bash
git clone git@github.com:olivb40/test-vietnamese-puzzle-backend.git
cd vietnamese-puzzle
```

2. **Build the project:**

`mvn clean package
`

This will compile the project and package it as an executable JAR.

## Running the API

You can run the API in one of the following ways:

### Using Maven
`mvn spring-boot:run
`

### Using the JAR File
After building the project, run:

`java -jar target/vietnamese-puzzle-0.0.1-SNAPSHOT.jar
`

The API will start on port 8080 by default.

## Running Tests
Unit tests are written using JUnit 5 and Mockito. To run the tests, execute:

`mvn test
`