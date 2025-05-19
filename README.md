# Project Management Backend API (Spring Boot)

This is the backend API for the Enova Robotics Project Management Platform, built with Java and the Spring Boot framework. It handles project creation and management, pole (team/department) organization, task assignments, personnel management, KPI calculations, and image uploads for user/pole avatars.

## Features

*   **Project Management:**
    *   Create, retrieve, update (progress), and delete projects.
    *   Each project includes title, description, progress, start/end dates, and a designated responsible person.
    *   Automatic calculation of "retard percent" based on current progress versus expected progress.
*   **Pole (Team/Department) Management:**
    *   Create, retrieve, update, and delete poles.
    *   Poles are associated with a responsible person (element pole) and can have an image.
    *   Automatic calculation of pole progress based on the average progress of its associated tasks.
*   **Task Management:**
    *   Create, retrieve, update, and delete tasks.
    *   Tasks include text, assigned resources (personnel), start date, progress, duration, parent task (for hierarchy), and associated pole.
    *   Tasks are linked to a specific project.
    *   Scheduled job to update `dureeReelle` for overdue tasks.
*   **Personnel Management:**
    *   Create, retrieve, update, and delete personnel records.
    *   Each person has a name, role, assigned pole name, and an optional profile image.
    *   Supports image uploads for personnel avatars.
    *   Supports deleting multiple personnel records.
*   **Link Management (for Gantt Chart):**
    *   Create, retrieve, update, and delete links between tasks (dependencies for Gantt charts).
    *   Supports optimistic locking (`@Version` annotation on `Link` entity).
*   **Key Performance Indicator (KPI) Service:**
    *   Calculate and provide various project KPIs:
        *   Respect des délais (on-time completion rate per date).
        *   Daily project progress.
        *   Daily project productivity.
        *   Ecart de durée (schedule variance per date).
*   **Image Handling:**
    *   Endpoint for uploading images for personnel.
    *   Endpoint for serving uploaded images.
*   **Data Transfer Objects (DTOs):** Used for structured data exchange between client and server.
*   **Relational Database:** Uses JPA (Hibernate) for ORM with a relational database (e.g., MySQL, PostgreSQL - specific database depends on your `application.properties`).

## Tech Stack

*   **Framework:** Spring Boot (likely v3.x based on `jakarta.persistence`)
*   **Language:** Java (likely 17+ for modern Spring Boot)
*   **Data Persistence:** Spring Data JPA, Hibernate
*   **Database:** (Specify your database, e.g., MySQL, PostgreSQL, H2 - configured in `application.properties`)
*   **API:** Spring Web (REST controllers)
*   **File Handling:** `MultipartFile` for image uploads.
*   **Utilities:** Lombok (for boilerplate code reduction like getters, setters, constructors).
*   **Build Tool:** Maven or Gradle (check `pom.xml` or `build.gradle`)

## Prerequisites

*   Java Development Kit (JDK) (e.g., JDK 17 or newer)
*   Maven (v3.6.x or newer) or Gradle (v7.x or newer) - *Specify which one you are using*
*   A running instance of your chosen relational database (e.g., MySQL, PostgreSQL).
*   An image upload directory configured

## Configuration (`application.properties`)

Ensure your `src/main/resources/application.properties` (or `application.yml`) file is configured with the correct database connection details and server port.

**Example `application.properties` (for MySQL):**

```properties
# Server Port
server.port=8080

# Database Configuration (Example for MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?createDatabaseIfNotExist=true
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update # Or create-drop for dev, validate for prod
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect # Adjust for your DB

# File Upload Configuration (Important!)
# Consider making this path configurable via environment variables or application properties
# For example: app.upload-dir=/path/to/your/uploads
# And then reference it in PersonneController: @Value("${app.upload-dir}") private String uploadDir;
# Ensure this directory exists and is writable by the application.

# Spring MVC Multipart properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
