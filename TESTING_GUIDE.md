# 🧪 Spring Cloud Bus - API Testing Reference

Quick reference for all endpoints and testing commands.

---

## 🚀 Quick Test

```bash
# Terminal 1: Start RabbitMQ
./scripts/start-rabbitmq.sh

# Terminal 2: Start Project 1
cd project-1-bus-foundation && mvn spring-boot:run

# Terminal 3: Test
curl http://localhost:8080/health
```

---

## 📍 Project 1: Bus Foundation (Port 8080)

### Health & Info
```bash
# Check if app is running
curl http://localhost:8080/health
# Response: "Bus Foundation App is running!"

# Get app info
curl http://localhost:8080/info
# Response: "Project 1: Bus Foundation & RabbitMQ Setup"
```

### Actuator Endpoints
```bash
# List all actuator endpoints
curl http://localhost:8080/actuator

# Trigger bus refresh (should return 204 No Content)
curl -X POST http://localhost:8080/actuator/busrefresh
# Response: (empty, status 204)

# View health details
curl http://localhost:8080/actuator/health
# Response: Shows app status, components health
```

### Testing Flow
```bash
# 1. Verify app started
curl http://localhost:8080/health

# 2. Check RabbitMQ connection (look in logs: "Creating new connection")

# 3. Test actuator is enabled
curl http://localhost:8080/actuator

# 4. Trigger refresh via bus
curl -X POST http://localhost:8080/actuator/busrefresh

# 5. Check RabbitMQ dashboard
# http://localhost:15672 (guest/guest)
```

---

## 📍 Project 2: Config Server + Clients

### Config Server (Port 8888)

```bash
# Get configuration for config-client-1
curl http://localhost:8888/config-client-1/default
# Response: {"propertySources":[{"name":"classpath:/config/config-client-1.yml","source":{"app.message":"...","app.version":"..."}}]}

# Get configuration for config-client-2
curl http://localhost:8888/config-client-2/default
# Response: {"propertySources":[{"name":"classpath:/config/config-client-2.yml","source":{"app.message":"...","app.version":"..."}}]}
```

### Config Client 1 (Port 8081)

```bash
# Get current configuration (returns only config properties as ConfigResponse DTO)
curl http://localhost:8081/config
# Response: {"message":"Hello from Config Server - Client 1","version":"1.0.0"}

# Health check
curl http://localhost:8081/health

# List actuator endpoints
curl http://localhost:8081/actuator
```

**Note:** The `/config` endpoint returns a `ConfigResponse` object with only the properties loaded from the Config Server (`message` and `version`). This is a custom endpoint, not a standard Spring Cloud Config endpoint.

### Config Client 2 (Port 8082)

```bash
# Get current configuration (returns only config properties as ConfigResponse DTO)
curl http://localhost:8082/config
# Response: {"message":"Hello from Config Server - Client 2","version":"1.0.0"}

# Health check
curl http://localhost:8082/health
```

**Note:** The `/config` endpoint returns a `ConfigResponse` object with only the properties loaded from the Config Server (`message` and `version`).

### Update Configuration Across All Clients

```bash
# Step 1: Edit config file for Client 1
nano project-2-config-server/src/main/resources/config/config-client-1.yml

# Change:
# app:
#   message: "Hello - Updated via Bus!"
#   version: "2.0.0"

# Step 2: Edit config file for Client 2
nano project-2-config-server/src/main/resources/config/config-client-2.yml

# Change:
# app:
#   message: "Hello - Updated via Bus!"
#   version: "2.0.0"

# Step 3: Refresh all clients via bus (call on ANY client)
curl -X POST http://localhost:8081/actuator/busrefresh
# Response: 204 No Content

# Step 4: Verify both clients updated
curl http://localhost:8081/config
# Response: {"message":"Hello - Updated via Bus!","version":"2.0.0"}

curl http://localhost:8082/config
# Response: {"message":"Hello - Updated via Bus!","version":"2.0.0"}
```

### Testing Flow - Configuration Refresh

