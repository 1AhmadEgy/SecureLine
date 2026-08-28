#!/bin/bash
echo "Deploying SecureLine Server..."
cd server
docker-compose down
docker-compose build --no-cache
docker-compose up -d
sleep 5
curl -s http://localhost:8080/health && echo "Server is healthy" || echo "Server health check failed"
