# ⚡ Quick Start Guide - Spring Cloud Bus

Complete step-by-step guide to run and test all projects.

---

## 🚀 Get Started in 5 Minutes

### Step 1: Start RabbitMQ (1 min)

```bash
cd spring-cloud-bus
./scripts/start-rabbitmq.sh
```

**Expected Output:**
```
=== Starting RabbitMQ ===
Starting RabbitMQ container...
[container_id]
```

**Verify RabbitMQ is running:**
```bash
# Check if container is running
docker ps | grep rabbitmq

# Access Management Dashboard
# URL: http://localhost:15672 (guest/guest)
```

---

## 📋 Project 1: Bus Foundation (10 minutes)

### Run Project 1

```bash
cd project-1-bus-foundation
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
...
2026-05-25 10:30:00 - c.s.foundation.BusController - ...
Started ServiceApplication in X seconds
```

### Test Project 1

**Open another terminal:**

```bash
# Test health endpoint
curl http://localhost:8080/health
# Expected: "Bus Foundation App is running!"

# Test info endpoint
curl http://localhost:8080/info
# Expected: "Project 1: Bus Foundation & RabbitMQ Setup"

# Test bus refresh (triggers bus message)
curl -X POST http://localhost:8080/actuator/busrefresh
# Expected: {}

# Check logs in the first terminal - you should see Spring Cloud Bus messages
```

✅ **Project 1 Complete!** You've verified RabbitMQ connectivity.

---

## 📋 Project 2: Config Server & Clients (20 minutes)

### Terminal 1: Config Server

```bash
cd project-2-config-server
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started ConfigServerApplication in X seconds
Embedded server started on port(s): 8888
```

### Terminal 2: Config Client 1

```bash
cd project-2-config-client-1
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started ConfigClientApplication in X seconds
Embedded server started on port(s): 8081
config: Fetching config from server...
```

### Terminal 3: Config Client 2

```bash
cd project-2-config-client-2
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started ConfigClientApplication in X seconds
Embedded server started on port(s): 8082
```

### Test Project 2

**Open another terminal (Terminal 4):**

```bash
# Get current config from Client 1
curl http://localhost:8081/config
# Expected: {"message":"Config from server","version":"1.0"}

# Get current config from Client 2
curl http://localhost:8082/config
# Expected: {"message":"Config from server","version":"1.0"}

# Health checks
curl http://localhost:8888/health   # Config Server
curl http://localhost:8081/health   # Client 1
curl http://localhost:8082/health   # Client 2
```

### Update Configuration

**Edit the config file for Client 1:**

```bash
# Open file
nano project-2-config-server/src/main/resources/config/config-client-1.yml

# Change the message
message: "Updated config - Testing Spring Cloud Bus!"

# Save and exit (Ctrl+O, Enter, Ctrl+X)
```

### Refresh All Clients via Bus

```bash
# Send busrefresh to Client 1 (message will broadcast to all via bus)
curl -X POST http://localhost:8081/actuator/busrefresh
# Expected: {}

# Verify the config was updated on both clients
curl http://localhost:8081/config
# Expected: {"message":"Updated config - Testing Spring Cloud Bus!","version":"2.0"}

curl http://localhost:8082/config
# Expected: {"message":"Updated config - Testing Spring Cloud Bus!","version":"2.0"}
```

**Watch logs in Terminal 2 & 3** - You'll see refresh messages from Spring Cloud Bus!

✅ **Project 2 Complete!** Dynamic configuration refresh works across all services.

---

## 📋 Project 3: Custom Events Broadcasting (15 minutes) ⭐

### Terminal 1: Service A (Publisher)

```bash
cd project-3-custom-events/service-a
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started ServiceAApplication in X seconds
Embedded server started on port(s): 8091
```

### Terminal 2: Service B (Listener)

```bash
cd project-3-custom-events/service-b
mvn clean install
mvn spring-boot:run
```

**Expected Output:**
```
Started ServiceBApplication in X seconds
Embedded server started on port(s): 8092
```

### Test Project 3

**Open another terminal (Terminal 3):**

```bash
# Check health of both services
curl http://localhost:8091/health
# Expected: "Service A (Publisher) is running!"

curl http://localhost:8092/health
# Expected: "Service B (Listener) is running!"

# Send notification from Service A to Service B
curl -X POST "http://localhost:8091/notify?message=Hello%20from%20Service%20A%21"
# Expected: "Notification sent: Hello from Service A!"
```

### Verify Event Reception

**Watch Terminal 2 (Service B logs):**

You should see:
```
========================================
[SERVICE B] Received NotificationEvent via Spring Cloud Bus!
[SERVICE B] Message: Hello from Service A!
[SERVICE B] From Service: [service-instance-id]
[SERVICE B] Destination: *
[SERVICE B] Event Timestamp: 1748328600000
[SERVICE B] Event ID: [event-id]
[SERVICE B] Event fully processed
========================================
```

### Send Multiple Events

Try sending different messages:

```bash
curl -X POST "http://localhost:8091/notify?message=Event%201"
curl -X POST "http://localhost:8091/notify?message=Event%202"
curl -X POST "http://localhost:8091/notify?message=Spring%20Cloud%20Bus%20is%20Amazing"
```

**Watch Service B receive each message in real-time!**

