# 📚 Book Management System

A REST API for managing books, built with Spring Boot.

## Prerequisites

- [Docker](https://www.docker.com/get-started) & Docker Compose

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/etornamklu/book-mgt-system.git
cd book-mgt-system
```

### 2. Start the application

```bash
docker compose up -d
```

The application will start on `http://localhost:8080`.

> The database is automatically seeded with sample books on first run.

## API Documentation

Once the application is running, visit:

```
http://localhost:8080/docs
```

This opens the Swagger UI where you can explore and test all available endpoints.

## Running Tests

```bash
mvn test
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/books` | Get all books (paginated) |
| GET | `/api/v1/books/{id}` | Get a book by ID |
| POST | `/api/v1/books` | Create a new book |
| PATCH | `/api/v1/books/{id}` | Update a book |
| DELETE | `/api/v1/books/{id}` | Delete a book |

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Docker
- SpringDoc OpenAPI (Swagger)