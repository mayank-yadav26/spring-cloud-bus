# Plan: Spring Cloud Bus Learning Path (Beginner)

## TL;DR
Create 3-4 progressive projects using Spring Cloud Bus with RabbitMQ to understand distributed configuration management and event propagation. Start with local setup, then build: (1) basic bus connectivity test, (2) config server with bus-enabled clients, (3) custom event broadcasting, and optionally (4) multi-service coordination. All projects run locally with Docker-based RabbitMQ.

---

## Projects Overview

### Project 1: Bus Foundation & RabbitMQ Setup
**Goal:** Verify Spring Cloud Bus connectivity with message broker
- Single Spring Boot app with Bus dependency
- Manual message publishing and consumption
- Verify RabbitMQ connection and message flow
- **Estimated time:** 30-45 minutes

### Project 2: Configuration Server + Bus Clients
**Goal:** Learn configuration refresh across multiple services
- 1 Spring Cloud Config Server (serves config from filesystem)
- 2 Config Client apps with `@RefreshScope`
- Test `/actuator/busrefresh` endpoint to refresh all clients simultaneously
- **Estimated time:** 1-1.5 hours

### Project 3: Custom Remote Events
**Goal:** Broadcast custom events between services
- 2 microservices with custom `RemoteApplicationEvent` subclass
- Event publisher in Service A, listener in Service B
- Demonstrate service-to-service event propagation
- **Estimated time:** 1 hour

### Project 4 (Optional): Multi-Service Coordination
**Goal:** Integrate all patterns (only if time permits)
- 3 services coordinating via configuration + custom events
- Simulated "emergency shutdown" scenario broadcast via bus
- Real-world pattern demonstration
- **Estimated time:** 1-1.5 hours

---

## Steps (Grouped by Phases)

### Phase 1: Environment Setup
1. Install Docker and start RabbitMQ container
   - Command: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine`
   - Verify: Access RabbitMQ dashboard at http://localhost:15672 (guest/guest)
2. Verify Java 17+ and Maven installed
3. Create workspace folder structure: `spring-cloud-bus/project-{1,2,3,4}/`

### Phase 2: Project 1 - Bus Foundation
1. Create new Spring Boot 3.x Maven project with dependencies:
   - `spring-cloud-starter-bus-amqp`
   - `spring-boot-starter-actuator`
   - Spring Cloud version: 2023.0.0 or later
2. Configure `application.yml`:
   - Set RabbitMQ host/port
   - Enable bus endpoints (`busrefresh`, `busenv`)
   - Enable actuator web exposure
3. Create test controller with endpoint that logs received messages
4. Verify startup and check RabbitMQ exchange creation
5. Test with `curl` to trigger bus endpoints

### Phase 3: Project 2 - Config Server + Clients
1. Create Config Server (Spring Cloud Config Server):
   - Create `src/main/resources/config/` directory with sample YAML files
   - Configure `application.yml` to serve from filesystem
   - Start on port 8888
2. Create 2 Config Client apps (ports 8081, 8082):
   - Add `spring-cloud-starter-config` + `spring-cloud-starter-bus-amqp`
   - Add `bootstrap.yml` pointing to Config Server
   - Create controller with `@RefreshScope` bean to expose property values
3. Add test property file in Config Server: `myapp.properties` (e.g., `app.message=Hello`)
4. Start all 3 apps and verify:
   - Both clients read config from server
   - Update config file on server
   - Hit `POST /actuator/busrefresh` on either client
   - Verify both clients updated immediately without restart

### Phase 4: Project 3 - Custom Events
1. Create shared module/package with custom `RemoteApplicationEvent`:
   - Class: `NotificationEvent extends RemoteApplicationEvent`
   - Fields: `message`, `timestamp`
2. Create Service A (port 8091):
   - REST endpoint: `POST /notify?message={msg}`
   - Publishes `NotificationEvent` to bus
3. Create Service B (port 8092):
   - `@EventListener` method for `NotificationEvent`
   - Logs received message to console/file
4. Test:
   - POST message to Service A
   - Verify Service B receives and logs it
   - Reverse roles (trigger from B, listen in A)

### Phase 5 (Optional): Project 4 - Coordination
1. Create 3 services with roles: Coordinator, Worker-1, Worker-2
2. Coordinator publishes `ShutdownEvent` custom event
3. Workers subscribe and log graceful shutdown messages
4. Test mass event propagation

---

## Relevant Files & Patterns to Reference

- **Spring Cloud Bus dependency matrix**: https://spring.io/projects/spring-cloud (version compatibility)
- **Config Server bootstrap**: Use `bootstrap.yml` (not `application.yml`) for config client discovery
- **Custom event pattern**: Extend `RemoteApplicationEvent`, use `ApplicationEventPublisher.publishEvent()`
- **@RefreshScope**: Annotate beans/properties that need dynamic refresh
- **Actuator endpoint config**: `/actuator/busrefresh`, `/actuator/busenv` in `application.yml`

---

## Verification

### Project 1 Verification
1. RabbitMQ admin panel shows exchanges/queues created
2. App logs show successful connection: `Creating new connection`
3. Actuator endpoints respond: `curl http://localhost:8080/actuator/busrefresh` → 204 success

### Project 2 Verification
1. Config Server startup logs show: `Serving configuration from: ...`
2. Both client apps log on startup: `fetching config from server` → `property: value` matches
3. After `POST /actuator/busrefresh`:
   - Config Server file modified
   - Both clients log refresh event within 2 seconds
   - Property value changes without app restart
   - Call endpoint on either client (not just the one refreshed)

### Project 3 Verification
1. POST to Service A succeeds: `curl -X POST http://localhost:8091/notify?message="test"`
2. Service B logs: `NotificationEvent received: test` within 1-2 seconds
3. Verify via RabbitMQ dashboard: custom exchange route shows message flow

### Project 4 Verification (Optional)
1. Coordinator sends `ShutdownEvent`
2. All 3 services log graceful shutdown within 2 seconds

---

## Decisions & Scope
- **Use RabbitMQ**: AMQP chosen for traditional enterprise pattern learning
- **Local Docker**: RabbitMQ runs locally in Docker for easy cleanup
- **Surface-level**: Skipped authentication, encryption, advanced routing filters, error handling patterns—keep focus on core bus mechanics
- **Config Server uses filesystem**: Simpler than Git backend for initial learning
- **No Kubernetes/service discovery**: Use hardcoded URLs (localhost) to keep beginner-friendly

---

## Technology Stack Summary
- Java 17+
- Spring Boot 3.x
- Spring Cloud 2023.0.0+
- RabbitMQ 3.x (Docker)
- Maven 3.8+
