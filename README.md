# PropertiesAnywhere – Spring Boot Backend

**Work in Progress 🚧**

PropertiesAnywhere is a Spring Boot REST API that provides the backend for the PropertiesAnywhere Flutter application.

The backend manages users, rental property listings, and user-to-user messaging. It uses PostgreSQL for data persistence and exposes REST APIs consumed by the Flutter mobile application.

The project is currently under active development and is intended for local development, testing, and experimentation.

---

## Current Features

### User Management

* User registration
* User login
* User persistence
* User identification through user IDs

### Property Management

* Property creation
* Property search by city
* Retrieve individual properties
* Retrieve properties belonging to a user
* Associate properties with their owner
* Update existing properties
* PostgreSQL persistence for properties

### Messaging

* User-to-user messaging
* Messages associated with a property
* Send messages between users
* Retrieve messages for a property
* Retrieve a user's messages
* Retrieve conversations between two users
* Unread message tracking
* Unread message count
* Mark conversations as read
* Conversation-specific message history

The messaging system is currently functional and integrated with the Flutter frontend.

---

# Technology Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* PostgreSQL
* Gradle
* REST APIs

---

# Architecture

The backend follows a layered architecture:

```text
Flutter Application
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

### Controller

Controllers handle HTTP requests and expose REST endpoints to the Flutter application.

Examples:

```text
UserController
PropertyController
MessageController
```

### Service

Services contain the application's business and application logic.

Examples:

```text
UserService
PropertyService
MessageService
```

### Repository

Repositories provide database access through Spring Data JPA.

Examples:

```text
UserRepository
PropertyRepository
MessageRepository
```

### Model

The model layer contains the JPA entities used by the application.

Examples:

```text
User
Property
Message
```

---

# Project Structure

```text
src/
└── main/
    └── java/
        └── com/
            └── propertiesanywhere/
                └── properties_anywhere_backend/
                    │
                    ├── controller/
                    │   ├── UserController.java
                    │   ├── PropertyController.java
                    │   └── MessageController.java
                    │
                    ├── model/
                    │   ├── User.java
                    │   ├── Property.java
                    │   └── Message.java
                    │
                    ├── repository/
                    │   ├── UserRepository.java
                    │   ├── PropertyRepository.java
                    │   └── MessageRepository.java
                    │
                    └── service/
                        ├── UserService.java
                        ├── PropertyService.java
                        └── MessageService.java
```

---

# Database

PropertiesAnywhere currently uses **PostgreSQL** as its database.

The main entities are:

```text
User
 │
 ├───────────────┐
 │               │
 │ 1             │ 1
 │               │
 │ *             │ *
Property       Message
                 │
                 ├── sender → User
                 ├── receiver → User
                 └── property → Property
```

A property belongs to a user through:

```text
Property.user
```

Messages contain relationships to:

```text
Message.sender
Message.receiver
Message.property
```

The message table also contains:

```text
is_read
```

which is used to track whether the receiving user has read a message.

---

# Property API

## Search Properties

```http
GET /api/properties?city={city}
```

Example:

```text
GET /api/properties?city=Siegen
```

Returns properties matching the requested city.

---

## Get Property

```http
GET /api/properties/{id}
```

Returns a specific property.

---

## Create Property

```http
POST /api/properties?userId={userId}
```

The property is associated with the specified user.

Example:

```text
POST /api/properties?userId=1
```

---

## Get User's Properties

```http
GET /api/properties/user/{userId}
```

Returns properties created by the specified user.

---

## Update Property

```http
PUT /api/properties/{id}
```

Updates an existing property.

Example request body:

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

---

# Messaging API

The messaging system allows users to communicate with each other regarding a specific property.

Each message contains:

```text
Sender
Receiver
Property
Content
Sent timestamp
Read status
```

---

## Send Message

```http
POST /api/messages
```

Parameters:

```text
senderId
receiverId
propertyId
```

Request body:

```json
{
  "content": "Is this room still available?"
}
```

Example:

```text
POST /api/messages?senderId=1&receiverId=2&propertyId=5
```

A new message is created with:

```text
isRead = false
```

---

## Get Messages for a Property

```http
GET /api/messages/property/{propertyId}
```

Returns all messages associated with a property.

Messages are ordered from oldest to newest.

---

## Get Conversation

```http
GET /api/messages/conversation
```

Parameters:

```text
user1Id
user2Id
propertyId
```

Example:

```text
GET /api/messages/conversation?user1Id=1&user2Id=2&propertyId=5
```

This retrieves messages exchanged between two users regarding a specific property.

The conversation is limited to:

```text
User 1 ↔ User 2
        +
   Property
