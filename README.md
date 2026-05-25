# Spring Cloud Bus - Distributed Communication & Configuration Management

A comprehensive learning project demonstrating **Spring Cloud Bus** for building microservices with distributed communication, dynamic configuration management, and event-driven architecture using RabbitMQ.

## 📚 What is Spring Cloud Bus?

Spring Cloud Bus connects multiple microservice instances using a message broker (RabbitMQ/Kafka), enabling:
- **Dynamic Configuration Refresh** - Update application configs across all services instantly without restart
- **Distributed Event Broadcasting** - Publish custom events to all connected microservices
- **Service Coordination** - Propagate admin commands and state changes in real-time
- **Event-Driven Architecture** - Build reactive, loosely-coupled microservice systems

## 🏗️ Project Architecture

This learning project consists of 4 practical applications:

### **Project 1: Bus Foundation** 
- **Purpose**: Verify RabbitMQ connectivity and Spring Cloud Bus setup
- **Services**: 1 (port 8080)
- **Learning**: Basic bus initialization, message broker connection
- **Files**: `project-1-bus-foundation/`

### **Project 2: Config Server & Clients**
- **Purpose**: Centralized configuration management with dynamic refresh
- **Services**: 3 (Config Server on 8888, Client-1 on 8081, Client-2 on 8082)
- **Learning**: Config server setup, client properties binding, `/actuator/busrefresh` endpoint
- **Features**: Dynamic configuration updates propagated to all clients via bus
- **Files**: `project-2-config-server/`, `project-2-config-client-1/`, `project-2-config-client-2/`

### **Project 3: Custom Events Broadcasting** ⭐ 
- **Purpose**: Implement custom RemoteApplicationEvent for inter-service communication
- **Services**: 2 (Service A on 8091, Service B on 8092)
- **Learning**: Custom event definition, event publishing via ApplicationEventPublisher, event listening with `@EventListener`
- **Key Feature**: `@RemoteApplicationEventScan` annotation for proper event deserialization
- **Files**: `project-3-custom-events/shared/`, `project-3-custom-events/service-a/`, `project-3-custom-events/service-b/`

## 📋 Project Structure

```
spring-cloud-bus/
├── project-1-bus-foundation/
│   ├── pom.xml
│   ├── src/main/java/com/springbus/foundation/
│   │   └── BusController.java
│   └── src/main/resources/application.yml
│
├── project-2-config-server/
│   ├── pom.xml
│   ├── src/main/java/com/springbus/configserver/
│   │   └── ConfigServerApplication.java
│   ├── src/main/resources/application.yml
│   └── src/main/resources/config/
│       ├── config-client-1.yml
│       └── config-client-2.yml
│
├── project-2-config-client-1/
│   ├── pom.xml
│   └── src/main/resources/application.yml
│
├── project-2-config-client-2/
│   ├── pom.xml
│   └── src/main/resources/application.yml
│
├── project-2-config-server/
│   ├── pom.xml
│   └── src/main/resources/application.yml
│
├── project-3-custom-events/
│   ├── shared/
│   │   ├── pom.xml
│   │   └── src/main/java/com/springbus/shared/
│   │       ├── NotificationEvent.java (extends RemoteApplicationEvent)
│   │       └── BroadcastMessage.java
│   │
│   ├── service-a/
│   │   ├── pom.xml
│   │   ├── src/main/java/com/springbus/servicea/
│   │   │   ├── ServiceAApplication.java
│   │   │   ├── ServiceAController.java
│   │   │   ├── MessageBroadcaster.java
│   │   │   └── RabbitMQConfig.java
│   │   └── src/main/resources/application.yml
│   │
│   └── service-b/
│       ├── pom.xml
│       ├── src/main/java/com/springbus/serviceb/
│       │   ├── ServiceBApplication.java
│       │   ├── NotificationListener.java
│       │   ├── ServiceBController.java
│       │   └── RabbitMQConfig.java
│       └── src/main/resources/application.yml
│
├── scripts/
│   ├── start-rabbitmq.sh       # Start RabbitMQ in Docker
│   ├── stop-rabbitmq.sh        # Stop RabbitMQ
│   ├── run-project-2-client-1.sh
│   ├── run-project-2-client-2.sh
│   ├── run-project-2-config-server.sh
│   ├── run-project-3-service-a.sh
│   ├── run-project-3-service-b.sh
│   └── setup.sh
│
├── README.md                    # This file
├── QUICK_START.md              # Quick start guide
└── .gitignore
```

## 🛠️ Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Language runtime |
| **Spring Boot** | 3.3.1 | Framework |
| **Spring Cloud** | 2023.0.3 | Microservices patterns |
| **RabbitMQ** | 3.13+ | Message broker |
| **Maven** | 3.8+ | Build tool |
| **Lombok** | Latest | Code generation (annotations) |
| **SLF4J** | Via Spring Boot | Logging framework |

