
[GoldenGit.com](https://goldengit.com/) is a web-based analytics dashboard that offers powerful insights into popular GitHub repositories. Designed for open-source contributors, project maintainers, and curious developers, GoldenGit helps you understand project health, development velocity, and team dynamics at a glance.

### Key Features
- Commit Frequency Analysis – Track how actively a repository is being maintained over time
- Pull Request Cycle Time – Visualize average time from PR creation to merge/close
- Developer Engagement Metrics – See who’s contributing, reviewing, and how consistently
- AI-Generated Summaries – Get concise, automated insights based on the latest pull requests using OpenAI integration
- Beautiful, Real-Time Dashboards – Built with React.js and powered by a Spring Boot backend and GitHub API

## Architecture
![goldengit-architecture](https://github.com/user-attachments/assets/7303284a-af9d-4781-b614-4b36238cd19d)


## AI generated insights
![image](https://github.com/user-attachments/assets/881abcfd-f41c-480a-88f8-a2244e85ac37)

## Repository Analytics
![image](https://github.com/user-attachments/assets/a49f5fcd-4c3f-4936-9880-e2664c1884b0)

## Developers Engagement
![image](https://github.com/user-attachments/assets/33892b91-74ea-43e1-859d-fa76ab791e63)


## Prerequisites
- Java 17 or higher installed on your system.
- Maven installed to build and run the project.

## Setting the environment variables
Set the following env vars to run the application locally:

| Variable                 | Description                                  |
|--------------------------|----------------------------------------------|
| GITHUB_API_TOKEN   | The API token used to consume the GitHub API |
| MYSQL_USER         | The MySQL username                           |
| MYSQL_PASSWORD     | The MySQL password                           |
| OPENAI_API_KEY     | The key used to consume the OpenAI API       |

## Docker compose
Run the Docker compose with the environment variables defined inside the docker-compose.yaml file

## Creating the package
Run the following command to create the application package:  
```mvn package``` 

## Running the application locally
Run the application passing the ```dev``` profile to Spring, for example:

```java -jar goldengit-0.0.1-SNAPSHOT.jar  --spring.profiles.active=dev```

# Project structure

## 1. [`application`](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application)

This layer typically includes service logic, DTOs, queries, and mapping/validation utilities.

### Subfolders:

- **[dto](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application/dto)**
  - *Technologies*: Java classes (POJOs), Lombok (for getters/setters), and possibly validation annotations (`javax.validation`).
  - *Purpose*: Data Transfer Objects (DTOs) used for communication between layers.

- **[mapper](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application/mapper)**
  - *Technologies*: MapStruct (for type-safe mapping), or manual mapping utilities.
  - *Purpose*: Converts between domain models and DTOs.

- **[query](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application/query)**
  - *Technologies*: Java, sometimes QueryDSL, or custom classes for encapsulating search/filter logic.
  - *Purpose*: Query objects or specifications for flexible searching/filtering.

- **[service](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application/service)**
  - *Technologies*: Spring `@Service` annotations, Java, possible use of transactional annotations.
  - *Purpose*: Business logic and orchestration between domain and web layers.

- **[validation](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/application/validation)**
  - *Technologies*: Java, Bean Validation (`javax.validation` or `jakarta.validation`), Spring Validators.
  - *Purpose*: Custom validation logic for DTOs and commands.

---

## 2. [`domain`](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/domain)

This is the heart of business rules and domain logic.

### Subfolders:

- **[common](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/domain/common)**
  - *Technologies*: Java, Utility classes, possibly interfaces for domain events.
  - *Purpose*: Shared domain utilities, constants, or base classes.

- **[exception](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/domain/exception)**
  - *Technologies*: Java, custom exception classes.
  - *Purpose*: Domain-specific exceptions.

- **[model](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/domain/model)**
  - *Technologies*: Java, JPA annotations (for entities), Lombok, Value Objects and Aggregates.
  - *Purpose*: Core domain models/entities and value objects.

---

## 3. [`infra`](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/infra)

Infrastructure layer: technical concerns, integration with external systems.

### Subfolders:

- **[api](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/infra/api)**
  - *Technologies*: Java, Spring WebClient/RestTemplate, Feign clients, OpenAPI, etc.
  - *Purpose*: Integrations with external APIs.

- **[config](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/infra/config)**
  - *Technologies*: Spring `@Configuration`, Java, application properties.
  - *Purpose*: Configuration classes for beans, security, scheduling, etc.

- **[db](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/infra/db)**
  - *Technologies*: Spring Data JPA, Hibernate, Liquibase/Flyway, native queries.
  - *Purpose*: Data access logic, repositories, migration scripts.

---

## 4. [`web`](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/web)

Web layer: incoming HTTP requests, controllers, filters.

### Subfolders:

- **[controller](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/web/controller)**
  - *Technologies*: Spring MVC (`@RestController`, `@RequestMapping`), Java.
  - *Purpose*: REST API endpoints.

- **[filter](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/web/filter)**
  - *Technologies*: Java, Servlet Filters, Spring `OncePerRequestFilter`.
  - *Purpose*: HTTP request/response filtering, security, logging.

- **[mapper](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/web/mapper)**
  - *Technologies*: Java, MapStruct, custom mappers.
  - *Purpose*: Web-specific model mapping.

- **[model](https://github.com/leosrdev/goldengit-be/tree/ccd47fca74b7fb5384c2998945ac508052340ec9/src/main/java/com/goldengit/web/model)**
  - *Technologies*: Java, DTOs for API requests/responses, validation annotations.
  - *Purpose*: Web-facing models, request and response objects.

---

## Summary Table

| Layer         | Technologies                          | Responsibilities                       |
|---------------|---------------------------------------|----------------------------------------|
| application   | Java, Spring, Lombok, MapStruct       | Service logic, DTOs, mapping, validation|
| domain        | Java, JPA, Lombok                    | Core business logic, entities, exceptions|
| infra         | Spring Data, Config, APIs, DB         | Persistence, configuration, integrations|
| web           | Spring MVC, Filters                   | REST endpoints, request/response models |

# Tech Stack

## Core Platform & Language
- **Java**: 17
- **Spring Boot**: 3.2.5

## Spring Boot Starters
- spring-boot-starter-web
- spring-boot-starter-security
- spring-boot-starter-webflux
- spring-boot-starter-data-jpa
- spring-boot-starter-data-redis
- spring-boot-starter-amqp
- spring-boot-starter-mail
- spring-boot-starter-validation
- spring-boot-starter-test
- spring-boot-starter-actuator

## Database
- **MySQL** (mysql-connector-j)

## ORM
- **JPA** (via spring-boot-starter-data-jpa)

## Redis
- **Spring Data Redis** (spring-boot-starter-data-redis)

## Messaging
- **AMQP** (via spring-boot-starter-amqp, likely RabbitMQ)

## Email
- **JavaMail** (spring-boot-starter-mail)

## Validation
- **Hibernate Validator** (spring-boot-starter-validation)

## Testing
- **JUnit, Mockito, etc.** (spring-boot-starter-test)

## Monitoring & Metrics
- **Actuator** (spring-boot-starter-actuator)
- **Micrometer Prometheus Registry**: micrometer-registry-prometheus

## API Documentation
- **OpenAPI/Swagger UI**: springdoc-openapi-starter-webmvc-ui: 2.5.0

## AI Integration
- **spring-ai-starter-model-openai**
- **spring-ai-bom**: 1.0.0-SNAPSHOT

## Dependency Injection and Utilities
- **Lombok**: 1.18.30

## API Rate Limiting
- **Bucket4j**: bucket4j_jdk17-core: 8.12.0

## YAML Processing
- **SnakeYAML**: 2.0

## JWT Authentication
- **jjwt-api**: 0.12.6
- **jjwt-impl**: 0.12.6
- **jjwt-jackson**: 0.12.6

## GitHub API Integration
- **github-api**: 1.321

## Miscellaneous
- **com.zliio.disposable: disposable**: 1.0

## Build Plugins
- **spring-boot-maven-plugin**
- **maven-compiler-plugin**: 3.11.0

## Repositories
- Spring Milestones
- Spring Snapshots