```bash
# Terminal 1: Start Config Server
cd project-2-config-server && mvn spring-boot:run

# Terminal 2: Start Client 1
cd project-2-config-client-1 && mvn spring-boot:run

# Terminal 3: Start Client 2
cd project-2-config-client-2 && mvn spring-boot:run

# Terminal 4: Test
# 1. Get initial config from both
curl http://localhost:8081/config
# Response: {"message":"Hello from Config Server - Client 1","version":"1.0.0"}

curl http://localhost:8082/config
# Response: {"message":"Hello from Config Server - Client 2","version":"1.0.0"}

# 2. Update config files on server
nano project-2-config-server/src/main/resources/config/config-client-1.yml
# Change version to 2.0.0 and message

nano project-2-config-server/src/main/resources/config/config-client-2.yml
# Change version to 2.0.0 and message

# 3. Trigger refresh via bus
curl -X POST http://localhost:8081/actuator/busrefresh

# 4. Verify both updated immediately!
curl http://localhost:8081/config
# Response: {"message":"...updated...","version":"2.0.0"}

curl http://localhost:8082/config
# Response: {"message":"...updated...","version":"2.0.0"}

# 5. Check logs - should see refresh messages
# Look for: "Refreshing properties..." in client logs
```

---

## 📍 Project 3: Custom Events

### Service A - Publisher (Port 8091)

```bash
# Publish a notification event
curl -X POST "http://localhost:8091/notify?message=Hello+from+Service+A"
# Response: "Notification sent: Hello from Service A"

# Publish another event
curl -X POST "http://localhost:8091/notify?message=Spring+Cloud+Bus+is+awesome"
# Response: "Notification sent: Spring Cloud Bus is awesome"

# Health check
curl http://localhost:8091/health
# Response: "Service A (Publisher) is running!"

# Get service info
curl http://localhost:8091/info
# Response: "Project 3: Service A - Event Publisher"
```

### Service B - Listener (Port 8092)

```bash
# Health check
curl http://localhost:8092/health
# Response: "Service B (Listener) is running!"

# Get service info
curl http://localhost:8092/info
# Response: "Project 3: Service B - Event Listener (check logs for received events)"

# Note: Service B doesn't have a dedicated endpoint for events
# Check the application logs to see received events:
# [SERVICE B] Received NotificationEvent: ...
# [SERVICE B] Message: ...
# [SERVICE B] From: service-a
# [SERVICE B] Event Time: ...
```

### Testing Flow - Event Broadcasting

```bash
# Terminal 1: Start RabbitMQ
./scripts/start-rabbitmq.sh

# Terminal 2: Start Service B (Listener)
cd project-3-custom-events/service-b && mvn spring-boot:run
# Look for: "Started ServiceBApplication"

# Terminal 3: Start Service A (Publisher)
cd project-3-custom-events/service-a && mvn spring-boot:run
# Look for: "Started ServiceAApplication"

# Terminal 4: Send an event
curl -X POST "http://localhost:8091/notify?message=Test+message"

# Check Terminal 2 (Service B) logs - should see:
# [SERVICE B] Received NotificationEvent: NotificationEvent{message='Test message'...
# [SERVICE B] Message: Test message
# [SERVICE B] From: service-a
# [SERVICE B] Event Time: 1234567890

# Send more events
curl -X POST "http://localhost:8091/notify?message=Another+test"
curl -X POST "http://localhost:8091/notify?message=Spring+Cloud+Bus+works!"

# Each should appear in Service B logs
```

---

## 🐰 RabbitMQ Dashboard

**URL:** http://localhost:15672  
**Username:** guest  
**Password:** guest

### What to Check

1. **Connections Tab**
   - Should see 2-3 connections (one per service)
   - Connection states: `running`

2. **Channels Tab**
   - Should see channels per connection
   - Message flow visible during operations

3. **Exchanges Tab**
   - Look for `springCloudBus` exchange
   - Type: `topic`
   - Status: `open`

4. **Queues Tab**
   - See queues for each service
   - Messages should flow through

---

## 📊 Complete Test Scenario

Run this full scenario to test all projects:

```bash
# ===== PHASE 1: START INFRASTRUCTURE =====
./scripts/start-rabbitmq.sh

# ===== PHASE 2: START ALL SERVICES =====
# Terminal 1: Project 1
cd project-1-bus-foundation && mvn spring-boot:run

# Terminal 2: Config Server
cd project-2-config-server && mvn spring-boot:run

# Terminal 3: Config Client 1
cd project-2-config-client-1 && mvn spring-boot:run

# Terminal 4: Config Client 2
cd project-2-config-client-2 && mvn spring-boot:run

# Terminal 5: Service A
cd project-3-custom-events/service-a && mvn spring-boot:run

# Terminal 6: Service B
cd project-3-custom-events/service-b && mvn spring-boot:run

# ===== PHASE 3: TEST PROJECT 1 =====
curl http://localhost:8080/health
curl -X POST http://localhost:8080/actuator/busrefresh

# ===== PHASE 4: TEST PROJECT 2 =====
# Initial state
curl http://localhost:8081/config
curl http://localhost:8082/config

# Update config (edit config-client-1.yml and config-client-2.yml in config-server)
# Then refresh
curl -X POST http://localhost:8081/actuator/busrefresh

# Verify update
curl http://localhost:8081/config
curl http://localhost:8082/config

# ===== PHASE 5: TEST PROJECT 3 =====
# Send event from Service A
curl -X POST "http://localhost:8091/notify?message=Hello+World"

# Check Service B logs for received event

# ===== PHASE 6: VERIFY RABBITMQ =====
# Visit http://localhost:15672
# Check:
# - Connections (should see 6)
# - Channels
# - Exchanges
# - Queues
```

