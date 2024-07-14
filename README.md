# reader-assistant-backend

## Running

1. Create role with name "me" and password "123" in PostgreSQL (or change it in src/main/resources/application.yml)
2. Create DB "reader-assistant" with owner from previous point
3. Start app
4. Go to http://localhost:8081/swagger-ui/index.html

## Architecture

- Spring
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Liquibase
- Springdoc OpenAPI

## Features

- Endpoint security
- JWT invalidation on logout
- Sorting, filtering, pagination

## Frontend

https://github.com/W1zeR/reader-assistant-frontend