## 🔑 Key Implementation Patterns

### 1. Dynamic Configuration Refresh (Project 2)
```
Update Config File → POST /actuator/busrefresh → RabbitMQ → All Clients Refresh
```

### 2. Custom Event Broadcasting (Project 3)
```
Service A publishes NotificationEvent 
→ Spring Cloud Bus intercepts 
→ JSON serialization via RabbitMQ 
→ Service B deserializes via @RemoteApplicationEventScan 
→ @EventListener invoked
```

### 3. Message Flow
```
Application → ApplicationEventPublisher → Spring Cloud Bus → RabbitMQ 
→ Spring Cloud Bus → Remote Services → Event Listeners
```

## 📦 Dependencies Used

### Core
- `spring-boot-starter-web` - REST endpoints
- `spring-cloud-starter-bus-amqp` - Spring Cloud Bus + RabbitMQ
- `spring-boot-starter-actuator` - Management endpoints

### Config Management (Project 2)
- `spring-cloud-starter-config` - Config server client

### Code Generation
- `lombok` - Annotations for logging, getters, setters

## ✅ Prerequisites

Before running any project:

```bash
✅ Java 21 or higher
✅ Maven 3.8+
✅ Docker (for RabbitMQ)
✅ curl or Postman (for testing endpoints)
```

## 🚀 Running the Projects

### Start RabbitMQ (Required for all projects)
```bash
cd scripts
./start-rabbitmq.sh
```

Access RabbitMQ Management:
- URL: http://localhost:15672
- Username: `guest`
- Password: `guest`

### Option 1: Run Individual Projects Manually

**Project 1:**
```bash
cd project-1-bus-foundation
mvn spring-boot:run
```

**Project 2 - Config Server:**
```bash
cd project-2-config-server
mvn spring-boot:run
```

**Project 2 - Config Clients (separate terminals):**
```bash
cd project-2-config-client-1
mvn spring-boot:run

cd project-2-config-client-2
mvn spring-boot:run
```

**Project 3 - Service A:**
```bash
cd project-3-custom-events/service-a
mvn spring-boot:run
```

**Project 3 - Service B:**
```bash
cd project-3-custom-events/service-b
mvn spring-boot:run
```

### Option 2: Use Provided Scripts
```bash
./scripts/run-project-2-config-server.sh
./scripts/run-project-2-client-1.sh
./scripts/run-project-2-client-2.sh
./scripts/run-project-3-service-a.sh
./scripts/run-project-3-service-b.sh
```

## 📊 Testing Each Project

See **[QUICK_START.md](QUICK_START.md)** for detailed testing commands for each project.

## 🎓 Learning Path

1. **Start with Project 1** (10 min) - Understand basic connectivity
2. **Then Project 2** (20 min) - Master configuration management
3. **Finally Project 3** (15 min) - Implement custom events

**Total learning time: ~45 minutes** ⏱️

## 🔍 Debugging Tips

### View RabbitMQ Queues
```
http://localhost:15672/
```

### Check Application Logs
```bash
# All services log to console with SLF4J
# Look for "[SERVICE A]" or "[SERVICE B]" prefixes
```

### Enable Debug Logging
Update `application.yml`:
```yaml
logging:
  level:
    com.springbus: DEBUG
    org.springframework.cloud.bus: DEBUG
```

## 📖 Important Concepts

### RemoteApplicationEvent
- Custom events must extend `RemoteApplicationEvent`
- Spring Cloud Bus automatically serializes to JSON and sends via RabbitMQ
- Remote services receive via message broker deserialization

### @RemoteApplicationEventScan
- **Critical**: Must be added to receiving services
- Enables automatic deserialization of custom RemoteApplicationEvent classes
- Without it, custom events won't be received

### Jackson2JsonMessageConverter
- Configures JSON serialization for AMQP messages
- Bean configuration in `RabbitMQConfig.java`

### @EventListener
- Listens for both local and remote events (when using RemoteApplicationEvent)
- Spring automatically routes RabbitMQ messages to appropriate listeners
- Works seamlessly with Spring Cloud Bus

## 🐛 Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Events not received | Missing `@RemoteApplicationEventScan` | Add annotation to Application class |
| RabbitMQ connection failed | Docker not running | Run `./scripts/start-rabbitmq.sh` |
| Configuration not refreshing | Bus not enabled | Ensure `spring.cloud.bus.enabled=true` |
| Port already in use | Another service using same port | Kill existing process or change port in `application.yml` |

## 📞 Support

For detailed setup and testing, refer to:
- **[QUICK_START.md](QUICK_START.md)** - Quick reference guide
- Individual `application.yml` files - Service configuration
- Source code comments - Implementation details

---

**Happy Learning! 🎉** Build distributed, event-driven microservices with Spring Cloud Bus!
