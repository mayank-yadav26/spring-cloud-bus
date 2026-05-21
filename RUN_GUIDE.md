# Spring Cloud Bus Learning Path - Complete Run Guide

## Overview
This guide provides step-by-step instructions to run all 4 projects for learning Spring Cloud Bus.

**Total Projects:** 4  
**Total Services:** 6 (Project 1: 1 service, Project 2: 3 services, Project 3: 2 services, Project 4: Optional)  
**Technology Stack:** Java 21, Spring Boot 3.3.1, Spring Cloud 2023.0.3, RabbitMQ (Docker), Maven

---

## Prerequisites Check

Verify all required tools are installed:

```bash
java -version          # Should show Java 21+
mvn -version           # Should show Maven 3.8+
docker --version       # Should show Docker 29+
```

✅ **All tools are already installed on your system!**

---

## Phase 0: Start RabbitMQ

RabbitMQ is the message broker that all services will connect to.

### Step 1: Start RabbitMQ in Docker

```bash
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management-alpine
```

**Verify RabbitMQ is running:**
- Access admin panel: http://localhost:15672
- Username: `guest`
- Password: `guest`
- You should see the RabbitMQ dashboard

### Step 2: Stop RabbitMQ (when done)

```bash
docker stop rabbitmq
docker rm rabbitmq
```

---

## Project 1: Bus Foundation & RabbitMQ Setup

**Goal:** Verify Spring Cloud Bus connectivity with RabbitMQ  
**Duration:** 30-45 minutes  
**Services:** 1 (runs on port 8080)

### Step 1: Navigate to Project 1

```bash
cd ./project-1-bus-foundation
```

### Step 2: Build the project

```bash
mvn clean install
```

Expected output: `BUILD SUCCESS`

### Step 3: Run the application

```bash
mvn spring-boot:run
```

Expected output:
```
Starting Application on... with PID...
Started Application in X seconds
```

### Step 4: Verify the application is running

In a **new terminal**, test the endpoints:

```bash
# Test health endpoint
curl http://localhost:8080/health

# Test info endpoint
curl http://localhost:8080/info

# Test actuator endpoints
curl http://localhost:8080/actuator

# Trigger bus refresh (should return 204 No Content)
curl -X POST http://localhost:8080/actuator/busrefresh
```

### Step 5: Check RabbitMQ dashboard

Visit http://localhost:15672 and verify:
- Under "Connections" tab, you should see a connection from the app
- Under "Channels" tab, you should see at least 1 channel

### Step 6: Verify bus connectivity in logs

Look for these log messages:
```
Creating new connection
Connected to RabbitMQ
Successfully declared queue...
```

### Step 7: Stop the application

Press `Ctrl+C` in the terminal running the app

---

## Project 2: Configuration Server + Bus Clients

**Goal:** Learn configuration refresh across multiple services using Spring Cloud Bus  
**Duration:** 1-1.5 hours  
**Services:** 3 (Config Server: 8888, Client 1: 8081, Client 2: 8082)

### Step 1: Build the shared components

```bash
cd ./project-2-config-server
mvn clean install

cd ../project-2-config-client-1
mvn clean install

cd ../project-2-config-client-2
mvn clean install
```

### Step 2: Start Config Server (Terminal 1)

```bash
cd ./project-2-config-server
mvn spring-boot:run
```

Expected output:
```
Starting ConfigServerApplication...
Serving configuration from: classpath:/config/
Started ConfigServerApplication
```

### Step 3: Start Config Client 1 (Terminal 2)

```bash
cd ./project-2-config-client-1
mvn spring-boot:run
```

Expected output:
```
Starting ConfigClientApplication...
Fetching config from server at: http://localhost:8888
Started ConfigClientApplication in X seconds
```

### Step 4: Start Config Client 2 (Terminal 3)

```bash
cd ./project-2-config-client-2
mvn spring-boot:run
```

### Step 5: Verify initial configuration

In a **new terminal**, test each client:

```bash
# Get config from Client 1
curl http://localhost:8081/config
# Output: {"message":"Hello from Config Server - Client 1","version":"1.0.0"}

# Get config from Client 2
curl http://localhost:8082/config
# Output: {"message":"Hello from Config Server - Client 1","version":"1.0.0"}
```

Both clients should show the **same configuration**.

### Step 6: Update configuration on Config Server

Edit the config files for both clients:

