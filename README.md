# Access Control Project README
## Overview
This project is a Spring Boot application that manages user access control. It includes functionalities for user authentication, authorization, and role management. The application is structured to ensure security and ease of management for different user roles (ADMIN, RH, WORKER).

## Table of Contents
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [APIs](#apis)
  
## Project Structure
- **com.brunoams.accesscontrol.config.accounts**: Configuration for default admin user.
- **com.brunoams.accesscontrol.config.security**: Security configurations including JWT and filters.
- **com.brunoams.accesscontrol.domain**: Domain models such as User and Role.
- **com.brunoams.accesscontrol.exception**: Custom exceptions for error handling.
- **com.brunoams.accesscontrol.repository**: Repository interfaces for database access.
- **com.brunoams.accesscontrol.service**: Service classes for business logic.
- **com.brunoams.accesscontrol.web.controller**: REST controllers for handling HTTP requests.
- **com.brunoams.accesscontrol.web.dto**: Data Transfer Objects (DTOs) for API requests and responses.
- **com.brunoams.accesscontrol.web.exception**: Handlers for API exceptions.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- MySQL or any compatible relational database
- Postman

### Installation

1. Clone the repository:
```bash
git clone https://github.com/brunoams/accesscontrol.git
cd accesscontrol
```
2. Configure your database:
- Create a database named accesscontrol.
- Update the application.properties file with your database credentials.

3. Build the project using Maven:
```bash
mvn clean install
```
## Configuration
##### application.properties
Configure the following properties in src/main/resources/application.properties:
```
spring.datasource.url=jdbc:mysql://localhost:3306/accesscontrol
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.private.key=your_jwt_secret_key
```
> [!IMPORTANT]
To generate your own private key go to the directory you want to store it.
>
> Remember to store the app.key file in a safe place

```bash
openssl genrsa > app.key
```

## Running the Application

1. Start the application:
```bash
mvn spring-boot:run
```
2. The application will start on http://localhost:8080.

## APIs

### Authentication
#### Login
- ##### Endpoint: /auth/login
- ##### Method: POST
- ##### Request Body:
```bash
{
    "username": "admin@gmail.com",
    "password": "123456"
}
```
- ##### Response:
```bash
"accessToken": "jwt_token"
```
### User Management
#### Create User
- ##### Endpoint: /users
- ##### Method: POST
- ##### Request Body:
```bash
{
    "username": "newuser@gmail.com",
    "password": "password",
    "role": "WORKER"
}
```
- ##### Response:
```bash
{
    "id": 1,
    "username": "newuser@gmail.com",
    "password": "encoded_password"
}
```
#### Get All Users
- ##### Endpoint: /users
- ##### Method: GET
- ##### Response:
```bash
[
    {
        "id": 1,
        "username": "admin@gmail.com",
        "password": "encoded_password"
    },
    ...
]
```
#### Get User by ID
- ##### Endpoint: /users/{id}
- ##### Method: GET
- ##### Response:
```bash
{
    "id": 1,
    "username": "admin@gmail.com",
    "password": "encoded_password"
}
```
#### Update Password
- ##### Endpoint: /users/{id}
- ##### Method: PATCH
- ##### Request Body:
```bash
{
    "currentPassword": "current_password",
    "newPassword": "new_password",
    "repeatNewPassword": "new_password"
}
```
- ##### Response: 204 No Content

#### Delete User
- ##### Endpoint: /users/{id}
- ##### Method: DELETE
- ##### Response: 204 No Content


## Conclusion
This project provides a robust foundation for user access control using Spring Boot. It ensures security through JWT-based authentication and role-based authorization, making it suitable for a variety of applications requiring secure user management.

For any questions or contributions, feel free to create an issue or submit a pull request.

