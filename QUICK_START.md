# ⚡ Quick Start Guide - Spring Cloud Bus Learning Path

## 📋 Summary of What's Been Created

✅ **All 4 projects are ready!**

- **Project 1:** Bus Foundation (1 service on port 8080)
- **Project 2:** Config Server + 2 Config Clients (3 services on ports 8888, 8081, 8082)
- **Project 3:** Custom Events - Service A + Service B (2 services on ports 8091, 8092)
- **Project 4:** Optional Multi-Service Coordination (can be created later)

**Total files created:** 50+  
**Total lines of code:** 2000+  
**All projects:** ✅ Built and ready to run

---

## 🚀 Get Started in 3 Minutes

### Step 1: Start RabbitMQ (1 min)

```bash
cd .
./scripts/start-rabbitmq.sh
```

RabbitMQ will run in Docker on:
- AMQP: `localhost:5672`
- Dashboard: `http://localhost:15672` (guest/guest)

### Step 2: Run Project 1 (1 min)

```bash
cd ./project-1-bus-foundation
mvn spring-boot:run
```

### Step 3: Test in another terminal (1 min)

```bash
# Test health
curl http://localhost:8080/health

# Trigger bus refresh
curl -X POST http://localhost:8080/actuator/busrefresh

# Check logs in the app terminal - you'll see bus messages!
```

**That's it! You've successfully tested Spring Cloud Bus connectivity! 🎉**

---

## 📚 Run All Projects (Without Shortcuts)

If you want to run all projects manually without scripts:

### Terminal 1: RabbitMQ
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine
```

### Terminal 2: Project 1
```bash
cd ./project-1-bus-foundation
mvn spring-boot:run
```

### Terminal 3: Project 2 Config Server
```bash
cd ./project-2-config-server
mvn spring-boot:run
```

### Terminal 4: Project 2 Client 1
```bash
cd ./project-2-config-client-1
mvn spring-boot:run
```

### Terminal 5: Project 2 Client 2
```bash
cd ./project-2-config-client-2
mvn spring-boot:run
```

### Terminal 6: Project 3 Service A
```bash
cd ./project-3-custom-events/service-a
mvn spring-boot:run
```

### Terminal 7: Project 3 Service B
```bash
cd ./project-3-custom-events/service-b
mvn spring-boot:run
```

---

## 🔗 Useful Commands

### Test Project 1
```bash
curl http://localhost:8080/health
curl -X POST http://localhost:8080/actuator/busrefresh
```

### Test Project 2
```bash
# Get config before update
curl http://localhost:8081/config

# Update config file, then refresh
curl -X POST http://localhost:8081/actuator/busrefresh

# Get config after update (should be updated on both clients!)
curl http://localhost:8081/config
curl http://localhost:8082/config
```

### Test Project 3
```bash
# Send event from Service A
curl -X POST "http://localhost:8091/notify?message=Hello"

# Check Service B logs - you should see the event!
```

### Stop RabbitMQ
```bash
./scripts/stop-rabbitmq.sh
```

---

## 📖 Detailed Documentation

For comprehensive, step-by-step instructions for each project, see:

👉 **[RUN_GUIDE.md](RUN_GUIDE.md)**

This document contains:
- Detailed verification steps for each project
- Troubleshooting section
- All endpoints and their expected responses
- File locations and structure

---

## 🎓 What You'll Learn

| Project | What You Learn |
|---------|-------|
| **1** | How Spring Cloud Bus connects to RabbitMQ |
| **2** | Dynamic configuration management without app restart |
| **3** | Custom event broadcasting between microservices |
| **4** | (Optional) Coordinating multiple services via bus |

---

## 📂 Project Structure

```
./
├── project-1-bus-foundation/       ← Start here!
├── project-2-config-server/        ← Then here
├── project-2-config-client-1/
├── project-2-config-client-2/
├── project-3-custom-events/
│   ├── shared/
│   ├── service-a/
│   └── service-b/
├── scripts/                        ← Helper scripts
├── RUN_GUIDE.md                    ← Full documentation
├── README.md                       ← Project overview
└── QUICK_START.md                  ← This file!
```

---

## ✅ Verification Checklist

After following Quick Start steps:

- [ ] RabbitMQ container is running
- [ ] Project 1 starts without errors
- [ ] `GET /health` returns "Bus Foundation App is running!"
- [ ] RabbitMQ dashboard shows a connection
- [ ] All projects built successfully in setup.sh

---

## 🆘 Common Issues & Fixes

| Problem | Solution |
|---------|----------|
| Docker error when starting RabbitMQ | Make sure Docker daemon is running |
| Port already in use (e.g., 8080) | Run `lsof -i :8080` then `kill -9 PID` |
| Maven build fails | Run `mvn clean install -U` to update dependencies |
| "Connection refused" when starting services | RabbitMQ is not running - start it first! |

---

## 🎯 Next Steps

1. **Complete Project 1** (30 min)
   - Understand bus connectivity basics
   - See RabbitMQ in action

2. **Complete Project 2** (1-1.5 hrs)
   - Learn configuration management at scale
   - Experience dynamic refresh without restart

3. **Complete Project 3** (1 hr)
   - Build custom events
   - See inter-service communication

4. **Explore Project 4** (Optional, 1-1.5 hrs)
   - Combine all concepts
   - Build more complex scenarios

---

## 📖 Learn More

- **RUN_GUIDE.md** - Complete project guide
- **README.md** - Project overview and resources
- **Plan document** - Learning path details (`.github/prompts/plan-springCloudBus.prompt.md`)

---

## 💡 Key Takeaway

**Spring Cloud Bus = Distributed communication framework for microservices**

Using RabbitMQ as the message broker, it enables:
- Configuration updates propagated to all services instantly
- Custom events broadcast across your system
- Loosely coupled microservice architecture

**Now you understand how large-scale distributed systems communicate! 🚀**

---

## 📞 Files & Locations

| File | Purpose |
|------|---------|
| `RUN_GUIDE.md` | Step-by-step guide for all 4 projects |
| `README.md` | Project overview & quick reference |
| `QUICK_START.md` | This file! 3-minute quick start |
| `.github/prompts/plan-springCloudBus.prompt.md` | Learning plan & design decisions |
| `scripts/` | Helper shell scripts |

---

**Ready? Start with: `./scripts/start-rabbitmq.sh`**

**Then: `cd project-1-bus-foundation && mvn spring-boot:run`**

**Enjoy learning Spring Cloud Bus! 🎉**
