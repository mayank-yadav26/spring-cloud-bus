# Quick Start - Run All Services

**Prerequisite:** Java 21, Maven 3.9.15, Docker installed

## Option 1: Automated Startup (Recommended)

### Step 1: Build All Projects
```bash
cd .
bash scripts/setup.sh
```
✅ Builds all 7 Maven modules (takes ~20 seconds)

### Step 2: Start RabbitMQ
```bash
bash scripts/start-rabbitmq.sh
```
✅ Starts RabbitMQ Docker container

### Step 3: Start All Services (Open 6 Terminals)

**Terminal 1 - Project 1 (Port 8080):**
```bash
cd ./project-1-bus-foundation
mvn spring-boot:run
# OR: java -jar target/project-1-bus-foundation-1.0.0.jar
```

**Terminal 2 - Config Server (Port 8888):**
```bash
cd ./project-2-config-server
mvn spring-boot:run
# OR: java -jar target/project-2-config-server-1.0.0.jar
```

**Terminal 3 - Config Client 1 (Port 8081):**
```bash
cd ./project-2-config-client-1
mvn spring-boot:run
# OR: java -jar target/project-2-config-client-1-1.0.0.jar
```

**Terminal 4 - Config Client 2 (Port 8082):**
```bash
cd ./project-2-config-client-2
mvn spring-boot:run
# OR: java -jar target/project-2-config-client-2-1.0.0.jar
```

**Terminal 5 - Service A Publisher (Port 8091):**
```bash
cd ./project-3-custom-events/service-a
mvn spring-boot:run
# OR: java -jar target/project-3-service-a-1.0.0.jar
```

**Terminal 6 - Service B Listener (Port 8092):**
```bash
cd ./project-3-custom-events/service-b
mvn spring-boot:run
# OR: java -jar target/project-3-service-b-1.0.0.jar
```

---

## Option 2: Using Provided Scripts

```bash
cd .

# Setup (build all)
bash scripts/setup.sh

# Start RabbitMQ
bash scripts/start-rabbitmq.sh

# Individual service runners (run each in separate terminal)
bash scripts/run-project-2-config-server.sh
bash scripts/run-project-2-client-1.sh
bash scripts/run-project-2-client-2.sh
bash scripts/run-project-3-service-a.sh
bash scripts/run-project-3-service-b.sh

# Stop RabbitMQ (when done)
bash scripts/stop-rabbitmq.sh
```

---

## Verification (After All Services Started)

### Check All Services Running
```bash
# Should return 6 "UP" statuses
for port in 8080 8888 8081 8082 8091 8092; do
  echo "Port $port: $(curl -s http://localhost:$port/actuator/health | jq -r '.status')"
done
```

### Check RabbitMQ Dashboard
```bash
# Open in browser: http://localhost:15672
# Login: guest / guest
# Should show 5+ connections
```

### Test Configuration Server
```bash
curl http://localhost:8888/myapp/default
```

### Test Event Publishing
```bash
# Publish event from Service A
curl -X POST "http://localhost:8091/notify?message=Hello%20World"

# Check Service B logs for received event
```

---

## Available Endpoints

| Service | Port | Endpoint | Method | Description |
|---------|------|----------|--------|-------------|
| Config Server | 8888 | `/myapp/default` | GET | Get configuration |
| Config Server | 8888 | `/actuator/health` | GET | Health check |
| Config Client 1 | 8081 | `/actuator/health` | GET | Health check |
| Config Client 1 | 8081 | `/actuator/busrefresh` | POST | Refresh config |
| Config Client 2 | 8082 | `/actuator/health` | GET | Health check |
| Config Client 2 | 8082 | `/actuator/busrefresh` | POST | Refresh config |
| Service A | 8091 | `/notify?message=...` | POST | Publish event |
| Service A | 8091 | `/actuator/health` | GET | Health check |
| Service B | 8092 | `/actuator/health` | GET | Health check |
| Bus Foundation | 8080 | `/actuator/health` | GET | Health check |
| Bus Foundation | 8080 | `/actuator/busrefresh` | POST | Bus refresh |

---

## Typical Usage Scenario

### 1. Learn Configuration Management
```bash
# View current config for Client 1
curl http://localhost:8888/config-client-1/default

# View current config for Client 2
curl http://localhost:8888/config-client-2/default

# Edit config for Client 1
vi ./project-2-config-server/src/main/resources/config/config-client-1.yml

# Edit config for Client 2
vi ./project-2-config-server/src/main/resources/config/config-client-2.yml

# Refresh clients
curl -X POST http://localhost:8081/actuator/busrefresh

# Verify update on clients (returns only message and version from ConfigResponse DTO)
curl http://localhost:8081/config
curl http://localhost:8082/config
```

### 2. Learn Event Publishing
```bash
# Terminal 1: Monitor Service B logs
tail -f service-b.log

# Terminal 2: Publish events
curl -X POST "http://localhost:8091/notify?message=Event1"
curl -X POST "http://localhost:8091/notify?message=Event2"

# Check Terminal 1 for received events
```

### 3. Test Bus Communication
```bash
# Kill a service and restart to verify bus rebalancing
# Edit config to trigger refresh across all services
# Monitor message flow in RabbitMQ dashboard
```

---

## Cleanup

### Stop All Services
```bash
# Kill all Java processes
pkill -f "java.*spring-boot"

# Stop RabbitMQ
bash scripts/stop-rabbitmq.sh
```

### View Logs
```bash
# Check last 50 lines of specific service logs
# (Logs are printed to terminal where service was started)
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Port already in use" | `pkill -f "java.*spring-boot"` |
| RabbitMQ connection refused | `bash scripts/start-rabbitmq.sh` |
| Config Server startup error | Check if `spring.profiles.active: native` is in application.yml |
| Services can't find each other | Verify all services are on localhost and ports are correct |

---

## Performance Notes

- **Build time:** ~20 seconds for all 7 modules
- **Startup time:** ~30 seconds for all 6 services
- **Memory usage:** ~2GB total for all services
- **Disk usage:** ~500MB for built artifacts

**All services tested and verified working correctly! ✅**
