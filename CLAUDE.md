# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Scrumble is a collaborative daily scrum sharing service built with Spring Boot. It allows teams to share todo items and manage squad-based task management with real-time notifications.

## Common Commands

### Build and Test
```bash
# Build the project
./gradlew build

# Run tests only
./gradlew test

# Run a single test class
./gradlew test --tests "ClassName"

# Run a single test method
./gradlew test --tests "ClassName.methodName"

# Generate test coverage report with Jacoco
./gradlew testAndGenerateJacocoReport
```

### Development
```bash
# Clean build artifacts and generated QueryDSL classes
./gradlew clean

# Run the application locally
./gradlew bootRun
```

### Test Reports
- Jacoco coverage reports: `/build/reports/jacoco/test/html/index.html`
- Test results: `/build/reports/tests/test/index.html`

## Architecture Overview

### Domain-Driven Design Structure
The application follows DDD with 6 main bounded contexts:
- **auth**: OAuth authentication (Google)
- **member**: User management and profiles  
- **squadmember**: Team/squad management and membership
- **todo**: Individual and squad-based task management
- **notification**: Real-time SSE notifications
- **event**: Domain event management with async processing

### Event-Driven Architecture
- **Domain Events**: All business events are persisted and processed asynchronously
- **Event Scheduler**: Processes unpublished events every 5 seconds in batches of 100
- **Event Listeners**: Handle domain events with `@Async` and `@TransactionalEventListener`
- **Reliable Processing**: Events are marked published only after successful processing

### Layer Structure (per domain)
```
domain/
├── controller/    # REST API endpoints
├── domain/        # Business entities and logic
├── dto/           # Data transfer objects  
├── facade/        # Application service orchestration
├── repository/    # Data access (JPA + QueryDSL)
└── service/       # Domain services
```

## Key Business Flows

### Squad Member Invitation
1. `SquadMemberService.inviteSquadMember()` creates invitation with `INVITING` status
2. Event is stored in database via `EventRepository`
3. `EventScheduler` picks up event and publishes to Spring event bus
4. `InvitedSquadMemberListener` creates notification and sends SSE message
5. Member responds via `responseInvitation()` to accept/reject

### Real-time Notifications
- **SSE Implementation**: `SseNotificationSender` manages Server-Sent Events
- **Connection Management**: `SseEmitterRepository` stores active connections in-memory
- **Heartbeat**: Periodic ping messages maintain connections
- **Auto-cleanup**: Connections removed on timeout/disconnection

## Technical Stack

### Core Dependencies
- **Spring Boot 3.3.3** with Java 17
- **JPA + QueryDSL** for data access
- **Lombok** for boilerplate reduction
- **Gson** for JSON processing
- **Spring Cloud OpenFeign** for OAuth clients
- **Jasypt** for configuration encryption

### Database
- **H2** for local development
- **MySQL** for production
- **QueryDSL** generated classes in `src/main/generated/`

### Testing
- **JUnit 5 + Mockito** for unit tests
- **Jacoco** for coverage with 70% line coverage target
- **Integration tests** with `@IntegrationTestHelper`

## Key Patterns and Conventions

### Facade Pattern
Used for orchestrating cross-domain operations:
- `AuthFacadeService`: OAuth provider coordination
- `SquadMemberFacadeService`: Squad creation with leader assignment  
- `SquadToDoFacadeService`: Todo and SquadTodo lifecycle management

### Event Processing
- All domain events extend `DomainEvent` with `publishedAt` timestamp
- Events stored as JSON in `Event` entity with `eventType` discrimination
- `EventMapper` handles JSON-to-domain-event conversion
- Use `now()` when creating events (static import from `LocalDateTime`)

### Repository Pattern
- Custom repository interfaces extend base JPA repository
- QueryDSL implementations in `impl/` packages
- Q-classes auto-generated in `src/main/generated/`

### Exception Handling
- Domain-specific exceptions: `MemberException`, `SquadException`, etc.
- Global exception handler in `GlobalExceptionHandler`
- Use `ExpectedException` with `ErrorCode` enum for business exceptions

## Configuration Notes

### Profiles
- **local**: H2 database, CORS + OAuth config
- **dev**: MySQL database, CORS + OAuth config  
- **prod**: MySQL database, CORS + OAuth config

### External Configuration
- OAuth properties in `config/application-oauth.yml`
- CORS settings in `config/application-cors.yml`
- Use Jasypt for sensitive configuration encryption

## Development Notes

### QueryDSL Generated Classes
- Generated classes in `src/main/generated/` are gitignored
- Run `./gradlew build` to regenerate Q-classes after entity changes
- Clean task removes generated directory

### Async Processing
- Event listeners use `@Async` with custom thread pool
- Use `@TransactionalEventListener` for reliable event processing
- SSE heartbeats run on scheduled executor

### Testing Conventions
- Integration tests extend `IntegrationTestHelper`
- Use `@MockBean` for external dependencies in integration tests
- Event listener tests may need `SyncTaskExecutor` to avoid async issues
- Korean language is used in test method names and descriptions