```bash
# Edit config for Client 1
nano ./project-2-config-server/src/main/resources/config/config-client-1.yml

# Edit config for Client 2
nano ./project-2-config-server/src/main/resources/config/config-client-2.yml
```

Change in config-client-1.yml:
```yaml
app:
  message: Hello from Config Server - Client 1
  version: 1.0.0
```

Change in config-client-2.yml:
```yaml
app:
  message: Hello from Config Server - Client 2
  version: 1.0.0
```

To:
```yaml
app:
  message: Hello - Configuration Updated via Bus!
  version: 2.0.0
```

Save and close (Ctrl+X, Y, Enter)

### Step 7: Refresh configuration across all clients

```bash
# Trigger bus refresh (call on any client)
curl -X POST http://localhost:8081/actuator/busrefresh
```

Expected: `204 No Content` response

### Step 8: Verify both clients updated immediately

```bash
# Check Client 1
curl http://localhost:8081/config
# Output: {"message":"Hello - Configuration Updated via Bus!","version":"2.0.0"}

# Check Client 2
curl http://localhost:8082/config
# Output: {"message":"Hello - Configuration Updated via Bus!","version":"2.0.0"}
```

✅ **KEY LEARNING:** Both clients updated **without restarting** - configuration changed dynamically via Spring Cloud Bus!

### Step 9: Check the logs

Look at Terminal 2 and Terminal 3 logs for:
```
Refreshing properties
Environment changed, reloading bean definitions
```

### Step 10: Stop all services

Press `Ctrl+C` in each terminal

---

## Project 3: Custom Remote Events

**Goal:** Broadcast custom events between microservices  
**Duration:** 1 hour  
**Services:** 2 (Service A: 8091, Service B: 8092) + 1 shared module

### Step 1: Build the shared module

```bash
cd ./project-3-custom-events/shared
mvn clean install
```

Expected: `BUILD SUCCESS`

### Step 2: Build Service A and Service B

```bash
cd ../service-a
mvn clean install

cd ../service-b
mvn clean install
```

### Step 3: Start Service B (Listener) (Terminal 1)

```bash
cd ./project-3-custom-events/service-b
mvn spring-boot:run
```

Expected output:
```
Starting ServiceBApplication...
Started ServiceBApplication in X seconds
```

### Step 4: Start Service A (Publisher) (Terminal 2)

```bash
cd ./project-3-custom-events/service-a
mvn spring-boot:run
```

### Step 5: Verify both services are running

In a **new terminal**:

```bash
# Check Service A health
curl http://localhost:8091/health

# Check Service B health
curl http://localhost:8092/health
```

### Step 6: Send a notification from Service A

```bash
curl -X POST "http://localhost:8091/notify?message=Hello from Service A"
```

Expected response: `"Notification sent: Hello from Service A"`

### Step 7: Verify Service B received the event

Look at **Terminal 1** (Service B) logs. You should see:

```
[SERVICE B] Received NotificationEvent: NotificationEvent{message='Hello from Service A'...
[SERVICE B] Message: Hello from Service A
[SERVICE B] From: service-a
[SERVICE B] Timestamp: 1716234567890
```

✅ **KEY LEARNING:** Event published from Service A was received and processed by Service B via the message bus!

### Step 8: Send another message

```bash
curl -X POST "http://localhost:8091/notify?message=Spring Cloud Bus is awesome!"
```

Check Service B logs again to confirm it received the new event.

### Step 9: Stop all services

Press `Ctrl+C` in each terminal

---

## Project 4: Multi-Service Coordination (Optional)

This project extends the previous concepts by coordinating multiple services. It requires the same steps but with 3 services working together.

**Note:** For now, focus on Projects 1-3. Project 4 can be created after you understand the core patterns.

---

## Quick Reference: All Services & Ports

| Project | Service | Port | Purpose |
|---------|---------|------|---------|
| **1** | Bus Foundation | 8080 | Basic bus connectivity test |
| **2** | Config Server | 8888 | Central configuration repository |
| **2** | Config Client 1 | 8081 | Configuration consumer |
| **2** | Config Client 2 | 8082 | Configuration consumer |
| **3** | Service A | 8091 | Event publisher |
| **3** | Service B | 8092 | Event listener |

---

## Useful Endpoints

