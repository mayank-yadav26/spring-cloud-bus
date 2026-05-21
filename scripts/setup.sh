#!/bin/bash

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Spring Cloud Bus - Project Setup ===${NC}"
echo ""

# Check Java
echo -e "${BLUE}Checking Java...${NC}"
if command -v java &> /dev/null; then
    java -version
    echo -e "${GREEN}✓ Java found${NC}"
else
    echo -e "${RED}✗ Java not found${NC}"
    exit 1
fi

echo ""

# Check Maven
echo -e "${BLUE}Checking Maven...${NC}"
if command -v mvn &> /dev/null; then
    mvn -version | head -1
    echo -e "${GREEN}✓ Maven found${NC}"
else
    echo -e "${RED}✗ Maven not found${NC}"
    exit 1
fi

echo ""

# Check Docker
echo -e "${BLUE}Checking Docker...${NC}"
if command -v docker &> /dev/null; then
    docker --version
    echo -e "${GREEN}✓ Docker found${NC}"
else
    echo -e "${RED}✗ Docker not found${NC}"
    exit 1
fi

echo ""

# Build all projects
echo -e "${BLUE}Building all projects...${NC}"
echo ""

projects=(
    "project-1-bus-foundation"
    "project-2-config-server"
    "project-2-config-client-1"
    "project-2-config-client-2"
)

for project in "${projects[@]}"; do
    echo -e "${BLUE}Building $project...${NC}"
    cd "$project"
    mvn clean install -q
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $project built successfully${NC}"
    else
        echo -e "${RED}✗ $project build failed${NC}"
        exit 1
    fi
    cd ..
done

echo ""

# Build Project 3 (shared module first, then services)
echo -e "${BLUE}Building Project 3 (custom events)...${NC}"
cd project-3-custom-events/shared
mvn clean install -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Project 3 shared module built${NC}"
else
    echo -e "${RED}✗ Project 3 shared module build failed${NC}"
    exit 1
fi

cd ../service-a
mvn clean install -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Project 3 Service A built${NC}"
else
    echo -e "${RED}✗ Project 3 Service A build failed${NC}"
    exit 1
fi

cd ../service-b
mvn clean install -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Project 3 Service B built${NC}"
else
    echo -e "${RED}✗ Project 3 Service B build failed${NC}"
    exit 1
fi

cd ../..

echo ""
echo -e "${GREEN}=== All projects built successfully! ===${NC}"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo "1. Start RabbitMQ: ./scripts/start-rabbitmq.sh"
echo "2. Run Project 1: cd project-1-bus-foundation && mvn spring-boot:run"
echo "3. Or read RUN_GUIDE.md for detailed instructions"
echo ""