✅ **Project 3 Complete!** Custom events broadcast successfully via Spring Cloud Bus.

---

## 🔍 Complete Test Scenario (Run All Projects)

### Setup (Execute in this order)

**Terminal 1: Start RabbitMQ**
```bash
./scripts/start-rabbitmq.sh
```

**Terminal 2: Project 2 - Config Server**
```bash
cd project-2-config-server && mvn spring-boot:run
```

**Terminal 3: Project 2 - Config Client 1**
```bash
cd project-2-config-client-1 && mvn spring-boot:run
```

**Terminal 4: Project 2 - Config Client 2**
```bash
cd project-2-config-client-2 && mvn spring-boot:run
```

**Terminal 5: Project 3 - Service A**
```bash
cd project-3-custom-events/service-a && mvn spring-boot:run
```

**Terminal 6: Project 3 - Service B**
```bash
cd project-3-custom-events/service-b && mvn spring-boot:run
```

**Terminal 7: Run All Tests**
```bash
#!/bin/bash
echo "=== Testing Project 2 - Config Server ==="
curl http://localhost:8081/config

echo -e "\n=== Testing Project 3 - Event Broadcasting ==="
curl -X POST "http://localhost:8091/notify?message=Complete%20Test%21"

# Give Service B time to receive and log the event
sleep 2
echo "Check Terminal 6 logs for event reception"
```

---

## 🎯 Testing Endpoints Summary

| Project | Service | Port | Endpoint | Method | Purpose |
|---------|---------|------|----------|--------|---------|
| 1 | Bus Foundation | 8080 | `/health` | GET | Health check |
| 1 | Bus Foundation | 8080 | `/info` | GET | Service info |
| 1 | Bus Foundation | 8080 | `/actuator/busrefresh` | POST | Trigger bus refresh |
| 2 | Config Server | 8888 | `/health` | GET | Health check |
| 2 | Config Client 1 | 8081 | `/config` | GET | Get current config |
| 2 | Config Client 1 | 8081 | `/actuator/busrefresh` | POST | Refresh config via bus |
| 2 | Config Client 2 | 8082 | `/config` | GET | Get current config |
| 3 | Service A | 8091 | `/notify?message=TEXT` | POST | Send notification |
| 3 | Service A | 8091 | `/health` | GET | Health check |
| 3 | Service B | 8092 | `/health` | GET | Health check |

---

## 🐛 Troubleshooting

### Problem: "Connection refused" errors

**Solution:**
```bash
# Check if RabbitMQ is running
docker ps | grep rabbitmq

# If not running, start it
./scripts/start-rabbitmq.sh
```

### Problem: Events not being received in Service B

**Cause:** Missing `@RemoteApplicationEventScan` annotation

**Solution:** Check `ServiceBApplication.java` has:
```java
@RemoteApplicationEventScan(basePackageClasses = NotificationEvent.class)
```

### Problem: Port already in use

**Solution:** Kill existing process:
```bash
# Find process on port 8091 (for example)
lsof -i :8091

# Kill it
kill -9 [PID]
```

### Problem: Maven build fails

**Solution:**
```bash
# Clean and rebuild
mvn clean install -U

# Force download of dependencies
mvn clean dependency:resolve
```

---

## 📊 Expected Architecture

```
┌─────────────────────┐
│   RabbitMQ Server   │
│   (Docker)          │
│   :5672, :15672     │
└──────────┬──────────┘
           │
    ┌──────┴──────┬──────────┬──────────┐
    │             │          │          │
    ▼             ▼          ▼          ▼
┌────────┐  ┌──────────┐ ┌──────────┐ ┌──────────┐
│Project1│  │Config    │ │Service A │ │Service B │
│  :8080 │  │Server    │ │  :8091   │ │  :8092   │
└────────┘  │  :8888   │ └────┬─────┘ └────┬─────┘
            │          │      │            │
            ├──────────┤  ┌────▼────┐      │
            │          │  │Events   │      │
            │ Clients  │  │via Bus  ◄──────┘
            │ 1&2      │  │         │
            │ :8081,82 │  └─────────┘
            └──────────┘
```

---

## ✅ Verification Checklist

- [ ] RabbitMQ is running (`docker ps | grep rabbitmq`)
- [ ] Project 1 health endpoint responds
- [ ] Project 2 config server returns configuration
- [ ] Project 2 busrefresh updates config on both clients
- [ ] Project 3 Service A receives notify requests
- [ ] Project 3 Service B logs received events
- [ ] Events appear in real-time in Service B logs

---

## 📞 Quick Reference

### Stop All Services

```bash
# Stop RabbitMQ
./scripts/stop-rabbitmq.sh

# Kill all running Java processes (careful!)
pkill -f spring-boot:run
```

### Clean Build All Projects

```bash
# Clean all pom.xml dependencies
mvn clean -pl project-1-bus-foundation,project-2-config-server,project-2-config-client-1,project-2-config-client-2,project-3-custom-events/shared,project-3-custom-events/service-a,project-3-custom-events/service-b
```

### View RabbitMQ Status

```bash
# Management UI
open http://localhost:15672  # or visit in browser
# Username: guest
# Password: guest

# Check queues, connections, etc.
```

---

🎉 **You've now mastered Spring Cloud Bus!** Learn more in [README.md](README.md)
