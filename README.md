# PropertiesAnywhere – Spring Boot Backend

PropertiesAnywhere is a Spring Boot REST API that provides the backend for the PropertiesAnywhere Flutter application.

The backend manages users and rental property listings and provides REST APIs for authentication, property discovery, property creation, property ownership and property updates.

## Current Features

* User registration
* User login
* User persistence
* Property creation
* Property search by city
* Retrieve individual properties
* Retrieve properties belonging to a user
* Associate properties with their owner
* Update existing properties
* PostgreSQL database integration
* JPA/Hibernate persistence
* REST API architecture

## Technology Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* REST APIs

## Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

### Controller

Handles HTTP requests and exposes REST endpoints.

### Service

Contains application and business logic.

### Repository

Provides database access using Spring Data JPA.

### Model

Contains the application's database entities.

## Project Structure

```text
src/
└── main/
    └── java/
        └── com/
            └── propertiesanywhere/
                └── properties_anywhere_backend/
                    ├── controller/
                    │   ├── UserController.java
                    │   └── PropertyController.java
                    │
                    ├── model/
                    │   ├── User.java
                    │   └── Property.java
                    │
                    ├── repository/
                    │   ├── UserRepository.java
                    │   └── PropertyRepository.java
                    │
                    └── service/
                        ├── UserService.java
                        └── PropertyService.java
```

Additional messaging-related classes will be added as the messaging feature is implemented.

## Database

The backend currently uses PostgreSQL.

The main entities are:

```text
User
  │
  │ 1
  │
  │ *
Property
```

A property belongs to a user through:

```text
Property.user
```

The database relationship uses:

```text
properties.user_id
```

## Property API

### Search Properties

```http
GET /api/properties?city={city}
```

Example:

```text
GET /api/properties?city=Siegen
```

Returns properties matching the requested city.

### Get Property

```http
GET /api/properties/{id}
```

Returns a specific property including its associated user information.

### Create Property

```http
POST /api/properties?userId={userId}
```

The property is associated with the specified user.

Example:

```text
POST /api/properties?userId=1
```

### Get User's Properties

```http
GET /api/properties/user/{userId}
```

Returns all properties created by the specified user.

### Update Property

```http
PUT /api/properties/{id}
```

Updates an existing property.

The request body contains the updated property information.

Example:

```json
{
  "title": "Updated Apartment",
  "city": "Siegen",
  "address": "Example Street 10",
  "rent": 450,
  "imageUrl": "https://example.com/image.jpg",
  "description": "Updated property description"
}
```

## Example Property

```json
{
  "id": 1,
  "title": "Modern WG Room",
  "city": "Siegen",
  "address": "Example Street 10",
  "rent": 450,
  "imageUrl": "https://example.com/image.jpg",
  "description": "A furnished room near the university.",
  "user": {
    "id": 1,
    "name": "Swamil",
    "email": "example@email.com"
  }
}
```

The `user` relationship allows the Flutter application to determine the owner of a property.

## Running the Backend

Make sure PostgreSQL is running and the database configuration is correctly configured.

From the backend project directory:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

## Flutter Android Emulator

The Android Emulator cannot use the development computer's `localhost` directly.

The Flutter application therefore uses:

```text
http://10.0.2.2:8080
```

to communicate with this backend.

## Current Development Status

### Completed

* Spring Boot project setup
* PostgreSQL integration
* User model
* User registration
* User login
* Property model
* Property ownership relationship
* Property creation API
* Property search API
* Property retrieval API
* User property API
* Property update API

### Currently Planned

* User-to-user messaging
* Conversation entity
* Message entity
* Conversation APIs
* Message APIs
* Chat functionality
* Conversation history
* Unread messages
* Owner/user authorization for property updates
* Production authentication using JWT
* Production deployment

## Development

The backend is currently configured for local development and testing with the Flutter Android application.

The messaging system will be added as the next major feature.
