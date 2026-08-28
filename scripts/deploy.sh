#!/bin/bash
cd server
docker-compose down
docker-compose pull
docker-compose up -d
echo "Server deployed successfully"
