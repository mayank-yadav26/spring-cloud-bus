# Spring Cloud Bus Learning Path

A comprehensive learning project for mastering **Spring Cloud Bus**, a distributed communication framework for microservices using message brokers RabbitMQ.

## 📚 What is Spring Cloud Bus?

Spring Cloud Bus connects multiple service instances using a lightweight message broker, enabling:
- **Dynamic Configuration Refresh** - Update configs across all services without restart
- **Distributed Events** - Broadcast custom events across microservices
- **Service Coordination** - Propagate admin commands/state changes instantly
- **Event-Driven Architecture** - Build reactive systems with loosely coupled services

## 🎯 Learning Path

This project includes 4 progressive projects designed for beginners:

| Project | Goal | Services | Duration |
|---------|------|----------|----------|
| **1. Bus Foundation** | Verify RabbitMQ connectivity | 1 | 30-45 min |
| **2. Config Server + Clients** | Master dynamic configuration refresh | 3 | 1-1.5 hrs |
| **3. Custom Events** | Broadcast events between services | 2 | 1 hour |
| **4. Multi-Service Coordination** | Integrate all patterns | 3 | 1-1.5 hrs (optional) |

## 🚀 Quick Start

### Prerequisites
- ✅ Java 21+ (already installed)
- ✅ Maven 3.8+ (already installed)
- ✅ Docker (already installed)
- ✅ curl (for testing)

### Run Project 1 (Bus Foundation) - 30 minutes

```bash
# 1. Start RabbitMQ
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine

# 2. Build and run Project 1
cd project-1-bus-foundation
mvn clean install
mvn spring-boot:run

# 3. Test (in another terminal)
curl http://localhost:8080/health
curl -X POST http://localhost:8080/actuator/busrefresh
```

### See Full Documentation

👉 **[RUN_GUIDE.md](RUN_GUIDE.md)** - Comprehensive step-by-step guide for all projects

### Plan Document

👉 **[.github/prompts/plan-springCloudBus.prompt.md](.github/prompts/plan-springCloudBus.prompt.md)** - Detailed learning plan

---

## 📂 Project Structure

```
spring-cloud-bus/
├── project-1-bus-foundation/           # Basic bus connectivity
│   ├── pom.xml
│   └── src/main/java/com/springbus/foundation/
│
├── project-2-config-server/            # Central config repository
│   ├── pom.xml
│   ├── src/main/resources/config/config-client-1.yml
│   ├── src/main/resources/config/config-client-2.yml
│   └── src/main/java/com/springbus/configserver/
│
├── project-2-config-client-1/          # Config consumer 1
├── project-2-config-client-2/          # Config consumer 2
│
├── project-3-custom-events/
│   ├── shared/                         # Shared event classes
│   ├── service-a/                      # Event publisher
│   └── service-b/                      # Event listener
│
├── RUN_GUIDE.md                        # Complete step-by-step guide
└── README.md                           # This file
```

---

## 🔑 Key Concepts

### 1. Dynamic Configuration Refresh (Project 2)
```
Update Config File → POST /actuator/busrefresh → All Services Refresh Immediately
```

### 2. Custom Event Broadcasting (Project 3)
```
Service A publishes NotificationEvent → Message Broker → Service B listens via @EventListener
```

### 3. Bus Connectivity (Project 1)
```
Service ↔ Spring Cloud Bus ↔ RabbitMQ ↔ Spring Cloud Bus ↔ Service
```

---

## 📝 Technology Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.3.1 |
| Spring Cloud | 2023.0.3 |
| RabbitMQ | 3.x (Docker) |
| Maven | 3.8+ |

---

## 🧪 Testing Commands

### Project 1 - Bus Foundation
```bash
curl http://localhost:8080/health
curl http://localhost:8080/info
curl -X POST http://localhost:8080/actuator/busrefresh
```

### Project 2 - Config Server & Clients
```bash
# Get current config
curl http://localhost:8081/config
curl http://localhost:8082/config

# After updating config file on server, refresh all clients
curl -X POST http://localhost:8081/actuator/busrefresh

# Verify both clients got updated
curl http://localhost:8081/config
curl http://localhost:8082/config
```

### Project 3 - Custom Events
```bash
# Send notification from Service A
curl -X POST "http://localhost:8091/notify?message=Test+message"

# Check Service B logs for received event
```

---

## 🔍 Next Steps

After completing all projects:

1. **Experiment with configuration:**
   - Add database URLs to config
   - Add feature flags that can be toggled via bus

2. **Explore advanced patterns:**
   - Learn about `@BusLocalEvent` (local-only events)
   - Implement custom event filtering
   - Add authentication to bus messages

3. **Switch message brokers:**
   - Replace RabbitMQ with Kafka
   - Understand broker abstraction via Spring Cloud Stream

4. **Production patterns:**
   - Error handling and resilience
   - Event monitoring and tracing
   - Deployment strategies

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| "Connection refused" | Start RabbitMQ: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine` |
| Port already in use | Kill process: `lsof -i :PORT` then `kill -9 PID` |
| Config not updating | Verify Config Server is running on port 8888 |
| Events not received | Check RabbitMQ dashboard for connections/channels |
| Build fails | Run `mvn clean install -U` to force dependency updates |

---

## 📖 Learning Resources

- [Spring Cloud Bus Documentation](https://docs.spring.io/spring-cloud-bus/docs/current/reference/html/)
- [Spring Cloud Config Documentation](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [Spring Cloud Concepts](https://spring.io/projects/spring-cloud)

---

## ✅ Verification Checklist

- [ ] Java 21+ installed
- [ ] Maven 3.8+ installed
- [ ] Docker installed
- [ ] Project 1 runs and connects to RabbitMQ
- [ ] Project 2 config clients refresh via `/busrefresh`
- [ ] Project 3 Service B receives events from Service A
- [ ] All 6 services communicate via bus
- [ ] No errors in logs or output

---

## 📞 Summary

This learning path teaches **Spring Cloud Bus** through hands-on projects:
- ✅ Understand microservice communication patterns
- ✅ Learn configuration management at scale
- ✅ Master event-driven architecture
- ✅ Build resilient distributed systems

**Start with Project 1 and follow the [RUN_GUIDE.md](RUN_GUIDE.md) for step-by-step instructions!**

Happy Learning! 🚀