### Project 1 (Bus Foundation)
- `GET http://localhost:8080/health` - Health check
- `GET http://localhost:8080/info` - Service info
- `POST http://localhost:8080/actuator/busrefresh` - Trigger refresh

### Project 2 (Config Server & Clients)
- `GET http://localhost:8888/myapp/default` - Get config from server
- `GET http://localhost:8081/config` - Get refreshed config (Client 1)
- `GET http://localhost:8082/config` - Get refreshed config (Client 2)
- `POST http://localhost:8081/actuator/busrefresh` - Refresh all clients

### Project 3 (Custom Events)
- `POST http://localhost:8091/notify?message=<msg>` - Publish notification
- `GET http://localhost:8091/health` - Health check (Service A)
- `GET http://localhost:8092/health` - Health check (Service B)

---

## Troubleshooting

### Issue: "Connection refused" when starting services
**Cause:** RabbitMQ is not running  
**Solution:** Start RabbitMQ container (see Phase 0)

### Issue: Services compile but don't start
**Cause:** Port already in use  
**Solution:** Kill the process using that port:
```bash
lsof -i :8080  # Replace with the port number
kill -9 <PID>
```

### Issue: Config clients not fetching config
**Cause:** Config Server not running or network issue  
**Solution:** 
1. Verify Config Server is running on port 8888
2. Check if `http://localhost:8888/myapp/default` returns config
3. Verify both are on same network (docker or localhost)

### Issue: Events not being received by Service B
**Cause:** RabbitMQ not running or services using wrong broker settings  
**Solution:**
1. Verify RabbitMQ container is running
2. Check logs for connection errors: `Connection to RabbitMQ...`
3. Verify `rabbitmq.host: localhost` in `application.yml`

### Issue: Build fails with "dependency not found"
**Cause:** Maven needs to download dependencies  
**Solution:**
```bash
mvn clean install -U  # Force update of dependencies
```

---

## Next Steps After Completing All Projects

1. **Enhance Project 2:**
   - Add database configuration updates via bus
   - Add cache invalidation via bus events

2. **Enhance Project 3:**
   - Add error handling in event listeners
   - Add event filtering based on service name

3. **Advanced Learning:**
   - Learn about `@BusLocalEvent` for local-only events
   - Implement custom bus event types
   - Add security/authentication to bus messages
   - Explore Kafka as alternative to RabbitMQ

---

## Key Concepts Learned

| Concept | Where | Explanation |
|---------|-------|-------------|
| **Bus Connectivity** | Project 1 | Services can connect to RabbitMQ via Spring Cloud Bus |
| **Configuration Refresh** | Project 2 | Update configs on server → call `/busrefresh` → all clients update immediately |
| **RemoteApplicationEvent** | Project 3 | Custom events extending this class are broadcast across all services |
| **@RefreshScope** | Project 2 | Beans/properties with this can be refreshed without app restart |
| **Event Listener** | Project 3 | `@EventListener` method receives events published to bus |
| **Bus Endpoints** | Project 1, 2 | `/actuator/busrefresh`, `/actuator/busenv` trigger bus-wide actions |

---

## File Locations

```
./
├── project-1-bus-foundation/
│   ├── pom.xml
│   └── src/main/java/com/springbus/foundation/
├── project-2-config-server/
│   ├── pom.xml
│   ├── src/main/resources/config/config-client-1.yml
│   ├── src/main/resources/config/config-client-2.yml
│   └── src/main/java/com/springbus/configserver/
├── project-2-config-client-1/
│   ├── pom.xml
│   └── src/main/java/com/springbus/configclient/
├── project-2-config-client-2/
│   ├── pom.xml
│   └── src/main/java/com/springbus/configclient/
└── project-3-custom-events/
    ├── shared/
    │   ├── pom.xml
    │   └── src/main/java/com/springbus/shared/NotificationEvent.java
    ├── service-a/
    │   ├── pom.xml
    │   └── src/main/java/com/springbus/servicea/
    └── service-b/
        ├── pom.xml
        └── src/main/java/com/springbus/serviceb/
```

---

## Final Notes

- All projects use **Maven** for building
- All projects use **Spring Boot 3.3.1** with **Spring Cloud 2023.0.3**
- All services connect to **RabbitMQ** running on `localhost:5672`
- Config updates refresh **without application restart** via bus
- Custom events propagate across all connected services **in milliseconds**

**Happy Learning with Spring Cloud Bus! 🚀**