```

This prevents messages from unrelated properties or users from appearing in the conversation.

---

## Get User Messages

```http
GET /api/messages/user/{userId}
```

Returns messages where the specified user is either:

```text
sender
```

or

```text
receiver
```

The Flutter application uses this endpoint to construct the Messages screen.

---

## Get Unread Messages

```http
GET /api/messages/unread/{userId}
```

Returns messages that have not yet been read by the specified user.

---

## Get Unread Message Count

```http
GET /api/messages/unread/count/{userId}
```

Returns the number of unread messages for a user.

The Flutter application uses this information to display the notification count on the Messages button.

---

## Mark Conversation as Read

```http
PUT /api/messages/read
```

Parameters:

```text
receiverId
senderId
propertyId
```

Example:

```text
PUT /api/messages/read?receiverId=1&senderId=2&propertyId=5
```

The backend updates matching unread messages:

```text
is_read = false
```

to:

```text
is_read = true
```

This happens when the user opens the specific conversation.

The read operation is therefore **conversation-specific** rather than marking every message in the application as read.

---

# Message Read/Unread Logic

The messaging system uses the following logic:

```text
User A sends message to User B
             ↓
        is_read = false
             ↓
User B sees notification
             ↓
User B opens Messages
             ↓
Unread conversation remains visible
             ↓
User B opens the specific chat
             ↓
PUT /api/messages/read
             ↓
Matching messages become:
        is_read = true
             ↓
Notification disappears
```

This allows unread messages to remain highlighted until the user actually opens the relevant conversation.

---

# Example Message

A message returned by the API can contain information similar to:

```json
{
  "id": 1,
  "sender": {
    "id": 1,
    "name": "Swamil"
  },
  "receiver": {
    "id": 2,
    "name": "John"
  },
  "property": {
    "id": 5,
    "title": "Modern WG Room"
  },
  "content": "Is this room still available?",
  "sentAt": "2026-08-27T14:30:00",
  "isRead": false
}
```

---

# PostgreSQL

The backend uses PostgreSQL for persistent storage.

The `messages` table contains the read-status field:

```sql
ALTER TABLE messages
ADD COLUMN IF NOT EXISTS is_read
BOOLEAN NOT NULL DEFAULT FALSE;
```

This ensures newly created messages are unread by default.

The database configuration is stored in the Spring Boot application configuration.

---

# Running the Backend

Make sure PostgreSQL is running and the database configuration is correctly configured.

From the backend project directory, run:

### Windows PowerShell

```powershell
.\gradlew.bat bootRun
```

### Windows Command Prompt

```cmd
gradlew.bat bootRun
```

### Linux/macOS

```bash
./gradlew bootRun
```

The backend runs on:

```text
http://localhost:8080
```

---

# Flutter Android Emulator

When running the Flutter application using the Android Emulator, the emulator cannot access the development computer using:

```text
localhost
```

Instead, the Flutter application communicates with the Spring Boot backend through:

```text
http://10.0.2.2:8080
```

Therefore, the current Flutter development configuration uses:

```text
10.0.2.2:8080
```

for API requests.

This configuration is intended for local development.

For production deployment, the Flutter application will need to use the URL of the deployed backend instead.

---

# Current Development Status

## Completed

* Spring Boot project setup
* Gradle build configuration
* PostgreSQL integration
* JPA/Hibernate integration
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
* Message model
* Message repository
* Message service
* Message controller
* Send message API
* Conversation API
* User message API
* Unread message API
* Unread message count
* Mark conversation as read
* Property-specific conversations
* Read/unread message tracking
* Flutter chat integration

---

# Work in Progress 🚧

PropertiesAnywhere is still an active development project.

The following areas are planned or being improved:

* Production deployment
* Production database configuration
* Secure authentication
* JWT-based authentication
* Password security improvements
* Authorization and access control
* Property ownership validation
* Better API validation
* Error handling
* API documentation
* Image storage and management
* User profiles
* Improved messaging experience
* Real-time messaging
* Push notifications
* Message timestamps and delivery status
* Pagination for messages and properties
* Search and filtering improvements
* Production security configuration
* CORS configuration for production
* Testing
* Automated tests
* Performance improvements

---

# Development Status

```text
🚧 WORK IN PROGRESS

Backend:
    ✅ User management
    ✅ Property management
    ✅ PostgreSQL
    ✅ Messaging
    ✅ Read/unread messages
    🚧 Authentication security
    🚧 Real-time messaging
    🚧 Production deployment
    🚧 Testing
    🚧 Additional features
```

This project is currently intended primarily for **development and local testing**.

The architecture and APIs may change as new features are implemented.

---

# Future Goal

The goal of PropertiesAnywhere is to provide a simple platform where users can:

```text
Create an account
       ↓
Browse properties
       ↓
View property details
       ↓
Contact the property owner
       ↓
Chat about the property
       ↓
Arrange a viewing / rental
```

The long-term goal is to evolve the project into a more complete property and shared-housing platform.

---

# License

This project is currently a personal work-in-progress project and is primarily intended for development, learning, and testing.
