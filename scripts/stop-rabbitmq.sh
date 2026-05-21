#!/bin/bash

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Stopping RabbitMQ ===${NC}"
echo ""

if docker ps -a --format '{{.Names}}' | grep -q "rabbitmq"; then
    echo -e "${BLUE}Stopping RabbitMQ...${NC}"
    docker stop rabbitmq
    
    echo -e "${BLUE}Removing RabbitMQ container...${NC}"
    docker rm rabbitmq
    
    echo -e "${GREEN}✓ RabbitMQ stopped and removed${NC}"
else
    echo -e "${RED}RabbitMQ container not found${NC}"
fi
