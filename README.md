# PropertiesAnywhere – Backend

Spring Boot REST API backend for **PropertiesAnywhere**, an Android property and room rental application built with Flutter.

The backend supports user authentication, property listings, city-based searches, property ownership, and property-based messaging, with PostgreSQL for persistent data storage.

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **API:** Spring Web (REST APIs)
- **Persistence:** Spring Data JPA / Hibernate
- **Database:** PostgreSQL
- **Build Tool:** Gradle

## Features

- User registration and login
- Property creation and management
- Search properties by city
- Property ownership management
- PostgreSQL persistence
- User-to-user messaging
- Property-based conversations
- Read/unread message tracking
- Unread message counts
- Mark conversations as read

## Architecture

```text
Flutter Android Application
            |
            v
         REST API
            |
            v
        Controller
            |
            v
          Service
            |
            v
        Repository
            |
            v
        PostgreSQL
```

## Project Structure

```text
src/main/java/
└── com/propertiesanywhere/properties_anywhere_backend/
    ├── controller/
    ├── model/
    ├── repository/
    └── service/
```

## Prerequisites

- Java Development Kit (JDK)
- PostgreSQL
- Git

## Run Locally

### 1. Clone the repository

```bash
git clone <repository-url>
cd <repository-directory>
```

### 2. Configure PostgreSQL

Ensure PostgreSQL is running and configure the database connection in the application's configuration files or environment variables.

Provide the appropriate database URL, username, and password.

### 3. Start the backend

**Windows:**

```powershell
.\gradlew.bat bootRun
```

**Linux/macOS:**

```bash
./gradlew bootRun
```

The backend runs at:

```text
http://localhost:8080
```

## Android Emulator Integration

The Flutter Android application connects to the local backend using:

```text
http://10.0.2.2:8080
```

This address allows the Android Emulator to access services running on the host machine.

## Build the Project

**Windows:**

```powershell
.\gradlew.bat build
```

**Linux/macOS:**

```bash
./gradlew build
```

## Project Status

The backend implements the core REST API functionality for PropertiesAnywhere, including property management, PostgreSQL persistence, and property-based messaging.

It integrates with the Flutter Android application to support the platform's main user workflows.