---

## 🔧 Debugging Commands

### Check if services are running
```bash
lsof -i :8080   # Project 1
lsof -i :8888   # Config Server
lsof -i :8081   # Config Client 1
lsof -i :8082   # Config Client 2
lsof -i :8091   # Service A
lsof -i :8092   # Service B
lsof -i :5672   # RabbitMQ
```

### Kill a service
```bash
pkill -f "port 8080"
pkill -f "spring-boot"  # Kills all Spring apps
```

### Check RabbitMQ
```bash
docker ps                      # See if container running
docker logs rabbitmq          # View logs
docker exec rabbitmq rabbitmqctl status  # Check status
```

### Rebuild a project
```bash
cd project-name
mvn clean install
mvn spring-boot:run
```

---

## 📈 Expected Responses

### Project 1
| Endpoint | Method | Expected Status |
|----------|--------|-----------------|
| `/health` | GET | 200 |
| `/info` | GET | 200 |
| `/actuator` | GET | 200 |
| `/actuator/busrefresh` | POST | 204 |

### Project 2
| Endpoint | Method | Expected Status |
|----------|--------|-----------------|
| `/config` | GET | 200 |
| `/health` | GET | 200 |
| `/actuator/busrefresh` | POST | 204 |

### Project 3
| Endpoint | Method | Expected Status |
|----------|--------|-----------------|
| Service A `/notify` | POST | 200 |
| Service A `/health` | GET | 200 |
| Service B `/health` | GET | 200 |

---

## ⏱️ Expected Timing

| Operation | Typical Time |
|-----------|------|
| RabbitMQ startup | 3-5 seconds |
| Spring Boot app startup | 5-10 seconds |
| Config refresh propagation | 1-2 seconds |
| Event delivery | <500 milliseconds |

---

## 🆘 If Something Doesn't Work

### No Response from Endpoints
- Check if service is running: `curl http://localhost:PORT/health`
- Check port availability: `lsof -i :PORT`
- Check logs in terminal where service runs

### RabbitMQ Connection Error
- Start RabbitMQ: `./scripts/start-rabbitmq.sh`
- Check Docker: `docker ps`
- Check logs: `docker logs rabbitmq`

### Config Not Updating
- Verify Config Server is running on 8888
- Verify clients can reach: `curl http://localhost:8888/myapp/default`
- Check application.yml has correct config server URL

### Events Not Received
- RabbitMQ dashboard shows connections?
- Both services connected to same RabbitMQ?
- Check service logs for error messages

---

## 📝 Quick Copy-Paste Commands

```bash
# Start everything quick
./scripts/start-rabbitmq.sh &
cd project-1-bus-foundation && mvn spring-boot:run &
cd project-2-config-server && mvn spring-boot:run &
cd project-2-config-client-1 && mvn spring-boot:run &

# Test Project 1
curl http://localhost:8080/health

# Test Project 2 - Before
curl http://localhost:8081/config
curl http://localhost:8082/config

# Test Project 2 - Edit config
nano ./project-2-config-server/src/main/resources/config/config-client-1.yml
nano ./project-2-config-server/src/main/resources/config/config-client-2.yml

# Test Project 2 - Refresh
curl -X POST http://localhost:8081/actuator/busrefresh

# Test Project 2 - After
curl http://localhost:8081/config
curl http://localhost:8082/config

# Test Project 3
curl -X POST "http://localhost:8091/notify?message=Test"

# Stop RabbitMQ
./scripts/stop-rabbitmq.sh
```

---

## ✅ Test Checklist

- [ ] RabbitMQ running and dashboard accessible
- [ ] Project 1 starts and responds to health
- [ ] Config Server serves configuration
- [ ] Both config clients can fetch config
- [ ] Config refresh works (both clients update)
- [ ] Service A can send events
- [ ] Service B receives events
- [ ] All logs show no errors
- [ ] RabbitMQ dashboard shows connections
- [ ] All services on correct ports

---

**Ready to test? Start with: `./scripts/start-rabbitmq.sh`**
