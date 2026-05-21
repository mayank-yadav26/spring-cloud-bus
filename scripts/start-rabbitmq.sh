#!/bin/bash

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Starting RabbitMQ ===${NC}"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker first."
    exit 1
fi

# Check if RabbitMQ container already exists
if docker ps -a --format '{{.Names}}' | grep -q "rabbitmq"; then
    echo "Removing existing RabbitMQ container..."
    docker stop rabbitmq 2>/dev/null
    docker rm rabbitmq 2>/dev/null
fi

# Start RabbitMQ
echo -e "${BLUE}Starting RabbitMQ container...${NC}"
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management-alpine

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ RabbitMQ started successfully${NC}"
    echo ""
    echo -e "${BLUE}RabbitMQ Dashboard:${NC} http://localhost:15672"
    echo -e "${BLUE}Username:${NC} guest"
    echo -e "${BLUE}Password:${NC} guest"
    echo ""
    echo "Waiting for RabbitMQ to be ready..."
    sleep 3
    echo -e "${GREEN}✓ RabbitMQ is ready!${NC}"
else
    echo "Failed to start RabbitMQ"
    exit 1
fi
