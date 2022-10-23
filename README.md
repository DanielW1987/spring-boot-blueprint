# ReadMe

## Description

This repository contains a simple spring boot project with jwt authentication flow enabled. To demonstrate registration and login behaviour it also contains a postman collection.

## Backend

To set up the project please apply the following steps:

1. Run `docker-compose.yml` via command `docker-compose up -d --no-recreate`. This starts a postgres database.

2. Define the data source connection details in file `application.yml`:
    - `spring.datasource.username` (you have to use user `postgres`)
    - `spring.datasource.password` (password must match `POSTGRES_PASSWORD` of service `db` from `docker-compose.yml` file)
    - `spring.datasource.url` (`jdbc:postgresql://localhost:5432/jwt-auth-demo`, database name must match `POSTGRES_DB` of service `db` from `docker-compose.yml` file)
3. Define the JWT settings in file `application.yml`:
    - `security.jwt.secret`: Secret to sign JWT tokens
    - `security.jwt.expiration-in-ms`: Expiration time in ms for JWT access token

It is also possible to define all mentioned connection details as environment variables. In this case no variables in `application.yml` need to be changed. The names of the environment variables are already in the `application.yml` file. You can  define the environment variables for example within a Run Configuration in IntelliJ.

There are two example users with the following credentials:

| Email               | Password  |
| ------------------- | --------- |
| admin@example.com   | test      |
| user@example.com    | test      |

## Postman

Import the [attached Postman Collection](postman/spring-boot-blueprint.postman_collection.json) to test via Postman.
