# PropertiesAnywhere – Backend

> 🚧 **Work in Progress**

Spring Boot REST API backend for the **PropertiesAnywhere** Flutter application.

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA / Hibernate
* PostgreSQL
* Gradle

## Current Features

* User registration and login
* Property creation and management
* Search properties by city
* Property ownership
* PostgreSQL persistence
* User-to-user messaging
* Property-based conversations
* Read/unread messages
* Unread message count
* Mark conversations as read

## Architecture

```text
Flutter
   ↓
REST API
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
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

## Run Locally

Make sure PostgreSQL is running, then:

### Windows

```powershell
.\gradlew.bat bootRun
```

### Linux/macOS

```bash
./gradlew bootRun
```

Backend:

```text
http://localhost:8080
```

For the Android Emulator, the Flutter app connects using:

```text
http://10.0.2.2:8080
```

## Status

🚧 **Work in Progress**

The backend is actively being developed. APIs, database structure, security, and features may change as development continues.

More features and production deployment are planned.
